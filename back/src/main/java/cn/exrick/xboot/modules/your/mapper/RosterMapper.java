package cn.exrick.xboot.modules.your.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.Roster;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工花名册数据处理层
 * @author 郑为中
 */
public interface RosterMapper extends BaseMapper<Roster> {
    Roster findById(@Param("id") String id);

    Roster findByMobile(@Param("mobile") String mobile);
}