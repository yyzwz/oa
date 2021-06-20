package cn.exrick.xboot.modules.crm.mapper;

import cn.exrick.xboot.modules.crm.entity.Shi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.crm.entity.BigArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 大区定义数据处理层
 * @author 郑为中
 */
public interface BigAreaMapper extends BaseMapper<BigArea> {
    List<BigArea> findBigAreaBySon(@Param("company") String company);
}