package cn.exrick.xboot.modules.physics.serviceimpl;

import cn.exrick.xboot.modules.physics.mapper.LaboratoryOrderMapper;
import cn.exrick.xboot.modules.physics.entity.LaboratoryOrder;
import cn.exrick.xboot.modules.physics.service.ILaboratoryOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 实验室预约接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class ILaboratoryOrderServiceImpl extends ServiceImpl<LaboratoryOrderMapper, LaboratoryOrder> implements ILaboratoryOrderService {

    @Autowired
    private LaboratoryOrderMapper laboratoryOrderMapper;
}