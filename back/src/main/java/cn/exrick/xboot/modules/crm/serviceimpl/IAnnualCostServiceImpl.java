package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.AnnualCostMapper;
import cn.exrick.xboot.modules.crm.entity.AnnualCost;
import cn.exrick.xboot.modules.crm.service.IAnnualCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员年费用接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IAnnualCostServiceImpl extends ServiceImpl<AnnualCostMapper, AnnualCost> implements IAnnualCostService {

    @Autowired
    private AnnualCostMapper annualCostMapper;
}