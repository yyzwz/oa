package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.PunchClockMapper;
import cn.exrick.xboot.modules.your.entity.PunchClock;
import cn.exrick.xboot.modules.your.service.IPunchClockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 考勤打卡接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IPunchClockServiceImpl extends ServiceImpl<PunchClockMapper, PunchClock> implements IPunchClockService {

    @Autowired
    private PunchClockMapper punchClockMapper;

    @Override
    public List<PunchClock> findByUserIdAndDate(String userId, String date) {
        return punchClockMapper.findByUserIdAndDate(userId,date);
    }
}