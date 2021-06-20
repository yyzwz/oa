package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.ShangCallMapper;
import cn.exrick.xboot.modules.crm.entity.ShangCall;
import cn.exrick.xboot.modules.crm.service.IShangCallService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 客商联系人接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IShangCallServiceImpl extends ServiceImpl<ShangCallMapper, ShangCall> implements IShangCallService {

    @Autowired
    private ShangCallMapper shangCallMapper;
}