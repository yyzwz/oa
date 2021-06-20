package cn.exrick.xboot.modules.base.serviceimpl;

import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.modules.base.dao.DepartmentDao;
import cn.exrick.xboot.modules.base.dao.UserDao;
import cn.exrick.xboot.modules.base.dao.UserDepartmentDao;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.entity.UserDepartment;
import cn.exrick.xboot.modules.base.service.UserDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserDepartmentServiceImpl implements UserDepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private UserDepartmentDao userDepartmentDao;

    @Autowired
    private UserDao userDao;

    @Override
    public UserDepartmentDao getRepository() {
        return userDepartmentDao;
    }


    @Override
    public UserDepartment findByIdZwz(String id) {
        return userDepartmentDao.findByIdZwz(id);
    }

    @Override
    public List<UserDepartment> findByUserIdZwz(String userId) {
        return userDepartmentDao.findByUserIdZwz(userId);
    }

    @Override
    public List<UserDepartment> findByDepartmentIdZwz(String departmentId) {
        return userDepartmentDao.findByDepartmentIdZwz(departmentId);
    }

    @Override
    public void deleteByUserId(String userId) {
        userDepartmentDao.deleteByUserId(userId);
    }

    @Override
    public List<Department> findDepartmentsByUserIdZwz(String userId) {
        return userDepartmentDao.findDepartmentsByUserIdZwz(userId);
    }

    @Override
    public List<String> findDepartmentsTitleByUserIdZwz(String userId) {
        return userDepartmentDao.findDepartmentsTitleByUserIdZwz(userId);
    }

    @Override
    public List<String> findDepartmentsIdByUserIdZwz(String userId) {
        return userDepartmentDao.findDepartmentsIdByUserIdZwz(userId);
    }

    @Override
    public UserDepartment findIsNullZwz(String userId, String departmentId) {
        return userDepartmentDao.findIsNullZwz(userId,departmentId);
    }
}
