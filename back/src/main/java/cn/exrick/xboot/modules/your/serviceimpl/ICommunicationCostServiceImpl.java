package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.CommunicationCostMapper;
import cn.exrick.xboot.modules.your.entity.CommunicationCost;
import cn.exrick.xboot.modules.your.service.ICommunicationCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯费接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class ICommunicationCostServiceImpl extends ServiceImpl<CommunicationCostMapper, CommunicationCost> implements ICommunicationCostService {

    @Autowired
    private CommunicationCostMapper communicationCostMapper;
}