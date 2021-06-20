package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.MonthlyRentMapper;
import cn.exrick.xboot.modules.crm.entity.MonthlyRent;
import cn.exrick.xboot.modules.crm.service.IMonthlyRentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 月租金接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IMonthlyRentServiceImpl extends ServiceImpl<MonthlyRentMapper, MonthlyRent> implements IMonthlyRentService {

    @Autowired
    private MonthlyRentMapper monthlyRentMapper;
}