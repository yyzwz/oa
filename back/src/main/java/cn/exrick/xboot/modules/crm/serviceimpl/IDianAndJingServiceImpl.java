package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.entity.DianMian;
import cn.exrick.xboot.modules.crm.mapper.DianAndJingMapper;
import cn.exrick.xboot.modules.crm.entity.DianAndJing;
import cn.exrick.xboot.modules.crm.service.IDianAndJingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 店面经理关系表接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IDianAndJingServiceImpl extends ServiceImpl<DianAndJingMapper, DianAndJing> implements IDianAndJingService {

    @Autowired
    private DianAndJingMapper dianAndJingMapper;

    @Override
    public List<DianMian> findByLi(String liId) {
        return dianAndJingMapper.findByLi(liId);
    }

    @Override
    public int deleteJAD(String liId, String dianId) {
        return dianAndJingMapper.deleteJAD(liId,dianId);
    }

    @Override
    public List<DianMian> findNotImport(String name,String liId) {
        return dianAndJingMapper.findNotImport("%" + name + "%",liId);
    }
}