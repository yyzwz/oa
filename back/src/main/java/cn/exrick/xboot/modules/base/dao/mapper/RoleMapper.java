package cn.exrick.xboot.modules.base.dao.mapper;

import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> findByRoleId(@Param("roleId") String roleId);

    List<Role> findByRoleGroupId(@Param("roleGroupId") String roleGroupId);

    List<Role> findNotInRoleGroup(@Param("roleGroupId") String roleGroupId);

    List<Role> findAllRoles();

    List<User> findNotImportUserByRoleId(@Param("roleId") String roleId);
}
