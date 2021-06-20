package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.DianPuTongXinMapper;
import cn.exrick.xboot.modules.crm.entity.DianPuTongXin;
import cn.exrick.xboot.modules.crm.service.IDianPuTongXinService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 通信位置接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IDianPuTongXinServiceImpl extends ServiceImpl<DianPuTongXinMapper, DianPuTongXin> implements IDianPuTongXinService {

    @Autowired
    private DianPuTongXinMapper dianPuTongXinMapper;
}