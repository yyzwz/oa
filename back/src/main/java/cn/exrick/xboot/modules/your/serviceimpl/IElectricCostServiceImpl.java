package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.ElectricCostMapper;
import cn.exrick.xboot.modules.your.entity.ElectricCost;
import cn.exrick.xboot.modules.your.service.IElectricCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 电费管理接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IElectricCostServiceImpl extends ServiceImpl<ElectricCostMapper, ElectricCost> implements IElectricCostService {

    @Autowired
    private ElectricCostMapper electricCostMapper;
}