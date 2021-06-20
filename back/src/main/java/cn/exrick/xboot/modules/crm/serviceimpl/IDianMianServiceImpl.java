package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.DianMianMapper;
import cn.exrick.xboot.modules.crm.entity.DianMian;
import cn.exrick.xboot.modules.crm.service.IDianMianService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 店面维护接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IDianMianServiceImpl extends ServiceImpl<DianMianMapper, DianMian> implements IDianMianService {

    @Autowired
    private DianMianMapper dianMianMapper;
}