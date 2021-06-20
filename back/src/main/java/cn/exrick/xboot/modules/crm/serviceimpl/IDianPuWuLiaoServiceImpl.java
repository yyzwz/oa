package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.DianPuWuLiaoMapper;
import cn.exrick.xboot.modules.crm.entity.DianPuWuLiao;
import cn.exrick.xboot.modules.crm.service.IDianPuWuLiaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺物料位置接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IDianPuWuLiaoServiceImpl extends ServiceImpl<DianPuWuLiaoMapper, DianPuWuLiao> implements IDianPuWuLiaoService {

    @Autowired
    private DianPuWuLiaoMapper dianPuWuLiaoMapper;
}