package cn.exrick.xboot.modules.crm.mapper;

import cn.exrick.xboot.modules.crm.entity.Shi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.crm.entity.Xian;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 县区定义数据处理层
 * @author 郑为中
 */
public interface XianMapper extends BaseMapper<Xian> {
    List<Xian> findShiByShengShi(@Param("sheng") String sheng,@Param("city") String city);
}