package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.DianPuXingXiangMapper;
import cn.exrick.xboot.modules.crm.entity.DianPuXingXiang;
import cn.exrick.xboot.modules.crm.service.IDianPuXingXiangService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 店面形象管理接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IDianPuXingXiangServiceImpl extends ServiceImpl<DianPuXingXiangMapper, DianPuXingXiang> implements IDianPuXingXiangService {

    @Autowired
    private DianPuXingXiangMapper dianPuXingXiangMapper;
}