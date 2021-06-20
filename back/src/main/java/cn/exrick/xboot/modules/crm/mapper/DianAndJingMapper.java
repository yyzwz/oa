package cn.exrick.xboot.modules.crm.mapper;

import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.entity.DianMian;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.crm.entity.DianAndJing;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店面经理关系表数据处理层
 * @author 郑为中
 */
public interface DianAndJingMapper extends BaseMapper<DianAndJing> {
    List<DianMian> findByLi(@Param("liId") String liId);

    int deleteJAD(@Param("liId") String liId,@Param("dianId") String dianId);

    List<DianMian> findNotImport(@Param("name") String name,@Param("liId") String liId);
}