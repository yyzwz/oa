package cn.exrick.xboot.modules.your.mapper;

import cn.exrick.xboot.modules.your.entity.AssetsType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.Warehousing;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库管理数据处理层
 * @author 郑为中
 */
public interface WarehousingMapper extends BaseMapper<Warehousing> {
    Warehousing findByIdZwz(@Param("id") String id);
}