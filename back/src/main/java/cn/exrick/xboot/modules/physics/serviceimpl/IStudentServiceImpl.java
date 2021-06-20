package cn.exrick.xboot.modules.physics.serviceimpl;

import cn.exrick.xboot.modules.physics.mapper.StudentMapper;
import cn.exrick.xboot.modules.physics.entity.Student;
import cn.exrick.xboot.modules.physics.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IStudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;
}