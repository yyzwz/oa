package cn.exrick.xboot.modules.base.controller.manage;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.redis.RedisTemplateHelper;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.dao.mapper.DeleteMapper;
import cn.exrick.xboot.modules.base.entity.*;
import cn.exrick.xboot.modules.base.service.*;
import cn.exrick.xboot.modules.base.service.mybatis.IRoleService;
import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.service.mybatis.IUserRoleService;
import cn.exrick.xboot.modules.your.entity.RoleRolegroup;
import cn.exrick.xboot.modules.your.entity.WarehouseOut;
import cn.exrick.xboot.modules.your.entity.WaterCost;
import cn.exrick.xboot.modules.your.service.IRoleRolegroupService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "角色管理接口")
@RequestMapping("/xboot/role")
@Transactional
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private DeleteMapper deleteMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private RoleGroupService roleGroupService;

    @Autowired
    private IRoleRolegroupService iRoleRolegroupService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @RequestMapping(value = "/getByAllSubInTreeByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取所有子节点并展开")
    public Result<List<Role>> getByAllSubInTreeByParentId(@PathVariable String parentId,
                                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){
        List<Role> list = new ArrayList<>();
        list = getByParentIdInTree(parentId, openDataFilter);
        for (Role jueSeManger : list) {
            jueSeManger.setTitleShow(jueSeManger.getTitle());
            jueSeManger.setTitle(jueSeManger.getTitle() + "（" + jueSeManger.getRequirement() + "）");
        }
        Result<List<Role>> result=new ResultUtil<List<Role>>().setData(list);
        return result;
    }

    public List<Role> getByParentIdInTree(String parentId,Boolean openDataFilter) {
        List<Role> list = new ArrayList<>();
        QueryWrapper<Role> qw = new QueryWrapper<Role>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iRoleService.list(qw);
        // list = iJueSeMangerService.findByParentIdOrderBySortOrder(parentId,openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            Role jueSeManger=list.get(i);
            jueSeManger.setChildren(getByParentIdInTree(jueSeManger.getId(),true));
            jueSeManger.setTitleShow(jueSeManger.getTitle());
            jueSeManger.setTitle(jueSeManger.getTitle() + "（" + jueSeManger.getRequirement() + "）");
        }
        return list;
    }
    public List<Role> setInfo(List<Role> list){

        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){

                Role parent = null; // = iJueSeMangerService.get(item.getParentId());
                QueryWrapper<Role> qw = new QueryWrapper<Role>();
                if(StrUtil.isNotBlank(item.getParentId())) {
                    qw.like("id", item.getParentId());
                }
                List<Role> list1 = iRoleService.list(qw);
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
    public Result<List<Role>> getByParentId(@PathVariable String parentId,
                                                    @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<Role> list = new ArrayList<>();
        QueryWrapper<Role> qw = new QueryWrapper<Role>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iRoleService.list(qw);
        list = setInfo(list);
        for (Role jueSeManger : list) {
            jueSeManger.setTitleShow(jueSeManger.getTitle());
            jueSeManger.setTitle(jueSeManger.getTitle() + "（" + jueSeManger.getRequirement() + "）");
        }
        return new ResultUtil<List<Role>>().setData(list);
    }

    @SystemLog(description = "新增角色", type = LogType.OPERATION)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(Role jueSeManger){

        iRoleService.save(jueSeManger);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if(!CommonConstant.PARENT_ID.equals(jueSeManger.getParentId())){
            QueryWrapper<Role> qw = new QueryWrapper<Role>();
            if(StrUtil.isNotBlank(jueSeManger.getParentId())) {
                qw.eq("id", jueSeManger.getParentId());
            }
            List<Role> list = iRoleService.list(qw);
            Role parent = null;
            if(!list.isEmpty()){
                parent = list.get(0);
            }
            if(parent.getIsParent()==null||!parent.getIsParent()){
                parent.setIsParent(true);
                iRoleService.saveOrUpdate(parent);
            }
        }
        return ResultUtil.success("添加成功");
    }

//    @SystemLog(description = "编辑角色", type = LogType.OPERATION)
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation(value = "编辑")
//    public Result<Object> edit(Role jueSeManger,
//                               @RequestParam(required = false) String[] mainHeader,
//                               @RequestParam(required = false) String[] viceHeader){
//
//        QueryWrapper<Role> qw = new QueryWrapper<Role>();
//        if(StrUtil.isNotBlank(jueSeManger.getId())) {
//            qw.eq("id", jueSeManger.getId());
//        }
//        List<Role> list = iRoleService.list(qw);
//        if(!list.isEmpty()){
//            Role old = list.get(0);
//            iRoleService.saveOrUpdate(jueSeManger);
//            return ResultUtil.success("编辑成功");
//        }
//        return ResultUtil.success("原先数据不存在");
//    }



    @SystemLog(description = "模糊搜索角色", type = LogType.OPERATION)
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "岗位名模糊搜索")
    public Result<List<Role>> searchByTitle(@RequestParam String title,
                                                    @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        QueryWrapper<Role> qw = new QueryWrapper<Role>();
        if(StrUtil.isNotBlank(title)) {
            qw.like("title", "%"+title+"%");
        }
        List<Role> list = iRoleService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<Role>>().setData(list);
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Role> get(@PathVariable Long id){

        Role jueSeManger = iRoleService.getById(id);
        return new ResultUtil<Role>().setData(jueSeManger);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Role>> getAll(){
        QueryWrapper<Role> qw = new QueryWrapper<Role>();
        qw.eq("is_parent", 0l);
        List<Role> list = iRoleService.list(qw);
        return new ResultUtil<List<Role>>().setData(list);
    }

    @RequestMapping(value = "/findNotImportUserByRoleId", method = RequestMethod.GET)
    @ApiOperation(value = "获取未加入指定角色的用户")
    public Result<List<User>> findNotImportUserByRoleId(String roleId){
        if(StrUtil.isNotBlank(roleId)){
            List<User> users = iRoleService.findNotImportUserByRoleId(roleId);
            return new ResultUtil<List<User>>().setData(users);
        }
        return new ResultUtil<List<User>>().setData(null);
    }

    @RequestMapping(value = "/importUserByRoleId", method = RequestMethod.POST)
    @ApiOperation(value = "批量导入用户到角色")
    public Result<Object> importUserByRoleId(@RequestParam String[] ids,@RequestParam String roleId){
        if(StrUtil.isNotBlank(roleId)){
            for(String id : ids){
                UserRole ur = new UserRole();
                ur.setRoleId(roleId);
                ur.setUserId(id);
                iUserRoleService.saveOrUpdate(ur);
            }
            return ResultUtil.success("导入成功");
        }
        return ResultUtil.error("导入失败");
    }

    /**
     * 角色组导入角色页面  不分页
     * @return
     */
    @SystemLog(description = "分页查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getAllRoles", method = RequestMethod.GET)
    @ApiOperation(value = "不分页获取")
    public Result<List<Role>> getAllRoles(){

        QueryWrapper<Role> qw = new QueryWrapper<Role>();
        qw.eq("is_parent", "0");
        List<Role> data = iRoleService.list(qw);
        return new ResultUtil<List<Role>>().setData(data);
    }

    @SystemLog(description = "分页查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Role>> getByPage(PageVo page){

        QueryWrapper<Role> qw = new QueryWrapper<Role>();
        qw.eq("is_parent", "0");
        IPage<Role> data = iRoleService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Role>>().setData(data);
    }

    @SystemLog(description = "查询没有在此角色组的角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getNotImportByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Role>> getNotImportByPage(@RequestParam String roleGroupId, @ModelAttribute PageVo page){
//        List<Role> ans = new ArrayList<>();
//        if(StrUtil.isNotBlank(roleGroupId)) {
//            ans = iRoleRolegroupService.findNotInRoleGroup(roleGroupId);
//        }else{
//            ans = iRoleRolegroupService.list();
//        }

        IPage<Role> data = iRoleService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<Role>>().setData(data);
    }

    @SystemLog(description = "查询角色列表", type = LogType.OPERATION)
    @RequestMapping(value = "/getJueListByPage", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<IPage<Role>> getByCondition(@ModelAttribute PageVo page){
        QueryWrapper<Role> qw = new QueryWrapper<Role>();
        qw.eq("is_parent", 0l);
        IPage<Role> data = iRoleService.page(PageUtil.initMpPage(page), qw);
        return new ResultUtil<IPage<Role>>().setData(data);
    }

    @SystemLog(description = "导入角色至角色组", type = LogType.OPERATION)
    @RequestMapping(value = "/importJueSe", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<Role> importJueSe(@RequestParam Long[] ids,String partid){
        RoleGroup rg = roleGroupService.findRoleGroupByIdById(partid);
        if(rg == null){
            return new ResultUtil<Role>().setErrorMsg("角色组不存在");
        }
        for (Long id : ids) {
            RoleRolegroup roleRolegroup = new RoleRolegroup();
            roleRolegroup.setRoleId("" + id);
            roleRolegroup.setRoleGroupId(partid);
            iRoleRolegroupService.save(roleRolegroup);
        }
        return new ResultUtil<Role>().setSuccessMsg("操作成功");
    }


    @SystemLog(description = "新增角色", type = LogType.OPERATION)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Role> saveOrUpdate(Role jueSeManger){

        if(iRoleService.saveOrUpdate(jueSeManger)){
            return new ResultUtil<Role>().setData(jueSeManger);
        }
        return new ResultUtil<Role>().setErrorMsg("操作失败");
    }


    @RequestMapping(value = "/getAllList", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部角色")
    public Result<Object> roleGetAll(){
        QueryWrapper<Role> qw = new QueryWrapper<Role>();
        qw.eq("is_parent", "0");
        List<Role> list = iRoleService.list(qw);
        return ResultUtil.data(list);
    }


    /**
     * 角色前段管理 加载全部角色用
     * @param page
     * @param roleGroupId
     * @return
     */
    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取角色")
    public Result<IPage<Role>> getRoleByPage(@ModelAttribute PageVo page, @RequestParam String roleGroupId){
        List<Role> ans = new ArrayList<>();

        if(StrUtil.isNotBlank(roleGroupId) && roleGroupId.equals("-1")){
            ans = iRoleService.findAllRoles();
        }else{
            ans = iRoleService.findByRoleGroupId(roleGroupId);
        }
        for (Role role : ans) {
            role.setPermissions(rolePermissionService.findByRoleId(role.getId()));
        }
        IPage<Role> data = iRoleService.page(PageUtil.initMpPage(page));
        data.setRecords(ans);
        data.setPages(page.getPageNumber());
        data.setSize(page.getPageSize());
        data.setTotal(ans.size());
        return new ResultUtil<IPage<Role>>().setData(data);
//        Page<Role> list = roleService.findAll(new Specification<Role>() {
//            @Override
//            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
//                Path<String> roleGroupIdField = root.get("roleGroupId");
//                Path<Boolean> isParent = root.get("isParent");
//                List<Predicate> list = new ArrayList<Predicate>();
//                //角色组Id
//                if(roleGroupId!=null && !(roleGroupId.equals("-1"))){
//                    list.add(cb.equal(roleGroupIdField,roleGroupId));
//                }
//                list.add(cb.equal(isParent,false));
//                List<String> ids = new ArrayList();
//                Predicate[] arr = new Predicate[list.size()];
//                cq.where(list.toArray(arr));
//                return null;
//            }
//        }, PageUtil.initPage(page));
//
//        // Page<Role> list = roleService.findAll(PageUtil.initPage(page));
//        for(Role role : list.getContent()){
//            // 角色拥有权限
//            List<RolePermission> permissions = rolePermissionService.findByRoleId(role.getId());
//            role.setPermissions(permissions);
//            // 角色拥有数据权限
//            List<RoleDepartment> departments = roleDepartmentService.findByRoleId(role.getId());
//            role.setDepartments(departments);
//        }
//        return new ResultUtil<Page<Role>>().setData(list);
    }

    @RequestMapping(value = "/setDefault", method = RequestMethod.POST)
    @ApiOperation(value = "设置或取消默认角色")
    public Result<Object> setDefault(@RequestParam String id,
                                     @RequestParam Boolean isDefault){

        Role role = roleService.get(id);
        if(role==null){
            return ResultUtil.error("角色不存在");
        }
        role.setDefaultRole(isDefault);
        roleService.update(role);
        return ResultUtil.success("设置成功");
    }

    @RequestMapping(value = "/editRolePerm", method = RequestMethod.POST)
    @ApiOperation(value = "编辑角色分配菜单权限")
    public Result<Object> editRolePerm(@RequestParam String roleId,
                                       @RequestParam(required = false) String[] permIds){

        // 删除其关联权限
        rolePermissionService.deleteByRoleId(roleId);
        // 批量分配新权限
        if(permIds!=null){
            List<RolePermission> list = Arrays.asList(permIds).stream().map(e -> {
                return new RolePermission().setRoleId(roleId).setPermissionId(e);
            }).collect(Collectors.toList());
            rolePermissionService.saveOrUpdateAll(list);
        }
        //手动批量删除缓存
        Set<String> keysUser = redisTemplateHelper.keys("user:" + "*");
        redisTemplate.delete(keysUser);
        Set<String> keysUserRole = redisTemplateHelper.keys("userRole:" + "*");
        redisTemplate.delete(keysUserRole);
        Set<String> keysUserMenu = redisTemplateHelper.keys("permission::userMenuList:*");
        redisTemplate.delete(keysUserMenu);
        return ResultUtil.data(null);
    }

    @RequestMapping(value = "/editRoleDep", method = RequestMethod.POST)
    @ApiOperation(value = "编辑角色分配数据权限")
    public Result<Object> editRoleDep(@RequestParam String roleId,
                                      @RequestParam Integer dataType,
                                      @RequestParam(required = false) String[] depIds){

        Role r = roleService.get(roleId);
        r.setDataType(dataType);
        roleService.update(r);
        if(CommonConstant.DATA_TYPE_CUSTOM.equals(dataType)){
            // 删除其关联数据权限
            roleDepartmentService.deleteByRoleId(roleId);
            // 批量分配新数据权限
            if(depIds!=null){
                List<RoleDepartment> list = Arrays.asList(depIds).stream().map(e -> {
                    return new RoleDepartment().setRoleId(roleId).setDepartmentId(e);
                }).collect(Collectors.toList());
                roleDepartmentService.saveOrUpdateAll(list);
            }
        }
        // 手动删除相关缓存
        Set<String> keys = redisTemplateHelper.keys("department:" + "*");
        redisTemplate.delete(keys);
        Set<String> keysUserRole = redisTemplateHelper.keys("userRole:" + "*");
        redisTemplate.delete(keysUserRole);

        return ResultUtil.data(null);
    }

    @SystemLog(description = "新增角色权限", type = LogType.INSERT)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存数据")
    public Result<Role> save(Role role){

        Role r = roleService.save(role);
        return new ResultUtil<Role>().setData(r);
    }

    @SystemLog(description = "修改角色权限", type = LogType.INSERT)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "更新数据")
    public Result<Role> edit(Role entity){

        Role r = roleService.update(entity);
        //手动批量删除缓存
        Set<String> keysUser = redisTemplateHelper.keys("user:" + "*");
        redisTemplate.delete(keysUser);
        Set<String> keysUserRole = redisTemplateHelper.keys("userRole:" + "*");
        redisTemplate.delete(keysUserRole);
        return new ResultUtil<Role>().setData(r);
    }

    @SystemLog(description = "删除角色权限", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过ids删除")
    public Result<Object> delByIds(@RequestParam String[] ids){

        for(String id:ids){
            List<UserRole> list = userRoleService.findByRoleId(id);
            if(list!=null&&list.size()>0){
                return ResultUtil.error("删除失败，包含正被用户使用关联的角色");
            }
        }
        for(String id:ids){
            roleService.delete(id);
            //删除关联菜单权限
            rolePermissionService.deleteByRoleId(id);
            //删除关联数据权限
            roleDepartmentService.deleteByRoleId(id);
            // 删除流程关联节点
            deleteMapper.deleteActNode(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

}
