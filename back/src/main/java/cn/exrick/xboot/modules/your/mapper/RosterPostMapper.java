package cn.exrick.xboot.modules.your.mapper;

import cn.exrick.xboot.modules.your.entity.Roster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.RosterPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 花名册岗位信息数据处理层
 * @author 郑为中
 */
public interface RosterPostMapper extends BaseMapper<RosterPost> {
    RosterPost findById(@Param("id") String id);
    RosterPost findByRosterId(@Param("rosterId") String rosterId);
    int deleteByRosterIdZwz(@Param("rosterId") String rosterId);
}