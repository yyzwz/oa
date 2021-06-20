package cn.exrick.xboot.modules.physics.serviceimpl;

import cn.exrick.xboot.modules.physics.mapper.LaboratoryInformationMapper;
import cn.exrick.xboot.modules.physics.entity.LaboratoryInformation;
import cn.exrick.xboot.modules.physics.service.ILaboratoryInformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 实验室信息接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class ILaboratoryInformationServiceImpl extends ServiceImpl<LaboratoryInformationMapper, LaboratoryInformation> implements ILaboratoryInformationService {

    @Autowired
    private LaboratoryInformationMapper laboratoryInformationMapper;
}