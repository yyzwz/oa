package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.WaterCostMapper;
import cn.exrick.xboot.modules.your.entity.WaterCost;
import cn.exrick.xboot.modules.your.service.IWaterCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 水费管理接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IWaterCostServiceImpl extends ServiceImpl<WaterCostMapper, WaterCost> implements IWaterCostService {

    @Autowired
    private WaterCostMapper waterCostMapper;
}