package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.RosterPostMapper;
import cn.exrick.xboot.modules.your.entity.RosterPost;
import cn.exrick.xboot.modules.your.service.IRosterPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 花名册岗位信息接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IRosterPostServiceImpl extends ServiceImpl<RosterPostMapper, RosterPost> implements IRosterPostService {

    @Autowired
    private RosterPostMapper rosterPostMapper;

    @Override
    public RosterPost findById(String id) {
        return rosterPostMapper.findById(id);
    }

    @Override
    public RosterPost findByRosterId(String rosterId) {
        return rosterPostMapper.findByRosterId(rosterId);
    }

    @Override
    public int deleteByRosterIdZwz(String rosterId) {
        return rosterPostMapper.deleteByRosterIdZwz(rosterId);
    }
}