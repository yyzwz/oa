package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.AnnualRentMapper;
import cn.exrick.xboot.modules.crm.entity.AnnualRent;
import cn.exrick.xboot.modules.crm.service.IAnnualRentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 年租金接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IAnnualRentServiceImpl extends ServiceImpl<AnnualRentMapper, AnnualRent> implements IAnnualRentService {

    @Autowired
    private AnnualRentMapper annualRentMapper;
}