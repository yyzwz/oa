package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.PinLeiMapper;
import cn.exrick.xboot.modules.crm.entity.PinLei;
import cn.exrick.xboot.modules.crm.service.IPinLeiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 品类基础档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IPinLeiServiceImpl extends ServiceImpl<PinLeiMapper, PinLei> implements IPinLeiService {

    @Autowired
    private PinLeiMapper pinLeiMapper;
}