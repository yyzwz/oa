package cn.exrick.xboot.modules.base.dao;

import cn.exrick.xboot.base.XbootBaseDao;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.DepartmentHeader;
import cn.exrick.xboot.modules.base.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 部门负责人数据处理层
 * @author 郑为中
 */
public interface DepartmentHeaderDao extends XbootBaseDao<DepartmentHeader,String> {

    /**
     * 通过部门和负责人类型获取
     * @param departmentId
     * @param type
     * @return
     */
    List<DepartmentHeader> findByDepartmentIdAndType(String departmentId, Integer type);

    /**
     * 通过部门获取
     * @param departmentIds
     * @return
     */
    List<DepartmentHeader> findByDepartmentIdIn(List<String> departmentIds);

    /**
     * 通过部门id删除
     * @param departmentId
     */
    @Modifying
    @Query("delete from DepartmentHeader d where d.departmentId = ?1")
    void deleteByDepartmentId(String departmentId);

    /**
     * 通过userId删除
     * @param userId
     */
    @Modifying
    @Query("delete from DepartmentHeader d where d.userId = ?1")
    void deleteByUserId(String userId);

    @Query("select u from User u ,DepartmentHeader dh where u.id = dh.userId and dh.departmentId = ?1 and dh.type = 0")
    User findMainHeaderNameZwz(String departmentId);

    @Query("select u from User u ,DepartmentHeader dh where u.id = dh.userId and dh.departmentId = ?1 and dh.type = 1")
    User findViceHeaderNameZwz(String departmentId);

    @Query("select d from Department d where d.id in (select dh.departmentId from User u ,DepartmentHeader dh where u.id = dh.userId and dh.userId = ?1)")
    List<Department> findDepartmentHeaderByUserIdZwz(String userId);
}