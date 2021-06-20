package cn.exrick.xboot.modules.your.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.Archive;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门档案数据处理层
 * @author 郑为中
 */
public interface ArchiveMapper extends BaseMapper<Archive> {
    Archive findById(@Param("id") String id);
}