package cn.exrick.xboot.modules.your.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.RosterEnclosure;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 花名册附件数据处理层
 * @author 郑为中
 */
public interface RosterEnclosureMapper extends BaseMapper<RosterEnclosure> {
    RosterEnclosure findById(@Param("id") String id);

    List<RosterEnclosure> findByRosterId(@Param("rosterId") String rosterId);
}