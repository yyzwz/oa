package cn.exrick.xboot.modules.base.dao;

import cn.exrick.xboot.base.XbootBaseDao;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.entity.RoleGroup;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 部门数据处理层
 * @author 齐鸣鸣
 */
public interface RoleGroupDao extends XbootBaseDao<RoleGroup,String> {

    @Query("select r from RoleGroup r where r.id =?1")
    RoleGroup findRoleGroupByIdById(String id);

    /**
     * 通过父id获取 升序
     * @param parentId
     * @return
     */
    List<RoleGroup> findByParentIdOrderBySortOrder(String parentId);

    /**
     * 通过父id获取 升序 数据权限
     * @param parentId
     * @param roleGroupIds
     * @return
     */
    List<RoleGroup> findByParentIdAndIdInOrderBySortOrder(String parentId, List<String> roleGroupIds);

    /**
     * 通过父id和状态获取 升序
     * @param parentId
     * @param status
     * @return
     */
    List<RoleGroup> findByParentIdAndStatusOrderBySortOrder(String parentId, Integer status);

    /**
     * 部门名模糊搜索 升序
     * @param title
     * @return
     */
    List<RoleGroup> findByTitleLikeOrderBySortOrder(String title);

    /**
     * 部门名模糊搜索 升序 数据权限
     * @param title
     * @param departmentIds
     * @return
     */
    List<RoleGroup> findByTitleLikeAndIdInOrderBySortOrder(String title, List<String> departmentIds);
}