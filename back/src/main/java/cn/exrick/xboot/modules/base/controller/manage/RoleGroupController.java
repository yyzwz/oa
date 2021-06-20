package cn.exrick.xboot.modules.base.controller.manage;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.exception.XbootException;
import cn.exrick.xboot.common.redis.RedisTemplateHelper;
import cn.exrick.xboot.common.utils.CommonUtil;
import cn.exrick.xboot.common.utils.HibernateProxyTypeAdapter;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.utils.SecurityUtil;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.entity.RoleGroup;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.service.RoleGroupService;
import cn.exrick.xboot.modules.base.service.RoleService;
import cn.exrick.xboot.modules.base.service.UserService;
import cn.hutool.core.util.StrUtil;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "角色组管理接口")
@RequestMapping("/xboot/roleGroup")
@CacheConfig(cacheNames = "roleGroup")
@Transactional
public class RoleGroupController {

    @Autowired
    private RoleGroupService roleGroupService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/getAllRoleGroupList", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部角色")
    public Result<Object> getAllRoleGroupList(){
        List<RoleGroup> list = roleGroupService.getAll();
        return ResultUtil.data(list);
    }

    @SystemLog(description = "查询角色组", type = LogType.OPERATION)
    @RequestMapping(value = "/getByAllSubInTreeByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取所有子节点并展开")
    public Result<List<RoleGroup>> getByAllSubInTreeByParentId(@PathVariable String parentId,
                                                                @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){
        List<RoleGroup> list = new ArrayList<>();
        list = getByParentIdInTree(parentId, openDataFilter);
        Result<List<RoleGroup>> result=new ResultUtil<List<RoleGroup>>().setData(list);
        return result;
    }

    public List<RoleGroup> getByParentIdInTree(String parentId,Boolean openDataFilter) {
        List<RoleGroup> list = new ArrayList<>();
        list = roleGroupService.findByParentIdOrderBySortOrder(parentId,openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            RoleGroup roleGroup=list.get(i);
            roleGroup.setChildren(getByParentIdInTree(roleGroup.getId(),true));
        }
        return list;
    }

    @RequestMapping(value = "/getByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取")
    public Result<List<RoleGroup>> getByParentId(@PathVariable String parentId,
                                                 @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<RoleGroup> list = new ArrayList<>();
        User u = securityUtil.getCurrUser();
        String key = "roleGroup::"+parentId+":"+u.getId()+"_"+openDataFilter;
        String v = redisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(v)){
            list = new Gson().fromJson(v, new TypeToken<List<RoleGroup>>(){}.getType());
            for(int i=0;i<list.size();i++){
                list.get(i).setExpand(false);
            }
            return new ResultUtil<List<RoleGroup>>().setData(list);
        }
        list = roleGroupService.findByParentIdOrderBySortOrder(parentId, openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            list.get(i).setExpand(false);
        }
        redisTemplate.opsForValue().set(key,
                new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create().toJson(list), 15L, TimeUnit.DAYS);
        return new ResultUtil<List<RoleGroup>>().setData(list);
    }

    @SystemLog(description = "新增角色组", type = LogType.OPERATION)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(RoleGroup roleGroup){

        RoleGroup d = roleGroupService.save(roleGroup);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if(!CommonConstant.PARENT_ID.equals(roleGroup.getParentId())){
            RoleGroup parent = roleGroupService.get(roleGroup.getParentId());
            if(parent.getIsParent()==null||!parent.getIsParent()){
                parent.setIsParent(true);
                roleGroupService.update(parent);
            }
        }
        // 更新缓存
        Set<String> keys = redisTemplateHelper.keys("roleGroup::*");
        redisTemplate.delete(keys);
        return ResultUtil.success("添加成功");
    }

    @SystemLog(description = "修改角色组", type = LogType.OPERATION)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(RoleGroup roleGroup,
                               @RequestParam(required = false) String[] mainHeader,
                               @RequestParam(required = false) String[] viceHeader){

        RoleGroup old = roleGroupService.get(roleGroup.getId());
        RoleGroup d = roleGroupService.update(roleGroup);
        // 先删除原数据

        // 若修改了角色组名称
        if(!old.getTitle().equals(roleGroup.getTitle())){
            // 删除所有用户缓存
            Set<String> keysUser = redisTemplateHelper.keys("roleGroup:" + "*");
            redisTemplate.delete(keysUser);
        }
        // 手动删除所有角色组缓存
        Set<String> keys = redisTemplateHelper.keys("roleGroup:" + "*");
        redisTemplate.delete(keys);
        return ResultUtil.success("编辑成功");
    }

    @SystemLog(description = "删除角色组", type = LogType.OPERATION)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for (String id : ids){
            RoleGroup roleGroup = roleGroupService.get(id);
            List<Role> arrRole=roleService.findAllRoleByRoleGroup(roleGroup.getId());
            List<RoleGroup>  subRoleGroup=roleGroupService.findByParentIdOrderBySortOrder(roleGroup.getId(),true);
            if(subRoleGroup.size()!=0)
                return ResultUtil.error(roleGroup.getTitle()+"有子角色组，不能删除！");
            if(arrRole.size()!=0)
                return ResultUtil.error(roleGroup.getTitle()+"有定义角色，不能删除！");
            roleGroupService.delete(id);
        }
        return ResultUtil.success("删除成功");
    }

    @SystemLog(description = "模糊搜索角色组", type = LogType.OPERATION)
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "角色组名模糊搜索")
    public Result<List<RoleGroup>> searchByTitle(@RequestParam String title,
                                                 @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<RoleGroup> list = roleGroupService.findByTitleLikeOrderBySortOrder("%"+title+"%", openDataFilter);
        list = setInfo(list);
        return new ResultUtil<List<RoleGroup>>().setData(list);
    }

    public List<RoleGroup> setInfo(List<RoleGroup> list){

        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){
                RoleGroup parent = roleGroupService.get(item.getParentId());
                item.setParentTitle(parent.getTitle());
            }else{
                item.setParentTitle("一级角色组");
            }

        });
        return list;
    }
}
