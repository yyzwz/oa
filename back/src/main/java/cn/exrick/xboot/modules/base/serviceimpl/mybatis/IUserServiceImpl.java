package cn.exrick.xboot.modules.base.serviceimpl.mybatis;

import cn.exrick.xboot.modules.base.dao.mapper.UserMapper;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.service.mybatis.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByIdZwz(String id) {
        return userMapper.findByIdZwz(id);
    }

    @Override
    public List<User> findAllZwz() {
        return userMapper.findAllZwz();
    }

    @Override
    public List<User> findByNameZwz(String name) {
        return userMapper.findByNameZwz(name);
    }

    @Override
    public User findByMobileZwz(String mobile) {
        return userMapper.findByMobileZwz(mobile);
    }

    @Override
    public List<User> findByDepartmentZwz(String departmentId) {
        return userMapper.findByDepartmentZwz(departmentId);
    }

    @Override
    public List<User> findByDepartmentAndNameZwz(String name, String departmentId) {
        return userMapper.findByDepartmentAndNameZwz(name,departmentId);
    }

    @Override
    public List<User> findByPostZwz(String post) {
        return userMapper.findByPostZwz(post);
    }

    @Override
    public User findByRosterIdZwz(String rosterId) {
        return userMapper.findByRosterIdZwz(rosterId);
    }

    @Override
    public List<User> findNotImportUserData(String postId) {
        return userMapper.findNotImportUserData(postId);
    }
}
