package cn.exrick.xboot.modules.base.service;


import cn.exrick.xboot.base.XbootBaseService;
import cn.exrick.xboot.modules.base.entity.Role;

import java.util.List;

/**
 * 角色接口
 * @author 郑为中
 */
public interface RoleService extends XbootBaseService<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);

    Role findRoleByIdById(String id);

    /**
     * 获取角色组对应的角色
     * @param roleGroupId
     * @return
     */
    List<Role> findAllRoleByRoleGroup(String roleGroupId);
}
