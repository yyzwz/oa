package cn.exrick.xboot.modules.physics.serviceimpl;

import cn.exrick.xboot.modules.physics.mapper.ClassInformationMapper;
import cn.exrick.xboot.modules.physics.entity.ClassInformation;
import cn.exrick.xboot.modules.physics.service.IClassInformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 班级信息接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IClassInformationServiceImpl extends ServiceImpl<ClassInformationMapper, ClassInformation> implements IClassInformationService {

    @Autowired
    private ClassInformationMapper classInformationMapper;
}