package cn.exrick.xboot.modules.your.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.PunchClock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考勤打卡数据处理层
 * @author 郑为中
 */
public interface PunchClockMapper extends BaseMapper<PunchClock> {
    List<PunchClock> findByUserIdAndDate(@Param("userId") String userId,@Param("date") String date);
}