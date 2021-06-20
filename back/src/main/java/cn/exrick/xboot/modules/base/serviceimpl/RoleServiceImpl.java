package cn.exrick.xboot.modules.base.serviceimpl;

import cn.exrick.xboot.modules.base.dao.RoleDao;
import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public RoleDao getRepository() {
        return roleDao;
    }

    @Override
    public List<Role> findByDefaultRole(Boolean defaultRole) {
        return roleDao.findByDefaultRole(defaultRole);
    }

    @Override
    public Role findRoleByIdById(String id) {
        return roleDao.findRoleByIdById(id);
    }

    @Override
    public List<Role> findAllRoleByRoleGroup(String roleGroupId){
        return roleDao.findAllRoleByRoleGroup(roleGroupId);
    }
}
