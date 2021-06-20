package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.ConsumablesMapper;
import cn.exrick.xboot.modules.your.entity.Consumables;
import cn.exrick.xboot.modules.your.service.IConsumablesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 耗材管理接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IConsumablesServiceImpl extends ServiceImpl<ConsumablesMapper, Consumables> implements IConsumablesService {

    @Autowired
    private ConsumablesMapper consumablesMapper;
}