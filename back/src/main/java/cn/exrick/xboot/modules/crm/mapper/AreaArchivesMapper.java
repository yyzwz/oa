package cn.exrick.xboot.modules.crm.mapper;

import cn.exrick.xboot.modules.crm.entity.Shi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 区域档案数据处理层
 * @author 郑为中
 */
public interface AreaArchivesMapper extends BaseMapper<AreaArchives> {
    List<String> findAllArea();

    List<AreaArchives> findAreaByBigArea(@Param("area") String area,@Param("company") String company);
}