package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.HibernateProxyTypeAdapter;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.entity.RoleGroup;
import cn.exrick.xboot.modules.base.service.RoleGroupService;
import cn.exrick.xboot.modules.base.service.RoleService;
import cn.exrick.xboot.modules.your.entity.RoleArchives;
import cn.exrick.xboot.modules.your.entity.RoleRolegroup;
import cn.exrick.xboot.modules.your.service.IRoleArchivesService;
import cn.exrick.xboot.modules.your.service.IRoleRolegroupService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "角色管理档案管理接口")
@RequestMapping("/xboot/roleArchives")
@Transactional
public class RoleArchivesController {

    @Autowired
    private IRoleArchivesService iRoleArchivesService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleGroupService roleGroupService;

    @Autowired
    private IRoleRolegroupService iRoleRolegroupService;


    @SystemLog(description = "查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByAllSubInTreeByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取所有子节点并展开")
    public Result<List<RoleArchives>> getByAllSubInTreeByParentId(@PathVariable String parentId,
                                                                 @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){
        List<RoleArchives> list = new ArrayList<>();
        list = getByParentIdInTree(parentId, openDataFilter);
        for (RoleArchives jueSeManger : list) {
            jueSeManger.setTitleShow(jueSeManger.getTitle());
            jueSeManger.setTitle(jueSeManger.getTitle() + "（" + jueSeManger.getRequirement() + "）");
        }
        Result<List<RoleArchives>> result=new ResultUtil<List<RoleArchives>>().setData(list);
        return result;
    }

