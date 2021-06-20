package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.ShiChangAreaMapper;
import cn.exrick.xboot.modules.crm.entity.ShiChangArea;
import cn.exrick.xboot.modules.crm.service.IShiChangAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 市场区域种类档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IShiChangAreaServiceImpl extends ServiceImpl<ShiChangAreaMapper, ShiChangArea> implements IShiChangAreaService {

    @Autowired
    private ShiChangAreaMapper shiChangAreaMapper;
}