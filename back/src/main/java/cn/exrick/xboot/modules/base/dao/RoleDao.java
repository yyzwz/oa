package cn.exrick.xboot.modules.base.dao;

import cn.exrick.xboot.base.XbootBaseDao;
import cn.exrick.xboot.modules.base.entity.Role;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * 角色数据处理层
 * @author 郑为中
 */
public interface RoleDao extends XbootBaseDao<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);

    @Query("select r from Role r where r.id =?1")
    Role findRoleByIdById(String id);

    /**
     * 根据角色组Id找到所有角色
     * @param roleGroupId
     */
    @Query("select r from Role r where r.roleGroupId = ?1")
    List<Role> findAllRoleByRoleGroup(String roleGroupId);

}
