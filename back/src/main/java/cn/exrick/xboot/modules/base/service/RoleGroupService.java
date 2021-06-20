package cn.exrick.xboot.modules.base.service;

import cn.exrick.xboot.base.XbootBaseService;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.RoleGroup;

import java.util.List;

/**
 * 部门接口
 * @author qmm
 */
public interface RoleGroupService extends XbootBaseService<RoleGroup,String> {

    RoleGroup findRoleGroupByIdById(String id);
    /**
     * 通过父id获取 升序
     * @param parentId
     * @param openDataFilter 是否开启数据权限
     * @return
     */
    List<RoleGroup> findByParentIdOrderBySortOrder(String parentId, Boolean openDataFilter);

    /**
     * 通过父id和状态获取
     * @param parentId
     * @param status
     * @return
     */
    List<RoleGroup> findByParentIdAndStatusOrderBySortOrder(String parentId, Integer status);

    /**
     * 部门名模糊搜索 升序
     * @param title
     * @param openDataFilter 是否开启数据权限
     * @return
     */
    List<RoleGroup> findByTitleLikeOrderBySortOrder(String title, Boolean openDataFilter);
}