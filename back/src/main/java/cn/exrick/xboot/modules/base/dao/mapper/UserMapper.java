package cn.exrick.xboot.modules.base.dao.mapper;

import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {

    User findByIdZwz(@Param("id") String id);

    List<User> findAllZwz();

    List<User> findByNameZwz(@Param("name") String name);

    User findByMobileZwz(@Param("mobile") String mobile);

    List<User> findByDepartmentZwz(@Param("departmentId") String departmentId);

    List<User> findByDepartmentAndNameZwz(@Param("name") String name,@Param("departmentId") String departmentId);

    List<User> findByPostZwz(@Param("post") String post);

    User findByRosterIdZwz(@Param("rosterId") String rosterId);

    List<User> findNotImportUserData(@Param("postId") String postId);
}
