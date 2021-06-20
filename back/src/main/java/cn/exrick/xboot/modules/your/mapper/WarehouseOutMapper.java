package cn.exrick.xboot.modules.your.mapper;

import cn.exrick.xboot.modules.your.entity.Warehousing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.WarehouseOut;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库管理数据处理层
 * @author 郑为中
 */
public interface WarehouseOutMapper extends BaseMapper<WarehouseOut> {
    WarehouseOut findByIdZwz(@Param("id") String id);
}