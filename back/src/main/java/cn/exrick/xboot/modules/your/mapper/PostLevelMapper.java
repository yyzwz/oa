package cn.exrick.xboot.modules.your.mapper;

import cn.exrick.xboot.modules.your.entity.Roster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.PostLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗级管理档案数据处理层
 * @author 郑为中
 */
public interface PostLevelMapper extends BaseMapper<PostLevel> {
    PostLevel findByIdZwz(@Param("id") String id);

    List<PostLevel> findListByParentIdZwz(@Param("parentId") String parentId);
}