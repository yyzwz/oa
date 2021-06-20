package cn.exrick.xboot.modules.your.mapper;

import cn.exrick.xboot.modules.your.entity.Welfare;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.PaySlip;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工资条数据处理层
 * @author 郑为中
 */
public interface PaySlipMapper extends BaseMapper<PaySlip> {
    PaySlip findByIdZwz(@Param("id") String id);
    int deleteByYearAndMouth(@Param("year") String year,@Param("mouth") String mouth);
}