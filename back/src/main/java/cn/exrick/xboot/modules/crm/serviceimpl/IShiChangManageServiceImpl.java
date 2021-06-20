package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.ShiChangManageMapper;
import cn.exrick.xboot.modules.crm.entity.ShiChangManage;
import cn.exrick.xboot.modules.crm.service.IShiChangManageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 市场占有率管理档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IShiChangManageServiceImpl extends ServiceImpl<ShiChangManageMapper, ShiChangManage> implements IShiChangManageService {

    @Autowired
    private ShiChangManageMapper shiChangManageMapper;
}