package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.PunchClock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考勤打卡接口
 * @author 郑为中
 */
public interface IPunchClockService extends IService<PunchClock> {
    List<PunchClock> findByUserIdAndDate(String userId,String date);
}