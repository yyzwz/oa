package cn.exrick.xboot.modules.base.service.mybatis;

import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author 郑为中
 */
@CacheConfig(cacheNames = "role")
public interface IRoleService extends IService<Role> {
    List<Role> findByRoleId(String roleId);

    List<Role> findByRoleGroupId(String roleGroupId);

    List<Role> findNotInRoleGroup(String roleGroupId);

    List<Role> findAllRoles();

    List<User> findNotImportUserByRoleId(String roleId);
}
