package cn.exrick.xboot.modules.base.service;


import cn.exrick.xboot.base.XbootBaseService;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.UserDepartment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDepartmentService extends XbootBaseService<UserDepartment,String> {

    UserDepartment findByIdZwz(String id);
    /**
     * 通过userId查找
     * @param userId
     * @return
     */
    List<UserDepartment> findByUserIdZwz(String userId);
    /**
     * 通过departmentId查找
     * @param departmentId
     * @return
     */
    List<UserDepartment> findByDepartmentIdZwz(String departmentId);
    /**
     * 删除用户部门
     * @param userId
     */
    void deleteByUserId(String userId);

    /**
     * 查找指定ID用户的所有部门
     * @param userId
     * @return
     */
    List<Department> findDepartmentsByUserIdZwz(String userId);

    /**
     * 查找指定ID用户的所有部门名称
     * @param userId
     * @return
     */
    List<String> findDepartmentsTitleByUserIdZwz(String userId);

    /**
     * 查找指定ID用户的所有部门ID
     * @param userId
     * @return
     */
    List<String> findDepartmentsIdByUserIdZwz(String userId);

    /**
     * 查询指定用户是否在某部门
     * @param userId
     * @param departmentId
     * @return
     */
    UserDepartment findIsNullZwz(String userId,String departmentId);
}