    public List<RoleArchives> getByParentIdInTree(String parentId,Boolean openDataFilter) {
        List<RoleArchives> list = new ArrayList<>();
        QueryWrapper<RoleArchives> qw = new QueryWrapper<RoleArchives>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iRoleArchivesService.list(qw);
        // list = iJueSeMangerService.findByParentIdOrderBySortOrder(parentId,openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            RoleArchives jueSeManger=list.get(i);
            jueSeManger.setChildren(getByParentIdInTree(jueSeManger.getId(),true));
            jueSeManger.setTitleShow(jueSeManger.getTitle());
            jueSeManger.setTitle(jueSeManger.getTitle() + "（" + jueSeManger.getRequirement() + "）");
        }
        return list;
    }
    public List<RoleArchives> setInfo(List<RoleArchives> list){

        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){

                RoleArchives parent = null; // = iJueSeMangerService.get(item.getParentId());
                QueryWrapper<RoleArchives> qw = new QueryWrapper<RoleArchives>();
                if(StrUtil.isNotBlank(item.getParentId())) {
                    qw.like("id", item.getParentId());
                }
                List<RoleArchives> list1 = iRoleArchivesService.list(qw);
                if(!list1.isEmpty()){
                    parent = list1.get(0);
                }
                item.setParentTitle(parent.getTitle());
            }else{
                item.setParentTitle("一级角色组");
            }

        });
        return list;
    }

    @SystemLog(description = "查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取")
    public Result<List<RoleArchives>> getByParentId(@PathVariable String parentId,
                                                   @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<RoleArchives> list = new ArrayList<>();
        QueryWrapper<RoleArchives> qw = new QueryWrapper<RoleArchives>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iRoleArchivesService.list(qw);
        list = setInfo(list);
        for (RoleArchives jueSeManger : list) {
            jueSeManger.setTitleShow(jueSeManger.getTitle());
            jueSeManger.setTitle(jueSeManger.getTitle() + "（" + jueSeManger.getRequirement() + "）");
        }
        return new ResultUtil<List<RoleArchives>>().setData(list);
    }

    @SystemLog(description = "新增角色", type = LogType.OPERATION)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(RoleArchives jueSeManger){

        iRoleArchivesService.save(jueSeManger);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if(!CommonConstant.PARENT_ID.equals(jueSeManger.getParentId())){
            QueryWrapper<RoleArchives> qw = new QueryWrapper<RoleArchives>();
            if(StrUtil.isNotBlank(jueSeManger.getParentId())) {
                qw.eq("id", jueSeManger.getParentId());
            }
            List<RoleArchives> list = iRoleArchivesService.list(qw);
            RoleArchives parent = null;
            if(!list.isEmpty()){
                parent = list.get(0);
            }
            if(parent.getIsParent()==null||!parent.getIsParent()){
                parent.setIsParent(true);
                iRoleArchivesService.saveOrUpdate(parent);
            }
        }
        return ResultUtil.success("添加成功");
    }

    @SystemLog(description = "编辑角色", type = LogType.OPERATION)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(RoleArchives jueSeManger,
                               @RequestParam(required = false) String[] mainHeader,
                               @RequestParam(required = false) String[] viceHeader){

        QueryWrapper<RoleArchives> qw = new QueryWrapper<RoleArchives>();
        if(StrUtil.isNotBlank(jueSeManger.getId())) {
            qw.eq("id", jueSeManger.getId());
        }
        List<RoleArchives> list = iRoleArchivesService.list(qw);
        if(!list.isEmpty()){
            RoleArchives old = list.get(0);
            iRoleArchivesService.saveOrUpdate(jueSeManger);
            return ResultUtil.success("编辑成功");
        }
        return ResultUtil.success("原先数据不存在");
    }

    @SystemLog(description = "删除角色", type = LogType.OPERATION)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@RequestParam String[] ids){

        for (String id : ids) {
            iRoleArchivesService.removeById(Long.parseLong(id));
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @SystemLog(description = "模糊搜索角色", type = LogType.OPERATION)
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "岗位名模糊搜索")
    public Result<List<RoleArchives>> searchByTitle(@RequestParam String title,
                                                   @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        QueryWrapper<RoleArchives> qw = new QueryWrapper<RoleArchives>();
        if(StrUtil.isNotBlank(title)) {
            qw.like("title", "%"+title+"%");
        }
        List<RoleArchives> list = iRoleArchivesService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<RoleArchives>>().setData(list);
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<RoleArchives> get(@PathVariable Long id){

        RoleArchives jueSeManger = iRoleArchivesService.getById(id);
        return new ResultUtil<RoleArchives>().setData(jueSeManger);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<RoleArchives>> getAll(){
        QueryWrapper<RoleArchives> qw = new QueryWrapper<RoleArchives>();
        qw.eq("is_parent", 0l);
        List<RoleArchives> list = iRoleArchivesService.list(qw);
        return new ResultUtil<List<RoleArchives>>().setData(list);
    }

    @SystemLog(description = "分页查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<RoleArchives>> getByPage(PageVo page){

        IPage<RoleArchives> data = iRoleArchivesService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<RoleArchives>>().setData(data);
    }

    @SystemLog(description = "查询没有在此角色组的角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getNotImportByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<RoleArchives>> getNotImportByPage(@RequestParam String roleGroupId, @ModelAttribute PageVo page){
//        List<RoleArchives> ans = new ArrayList<>();
//        if(StrUtil.isNotBlank(roleGroupId)) {
//            ans = iRoleRolegroupService.findNotInRoleGroup(roleGroupId);
//        }else{
//            ans = iRoleRolegroupService.list();
//        }

        IPage<RoleArchives> data = iRoleArchivesService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<RoleArchives>>().setData(data);
    }

    @SystemLog(description = "查询角色列表", type = LogType.OPERATION)
    @RequestMapping(value = "/getJueListByPage", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<IPage<RoleArchives>> getByCondition(@ModelAttribute PageVo page){
        QueryWrapper<RoleArchives> qw = new QueryWrapper<RoleArchives>();
        qw.eq("is_parent", 0l);
        IPage<RoleArchives> data = iRoleArchivesService.page(PageUtil.initMpPage(page), qw);
        return new ResultUtil<IPage<RoleArchives>>().setData(data);
    }

    @SystemLog(description = "导入角色至角色组", type = LogType.OPERATION)
    @RequestMapping(value = "/importJueSe", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<RoleArchives> importJueSe(@RequestParam Long[] ids,String partid){
        RoleGroup rg = roleGroupService.findRoleGroupByIdById(partid);
        String roleGroupName = "";
        if(rg != null){
            roleGroupName = rg.getTitle();
        }
//        String[] idss = ids.split(",");
        for (Long id : ids) {
            RoleArchives j = iRoleArchivesService.getById(id);
            if(j != null){
                Role role = new Role();
                role.setName(j.getTitle());
                role.setDescription(j.getRequirement());
                role.setCreateTime(j.getCreateTime());
                role.setRoleGroupId(partid);
                role.setRoleGroupName(roleGroupName);
                roleService.save(role);
            }
        }
        return new ResultUtil<RoleArchives>().setSuccessMsg("操作成功");
    }


    @SystemLog(description = "新增角色", type = LogType.OPERATION)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<RoleArchives> saveOrUpdate(RoleArchives jueSeManger){

        if(iRoleArchivesService.saveOrUpdate(jueSeManger)){
            return new ResultUtil<RoleArchives>().setData(jueSeManger);
        }
        return new ResultUtil<RoleArchives>().setErrorMsg("操作失败");
    }

//    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
//    @ApiOperation(value = "批量通过id删除")
//    public Result<Object> delAllByIds(@RequestParam Long[] ids){
//
//        for(Long id : ids){
//            iJueSeMangerService.removeById(id);
//        }
//        return ResultUtil.success("批量通过id删除数据成功");
//    }
}
