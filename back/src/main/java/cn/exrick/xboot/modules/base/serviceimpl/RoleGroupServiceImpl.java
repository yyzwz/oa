package cn.exrick.xboot.modules.base.serviceimpl;

import cn.exrick.xboot.common.utils.SecurityUtil;
import cn.exrick.xboot.modules.base.dao.RoleGroupDao;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.RoleGroup;
import cn.exrick.xboot.modules.base.service.RoleGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门接口实现
 * @author 齐鸣鸣
 */
@Slf4j
@Service
@Transactional
public class RoleGroupServiceImpl implements RoleGroupService {

    @Autowired
    private RoleGroupDao roleGroupDao;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public RoleGroupDao getRepository() {
        return roleGroupDao;
    }

    @Override
    public RoleGroup findRoleGroupByIdById(String id) {
        return roleGroupDao.findRoleGroupByIdById(id);
    }

    @Override
    public List<RoleGroup> findByParentIdOrderBySortOrder(String parentId, Boolean openDataFilter) {

        // 数据权限
        List<String> roleGroupIds = securityUtil.getDeparmentIds();
        if(roleGroupIds!=null&&roleGroupIds.size()>0&&openDataFilter){
            return roleGroupDao.findByParentIdAndIdInOrderBySortOrder(parentId, roleGroupIds);
        }
        return roleGroupDao.findByParentIdOrderBySortOrder(parentId);
    }

    @Override
    public List<RoleGroup> findByParentIdAndStatusOrderBySortOrder(String parentId, Integer status) {

        return roleGroupDao.findByParentIdAndStatusOrderBySortOrder(parentId, status);
    }

    @Override
    public List<RoleGroup> findByTitleLikeOrderBySortOrder(String title, Boolean openDataFilter) {

        // 数据权限
        List<String> roleGroupIds = securityUtil.getDeparmentIds();
        if(roleGroupIds!=null&&roleGroupIds.size()>0&&openDataFilter){
            return roleGroupDao.findByTitleLikeAndIdInOrderBySortOrder(title, roleGroupIds);
        }
        return roleGroupDao.findByTitleLikeOrderBySortOrder(title);
    }
}