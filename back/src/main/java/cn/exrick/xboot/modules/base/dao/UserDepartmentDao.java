package cn.exrick.xboot.modules.base.dao;

import cn.exrick.xboot.base.XbootBaseDao;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.UserDepartment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDepartmentDao extends XbootBaseDao<UserDepartment,String> {

    @Query("select u from UserDepartment u where u.id =?1")
    UserDepartment findByIdZwz(String id);

    /**
     * 通过userId查找
     * @param userId
     * @return
     */
    @Query("select u from UserDepartment u where u.userId = ?1")
    List<UserDepartment> findByUserIdZwz(String userId);
    /**
     * 通过departmentId查找
     * @param departmentId
     * @return
     */
    @Query("select u from UserDepartment u where u.departmentId = ?1")
    List<UserDepartment> findByDepartmentIdZwz(String departmentId);
    /**
     * 删除用户部门
     * @param userId
     */
    @Modifying
    @Query("delete from UserDepartment u where u.userId = ?1")
    void deleteByUserId(String userId);

    /**
     * 查找指定ID用户的所有部门
     * @param userId
     * @return
     */
    @Query("select d from Department d where d.id in (select distinct ud.departmentId from UserDepartment ud where ud.userId = ?1)")
    List<Department> findDepartmentsByUserIdZwz(String userId);

    /**
     * 查找指定ID用户的所有部门名称
     * @param userId
     * @return
     */
    @Query("select d.title from Department d where d.id in (select distinct ud.departmentId from UserDepartment ud where ud.userId = ?1)")
    List<String> findDepartmentsTitleByUserIdZwz(String userId);

    /**
     * 查找指定ID用户的所有部门ID
     * @param userId
     * @return
     */
    @Query("select d.id from Department d where d.id in (select distinct ud.departmentId from UserDepartment ud where ud.userId = ?1)")
    List<String> findDepartmentsIdByUserIdZwz(String userId);

    /**
     * 查询指定用户是否在某部门
     * @param userId
     * @param departmentId
     * @return
     */
    @Query("select ud from UserDepartment ud where ud.userId = ?1 and ud.departmentId = ?2")
    UserDepartment findIsNullZwz(String userId,String departmentId);
}
