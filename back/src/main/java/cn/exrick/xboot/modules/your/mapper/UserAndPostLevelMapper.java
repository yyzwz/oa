package cn.exrick.xboot.modules.your.mapper;

import cn.exrick.xboot.modules.your.entity.PostLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.UserAndPostLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户岗级关系表数据处理层
 * @author 郑为中
 */
public interface UserAndPostLevelMapper extends BaseMapper<UserAndPostLevel> {
    int deleteAllByUserId(@Param("userId") String userId);

    List<PostLevel> selectByUserId(@Param("userId") String userId);
}