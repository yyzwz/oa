package cn.exrick.xboot.modules.base.dao;

import cn.exrick.xboot.base.XbootBaseDao;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.your.entity.Roster;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户数据处理层
 * @author 郑为中
 */
public interface UserDao extends XbootBaseDao<User,String> {

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 通过手机获取用户
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 通过邮件获取用户
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 通过部门id获取
     * @param departmentId
     * @return
     */
    List<User> findByDepartmentId(String departmentId);

    /**
     * 通过用户名模糊搜索
     * @param key
     * @param status
     * @return
     */
    @Query("select u from User u where u.username like %?1% or u.nickname like %?1% and u.status = ?2")
    List<User> findByUsernameLikeAndStatus(String key, Integer status);

    /**
     * 更新部门名称
     * @param departmentId
     * @param departmentTitle
     */
    @Modifying
    @Query("update User u set u.departmentTitle=?2 where u.departmentId=?1")
    void updateDepartmentTitle(String departmentId, String departmentTitle);

    @Query(value = "SELECT * FROM t_user u , t_user_role r WHERE u.id = r.user_id AND r.role_id = ?1",nativeQuery = true)
    List<User> getUserByRoles(String roleId);

    @Query("select u from User u where u.id=?1")
    User findByIdZwz(String id);

    // select * from t_roster where id not in(select t_roster.id from t_user,t_roster where t_user.roster_id = t_roster.id)
    @Query("select r from Roster r where r.id not in (select r.id from User u,Roster r where u.rosterId = r.id)")
    List<Roster> findNotImportUserList();

    @Query("select r from Roster r where r.userName like ?1 and r.id not in (select r.id from User u,Roster r where u.rosterId = r.id)")
    List<Roster> findNotImportUserListByName(String name);

    @Query("select u from  User u where u.rosterId = ?1")
    User findByRosterId(String id);

    @Query("select u from User u where u.nickname like ?1 and u.id in (select ud.id from UserDepartment ud where ud.departmentId=?2)")
    List<User> findByConditionZwz(String name,String departmentId);

    @Query("select u from User u where u.id not in(select distinct ud.userId from UserDepartment ud where ud.departmentId = ?1)")
    List<User> findNotImportUserByDepartment(String departmentId);

    @Query("select u from User u where u.nickname like ?2 and u.id not in(select distinct ud.userId from UserDepartment ud where ud.departmentId = ?1)")
    List<User> findNotImportUserByDepartmentAndName(String departmentId,String name);

    @Query(value = "select u from User u where u.id in(select ur.userId from UserRole ur where ur.roleId = ?1)")
    List<User> findByRolesId(String roleId);
}
