package cn.exrick.xboot.modules.base.service.mybatis;

import cn.exrick.xboot.modules.base.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IUserService extends IService<User> {

    User findByIdZwz(String id);

    List<User> findAllZwz();

    List<User> findByNameZwz(String name);

    User findByMobileZwz(String mobile);

    List<User> findByDepartmentZwz(String departmentId);

    List<User> findByDepartmentAndNameZwz(String name,String departmentId);

    List<User> findByPostZwz(String post);

    User findByRosterIdZwz(String rosterId);

    List<User> findNotImportUserData(String postId);
}