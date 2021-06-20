package cn.exrick.xboot.modules.base.dao.mapper;

import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 郑为中
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Role> findByUserId(@Param("userId") String userId);

    /**
     * 通过用户id获取用户角色关联的部门数据
     * @param userId
     * @return
     */
    List<String> findDepIdsByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID找关系表
     * @param userId
     * @return
     */
    List<UserRole> findUserRoleByUserId(@Param("userId") String userId);

//    @Query("select r from UserRole ur,Role r where r.id = ur.roleId and ur.userId = ?1")
    List<Role> findByUserIdZwz(@Param("userId")String userId);
}
