package cn.exrick.xboot.modules.base.controller.manage;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.exception.XbootException;
import cn.exrick.xboot.common.redis.RedisTemplateHelper;
import cn.exrick.xboot.common.utils.*;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.dao.mapper.DeleteMapper;
import cn.exrick.xboot.modules.base.entity.*;
import cn.exrick.xboot.modules.base.entity.dingtalk.DingtalkDepartment;
import cn.exrick.xboot.modules.base.entity.dingtalk.DingtalkUser;
import cn.exrick.xboot.modules.base.service.*;
import cn.exrick.xboot.modules.base.utils.DingtalkUtils;
import cn.exrick.xboot.modules.your.entity.Archive;
import cn.exrick.xboot.modules.your.entity.Roster;
import cn.exrick.xboot.modules.your.service.IArchiveService;
import cn.exrick.xboot.modules.your.service.IRosterService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "部门管理接口")
@RequestMapping("/xboot/department")
@CacheConfig(cacheNames = "department")
@Transactional
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private DepartmentHeaderService departmentHeaderService;

    @Autowired
    private DeleteMapper deleteMapper;

    @Autowired
    private IArchiveService iArchiveService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IRosterService iRosterService;

    @Autowired
    private UserDepartmentService userDepartmentService;

    /**
     * 导入花名册的数据
     * @param ids
     * @return
     * @throws ParseException
     */
    // 事务 注解 否则try catch无法正常获取抛出的异常
    @SystemLog(description = "花名册导入用户", type = LogType.OPERATION)
    @Transactional(propagation = Propagation.SUPPORTS)
    @RequestMapping(value = "/importFromRoster", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id花名册导入")
    public Result<Object> importAllByIds(@RequestParam String[] ids,@RequestParam String departmentId){
        for(String id : ids){
            User user = userService.findByRosterId(id);
            if(user != null){
                UserDepartment ud = new UserDepartment();
                ud.setDepartmentId(departmentId);
                ud.setUserId(id);
                userDepartmentService.save(ud);
            }
            // 删除未挂靠部门
            departmentService.deleteNotFlag(user.getId());
        }
        return ResultUtil.success("批量通过id导入花名册数据成功");
    }

    /**
     * 从用户导入到组织架构
     * @param ids
     * @param departmentId
     * @return
     */
    @SystemLog(description = "批量导入用户", type = LogType.OPERATION)
    @Transactional(propagation = Propagation.SUPPORTS)
    @RequestMapping(value = "/importFromUser", method = RequestMethod.POST)
    @ApiOperation(value = "批量导入用户")
    public Result<Object> importFromUser(@RequestParam String[] ids,@RequestParam String departmentId){
        for(String id : ids){
            // 判断用户是否已在此部门
            UserDepartment res = userDepartmentService.findIsNullZwz(id, departmentId);
            if(res == null){
                UserDepartment ud = new UserDepartment();
                ud.setUserId(id).setDepartmentId(departmentId);
                userDepartmentService.save(ud);
                // 删除未挂靠信息
                UserDepartment defaultDepament = userDepartmentService.findIsNullZwz(id, "394196146");
                if(defaultDepament != null){
                    userDepartmentService.delete(defaultDepament.getId());
                }
                User user = userService.findByIdZwz(id);
                List<Department> departments = userDepartmentService.findDepartmentsByUserIdZwz(user.getId());
                DingtalkUtils.updateUser(userToDingtalkUser(user),DingtalkUtils.getDepartmentIdByDepartment(departments));
            }
        }
        return ResultUtil.success("批量导入用户成功");
    }



    @SystemLog(description = "查询组织架构", type = LogType.OPERATION)
    @RequestMapping(value = "/getByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取")
    public Result<List<Department>> getByParentId(@PathVariable String parentId,
                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<Department> list = new ArrayList<>();
//        User u = securityUtil.getCurrUser();
//        String key = "department::"+parentId+":"+u.getId()+"_"+openDataFilter;
//        String v = redisTemplate.opsForValue().get(key);
//        if(StrUtil.isNotBlank(v)){
//            list = new Gson().fromJson(v, new TypeToken<List<Department>>(){}.getType());
//            return new ResultUtil<List<Department>>().setData(list);
//        }
        list = departmentService.findByParentIdOrderBySortOrder(parentId, openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            list.get(i).setExpand(false);
        }
//        redisTemplate.opsForValue().set(key,
//                new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create().toJson(list), 15L, TimeUnit.DAYS);
        return new ResultUtil<List<Department>>().setData(list);
    }

    @RequestMapping(value = "/getByAllSubInTreeByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取所有子节点并展开")
    public Result<List<Department>> getByAllSubInTreeByParentId(@PathVariable String parentId,
                                                                @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){
        List<Department> list = new ArrayList<>();
        list = getByParentIdInTree(parentId, openDataFilter);
        Result<List<Department>> result=new ResultUtil<List<Department>>().setData(list);
        return result;
    }

    public List<Department> getByParentIdInTree(String parentId,Boolean openDataFilter) {
        List<Department> list = new ArrayList<>();
        list = departmentService.findByParentIdOrderBySortOrder(parentId,openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            Department department=list.get(i);
            department.setChildren(getByParentIdInTree(department.getId(),true));
        }
        return list;
    }


    @SystemLog(description = "新增组织架构", type = LogType.INSERT)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(Department department){

        String[] arrDepartmentName=department.getTitle().split(";");
        String[] ids = department.getTempIds().split(",");
        String shangJiBuMenId = department.getParentDepartmentId();

        for(int i=0;i<arrDepartmentName.length;i++) {
            department.setArchiveId(ids[i]);
            department.setTitle(arrDepartmentName[i]);
//            department.setId(GenerateUniqueIdUtil.getGuid());
            // 同步钉钉
            String id = DingtalkUtils.createDepartment(departmentToDingtalkDepartment(department));
            if(id == null ||id.equals("null")){
                return ResultUtil.error("钉钉系统繁忙!");
            }
            department.setId(id);
            Archive archive = iArchiveService.findById(ids[i]);
            if(archive != null){
                department.setDuty(archive.getDuty());
            }
            departmentService.save(department);
        }
        if (!CommonConstant.PARENT_ID.equals(department.getParentId())) {
            Department parent = departmentService.get(department.getParentId());
            if (parent.getIsParent() == null || !parent.getIsParent()) {
                parent.setIsParent(true);
                departmentService.update(parent);
            }
        }

        // 更新缓存
        Set<String> keys = redisTemplateHelper.keys("department::*");
        redisTemplate.delete(keys);
        return ResultUtil.success("添加成功");
    }



    public List<Department> setInfo(List<Department> list){

        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){
                Department parent = departmentService.get(item.getParentId());
                item.setParentTitle(parent.getTitle());
            }else{
                item.setParentTitle("一级部门");
            }
            // 设置负责人
            User mainHeader = departmentHeaderService.findMainHeaderNameZwz(item.getId());
            if(mainHeader != null){
                item.setMainHeaderId(mainHeader.getId());
                item.setMainHeaderName(mainHeader.getNickname());
            }
            User viceHeader = departmentHeaderService.findViceHeaderNameZwz(item.getId());
            if(viceHeader != null){
                item.setViceHeaderId(viceHeader.getId());
                item.setViceHeaderName(viceHeader.getNickname());
            }
        });
        return list;
    }
    @SystemLog(description = "模糊搜索组织架构", type = LogType.OPERATION)
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "部门名模糊搜索")
    public Result<List<Department>> searchByTitle(@RequestParam String title,
                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<Department> list = departmentService.findByTitleLikeOrderBySortOrder("%"+title+"%", openDataFilter);
        list = setInfo(list);
        return new ResultUtil<List<Department>>().setData(list);
    }

    @SystemLog(description = "修改组织架构", type = LogType.INSERT)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(Department department){

        Department old = departmentService.get(department.getId());
        String oldTitle = old.getTitle();
        Department d = departmentService.update(department);
        // 部门负责人
        departmentHeaderService.deleteByDepartmentId(department.getId());
        if(department.getMainHeaderId() != null && !department.getMainHeaderId().equals("")){
            DepartmentHeader dh = new DepartmentHeader().setUserId(department.getMainHeaderId()).setDepartmentId(d.getId())
                    .setType(CommonConstant.HEADER_TYPE_MAIN);
            departmentHeaderService.save(dh);
        }
        if(department.getViceHeaderId() != null && !department.getViceHeaderId().equals("")){
            DepartmentHeader dh = new DepartmentHeader().setUserId(department.getViceHeaderId()).setDepartmentId(d.getId())
                    .setType(CommonConstant.HEADER_TYPE_VICE);
            departmentHeaderService.save(dh);
        }
        editDepartmentArchive(department);
        // 同步更新钉钉
        DingtalkUtils.updateDepartment(departmentToDingtalkDepartment(department));
        // 若修改了部门名称
        if(!old.getTitle().equals(department.getTitle())){
            userService.updateDepartmentTitle(department.getId(), department.getTitle());
            // 删除所有用户缓存
            Set<String> keysUser = redisTemplateHelper.keys("user:" + "*");
            redisTemplate.delete(keysUser);
        }
        // 手动删除所有部门缓存
        Set<String> keys = redisTemplateHelper.keys("department:" + "*");
        redisTemplate.delete(keys);
        return ResultUtil.success("编辑成功");
    }

    private void editDepartmentArchive(Department department){
        QueryWrapper<Archive> qw = new QueryWrapper<Archive>();
        if(StrUtil.isNotBlank(department.getId())) {
            qw.like("id", department.getArchiveId());
        }
        List<Archive> res = iArchiveService.list(qw);
        if(!res.isEmpty()){
            Archive resultArchive = res.get(0);
            if(resultArchive != null){
                resultArchive.setTitle(department.getTitle());
                resultArchive.setDuty(department.getDuty());
                iArchiveService.saveOrUpdate(resultArchive);
            }
        }
    }

    private boolean hasUserInDepartment(String departmentId){
        return userDepartmentService.findByDepartmentIdZwz(departmentId).isEmpty();
    }

    @SystemLog(description = "删除组织架构", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@RequestParam String[] ids){

        for(String id : ids){
            // 未挂靠部门,禁止删除
            if(id.equals("394196146")) continue;
            // 判断部门是否有人
            if(!hasUserInDepartment(id)){
                return ResultUtil.error("请先删除部门下的员工!");
            }
            // 钉钉同步删除
            DingtalkUtils.deleteDepartment(id);
            deleteRecursion(id, ids);
        }
        // 手动删除所有部门缓存
        Set<String> keys = redisTemplateHelper.keys("department:" + "*");
        redisTemplate.delete(keys);
        // 删除数据权限缓存
        Set<String> keysUserRoleData = redisTemplateHelper.keys("userRole::depIds:" + "*");
        redisTemplate.delete(keysUserRoleData);
        return ResultUtil.success("批量通过id删除数据成功");
    }

    public void deleteRecursion(String id, String[] ids){

        List<User> list = userService.findByDepartmentId(id);
        if(list!=null&&list.size()>0){
            throw new XbootException("删除失败，包含正被用户使用关联的部门");
        }
        // 获得其父节点
        Department dep = departmentService.get(id);
        Department parent = null;
        if(dep!=null&&StrUtil.isNotBlank(dep.getParentId())){
            parent = departmentService.get(dep.getParentId());
        }
        departmentService.delete(id);
        // 删除关联数据权限
        roleDepartmentService.deleteByDepartmentId(id);
        // 删除关联部门负责人
        departmentHeaderService.deleteByDepartmentId(id);
        // 删除流程关联节点
        deleteMapper.deleteActNode(id);
        // 判断父节点是否还有子节点
        if(parent!=null){
            List<Department> childrenDeps = departmentService.findByParentIdOrderBySortOrder(parent.getId(), false);
            if(childrenDeps==null||childrenDeps.size()==0){
                parent.setIsParent(false);
                departmentService.update(parent);
            }
        }
        // 递归删除
        List<Department> departments = departmentService.findByParentIdOrderBySortOrder(id, false);
        for(Department d : departments){
            if(!CommonUtil.judgeIds(d.getId(), ids)){
                deleteRecursion(d.getId(), ids);
            }
        }
    }

    /**
     * OA部门转换成钉钉部门
     * @param department
     * @return
     */
    private DingtalkDepartment departmentToDingtalkDepartment(Department department){
        DingtalkDepartment dingtalkDepartment = new DingtalkDepartment();
        dingtalkDepartment.setId(department.getId());
        dingtalkDepartment.setName(department.getTitle());
        dingtalkDepartment.setParentid(department.getParentId());
        return dingtalkDepartment;
    }

    /**
     * OA用户转换成钉钉用户
     * @param user
     * @return
     */
    private DingtalkUser userToDingtalkUser(User user){
        DingtalkUser dingtalkUser = new DingtalkUser();
        dingtalkUser.setEmail(user.getEmail());
        dingtalkUser.setJobnumber(user.getUsername());
        dingtalkUser.setMobile(user.getMobile());
        dingtalkUser.setName(user.getNickname());
        dingtalkUser.setUserid(user.getId());
        List<Long> depertmentId = new ArrayList<>();
        List<String> ids = userDepartmentService.findDepartmentsIdByUserIdZwz(user.getId());
        for (String id : ids) {
            depertmentId.add(Long.parseLong(id));
        }
        dingtalkUser.setDepartment(depertmentId);
        return dingtalkUser;
    }
}
