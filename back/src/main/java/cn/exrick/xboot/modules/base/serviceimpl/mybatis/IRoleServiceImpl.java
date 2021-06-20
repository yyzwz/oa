package cn.exrick.xboot.modules.base.serviceimpl.mybatis;

import cn.exrick.xboot.modules.base.dao.mapper.RoleMapper;
import cn.exrick.xboot.modules.base.dao.mapper.UserMapper;
import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.service.mybatis.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IRoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findByRoleId(String roleId) {
        return roleMapper.findByRoleId(roleId);
    }

    @Override
    public List<Role> findByRoleGroupId(String roleGroupId) {
        return roleMapper.findByRoleGroupId(roleGroupId);
    }

    @Override
    public List<Role> findNotInRoleGroup(String roleGroupId) {
        return roleMapper.findNotInRoleGroup(roleGroupId);
    }

    @Override
    public List<Role> findAllRoles() {
        return roleMapper.findAllRoles();
    }

    @Override
    public List<User> findNotImportUserByRoleId(String roleId) {
        return roleMapper.findNotImportUserByRoleId(roleId);
    }
}
