package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.BigAreaMapper;
import cn.exrick.xboot.modules.crm.entity.BigArea;
import cn.exrick.xboot.modules.crm.service.IBigAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 大区定义接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IBigAreaServiceImpl extends ServiceImpl<BigAreaMapper, BigArea> implements IBigAreaService {

    @Autowired
    private BigAreaMapper bigAreaMapper;

    @Override
    public List<BigArea> findBigAreaBySon(String company) {
        return bigAreaMapper.findBigAreaBySon(company);
    }
}