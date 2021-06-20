package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.SellAreaMapper;
import cn.exrick.xboot.modules.crm.entity.SellArea;
import cn.exrick.xboot.modules.crm.service.ISellAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售区域管理接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class ISellAreaServiceImpl extends ServiceImpl<SellAreaMapper, SellArea> implements ISellAreaService {

    @Autowired
    private SellAreaMapper sellAreaMapper;
}