package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.service.mybatis.IUserService;
import cn.exrick.xboot.modules.crm.entity.JingLi;
import cn.exrick.xboot.modules.crm.service.IJingLiService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "经理档案管理接口")
@RequestMapping("/xboot/jingLi")
@Transactional
public class JingLiController {

    @Autowired
    private IJingLiService iJingLiService;

    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "/importById", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> importById(@RequestParam String[] ids,@RequestParam String pid){

        for(String id : ids){
            User user = iUserService.findByIdZwz("" + id);
            JingLi li = new JingLi();
            li.setTitle(user.getNickname());
            li.setParentId(pid);
            li.setJobnumber(user.getUsername());
            li.setMobile(user.getMobile());
            li.setSortOrder(BigDecimal.ZERO);
            iJingLiService.save(li);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/getNotParent", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部子客商类型")
    public Result<List<JingLi>> getNotParent(){
        QueryWrapper<JingLi> qw = new QueryWrapper<JingLi>();
        qw.eq("is_parent", 0l);
        List<JingLi> list = iJingLiService.list(qw);
        return new ResultUtil<List<JingLi>>().setData(list);
    }

    @SystemLog(description = "查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByAllSubInTreeByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取所有子节点并展开")
    public Result<List<JingLi>> getByAllSubInTreeByParentId(@PathVariable String parentId,
                                                                 @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){
        List<JingLi> list = new ArrayList<>();
        list = getByParentIdInTree(parentId, openDataFilter);
        Result<List<JingLi>> result=new ResultUtil<List<JingLi>>().setData(list);
        return result;
    }

    public List<JingLi> getByParentIdInTree(String parentId,Boolean openDataFilter) {
        List<JingLi> list = new ArrayList<>();
        QueryWrapper<JingLi> qw = new QueryWrapper<JingLi>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iJingLiService.list(qw);
        Collections.sort(list, new Comparator<JingLi>() {
            @Override

            public int compare(JingLi o1, JingLi o2) {
                return o1.getSortOrder().compareTo(o2.getSortOrder());
            }
        });
        // list = iJingLiService.findByParentIdOrderBySortOrder(parentId,openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            JingLi JingLi=list.get(i);
            JingLi.setChildren(getByParentIdInTree(JingLi.getId(),true));
        }
        return list;
    }
    public List<JingLi> setInfo(List<JingLi> list){

        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){

                JingLi parent = null; // = iJingLiService.get(item.getParentId());
                QueryWrapper<JingLi> qw = new QueryWrapper<JingLi>();
                if(StrUtil.isNotBlank(item.getParentId())) {
                    qw.like("id", item.getParentId());
                }
                List<JingLi> list1 = iJingLiService.list(qw);
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
    public Result<List<JingLi>> getByParentId(@PathVariable String parentId,
                                                   @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<JingLi> list = new ArrayList<>();
        QueryWrapper<JingLi> qw = new QueryWrapper<JingLi>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iJingLiService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<JingLi>>().setData(list);
    }

    @SystemLog(description = "新增角色", type = LogType.OPERATION)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(JingLi JingLi){

        iJingLiService.save(JingLi);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if(!CommonConstant.PARENT_ID.equals(JingLi.getParentId())){
            QueryWrapper<JingLi> qw = new QueryWrapper<JingLi>();
            if(StrUtil.isNotBlank(JingLi.getParentId())) {
                qw.eq("parent_id", JingLi.getParentId());
            }
            List<JingLi> list = iJingLiService.list(qw);
            JingLi parent = null;
            if(!list.isEmpty()){
                parent = list.get(0);
            }
            if(parent.getIsParent()==null||!parent.getIsParent()){
                parent.setIsParent(true);
                iJingLiService.saveOrUpdate(parent);
            }
        }
        return ResultUtil.success("添加成功");
    }

    @SystemLog(description = "编辑角色", type = LogType.OPERATION)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(JingLi JingLi,
                               @RequestParam(required = false) String[] mainHeader,
                               @RequestParam(required = false) String[] viceHeader){

        QueryWrapper<JingLi> qw = new QueryWrapper<JingLi>();
        if(StrUtil.isNotBlank(JingLi.getId())) {
            qw.eq("id", JingLi.getId());
        }
        List<JingLi> list = iJingLiService.list(qw);
        if(!list.isEmpty()){
            JingLi old = list.get(0);
            iJingLiService.saveOrUpdate(JingLi);
            return ResultUtil.success("编辑成功");
        }
        return ResultUtil.success("原先数据不存在");
    }

    @SystemLog(description = "删除角色", type = LogType.OPERATION)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@RequestParam String[] ids){

        for (String id : ids) {
            iJingLiService.removeById(Long.parseLong(id));
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @SystemLog(description = "模糊搜索角色", type = LogType.OPERATION)
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "岗位名模糊搜索")
    public Result<List<JingLi>> searchByTitle(@RequestParam String title,
                                                   @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        QueryWrapper<JingLi> qw = new QueryWrapper<JingLi>();
        if(StrUtil.isNotBlank(title)) {
            qw.like("title", "%"+title+"%");
        }
        List<JingLi> list = iJingLiService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<JingLi>>().setData(list);
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<JingLi> get(@PathVariable Long id){

        JingLi JingLi = iJingLiService.getById(id);
        return new ResultUtil<JingLi>().setData(JingLi);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<JingLi>> getAll(){

        List<JingLi> list = iJingLiService.list();
        return new ResultUtil<List<JingLi>>().setData(list);
    }

    @SystemLog(description = "分页查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<JingLi>> getByPage(PageVo page){

        IPage<JingLi> data = iJingLiService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<JingLi>>().setData(data);
    }

    @SystemLog(description = "新增角色", type = LogType.OPERATION)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<JingLi> saveOrUpdate(JingLi JingLi){

        if(iJingLiService.saveOrUpdate(JingLi)){
            return new ResultUtil<JingLi>().setData(JingLi);
        }
        return new ResultUtil<JingLi>().setErrorMsg("操作失败");
    }
}
