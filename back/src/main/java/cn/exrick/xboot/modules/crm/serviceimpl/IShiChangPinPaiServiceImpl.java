package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.ShiChangPinPaiMapper;
import cn.exrick.xboot.modules.crm.entity.ShiChangPinPai;
import cn.exrick.xboot.modules.crm.service.IShiChangPinPaiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 市场品牌档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IShiChangPinPaiServiceImpl extends ServiceImpl<ShiChangPinPaiMapper, ShiChangPinPai> implements IShiChangPinPaiService {

    @Autowired
    private ShiChangPinPaiMapper shiChangPinPaiMapper;
}