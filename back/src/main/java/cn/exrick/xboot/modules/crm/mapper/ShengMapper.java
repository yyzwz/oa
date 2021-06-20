package cn.exrick.xboot.modules.crm.mapper;

import cn.exrick.xboot.modules.crm.entity.Shi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.crm.entity.Sheng;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 省份定义数据处理层
 * @author 郑为中
 */
public interface ShengMapper extends BaseMapper<Sheng> {
    List<String> findAllSheng();
    List<Sheng> findShengByArea(@Param("area") String area);
}