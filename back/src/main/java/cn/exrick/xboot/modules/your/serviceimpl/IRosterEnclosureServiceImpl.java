package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.RosterEnclosureMapper;
import cn.exrick.xboot.modules.your.entity.RosterEnclosure;
import cn.exrick.xboot.modules.your.service.IRosterEnclosureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 花名册附件接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IRosterEnclosureServiceImpl extends ServiceImpl<RosterEnclosureMapper, RosterEnclosure> implements IRosterEnclosureService {

    @Autowired
    private RosterEnclosureMapper rosterEnclosureMapper;

    @Override
    public RosterEnclosure findById(String id) {
        return rosterEnclosureMapper.findById(id);
    }

    @Override
    public List<RosterEnclosure> findByRosterId(String rosterId) {
        return rosterEnclosureMapper.findByRosterId(rosterId);
    }
}