package cn.exrick.xboot.modules.your.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.Welfare;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗级福利数据处理层
 * @author 郑为中
 */
public interface WelfareMapper extends BaseMapper<Welfare> {
    Welfare findByIdZwz(@Param("id") String id);

    Welfare findByRosterIdZwz(@Param("rosterId") String rosterId);
}