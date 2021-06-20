package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.MonthCostMapper;
import cn.exrick.xboot.modules.crm.entity.MonthCost;
import cn.exrick.xboot.modules.crm.service.IMonthCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员月费用接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IMonthCostServiceImpl extends ServiceImpl<MonthCostMapper, MonthCost> implements IMonthCostService {

    @Autowired
    private MonthCostMapper monthCostMapper;
}