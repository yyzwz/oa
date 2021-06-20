package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.PaySlipMapper;
import cn.exrick.xboot.modules.your.entity.PaySlip;
import cn.exrick.xboot.modules.your.service.IPaySlipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 工资条接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IPaySlipServiceImpl extends ServiceImpl<PaySlipMapper, PaySlip> implements IPaySlipService {

    @Autowired
    private PaySlipMapper paySlipMapper;

    @Override
    public PaySlip findByIdZwz(String id) {
        return paySlipMapper.findByIdZwz(id);
    }

    @Override
    public int deleteByYearAndMouth(String year, String mouth) {
        return paySlipMapper.deleteByYearAndMouth(year,mouth);
    }
}