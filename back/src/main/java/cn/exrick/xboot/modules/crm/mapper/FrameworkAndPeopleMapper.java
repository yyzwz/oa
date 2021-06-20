package cn.exrick.xboot.modules.crm.mapper;

import cn.exrick.xboot.modules.crm.entity.DianMian;
import cn.exrick.xboot.modules.crm.entity.Businessman;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.crm.entity.FrameworkAndPeople;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 经销商组织人员关系表数据处理层
 * @author 郑为中
 */
public interface FrameworkAndPeopleMapper extends BaseMapper<FrameworkAndPeople> {
    List<Businessman> findByLi(@Param("frameworkId") String frameworkId);

    int deleteJAD(@Param("frameworkId") String frameworkId,@Param("peopleId") String peopleId);

    List<Businessman> findNotImport(@Param("name") String name,@Param("frameworkId") String frameworkId);
}