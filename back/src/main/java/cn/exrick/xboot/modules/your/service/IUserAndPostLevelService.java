package cn.exrick.xboot.modules.your.service;

import cn.exrick.xboot.modules.your.entity.PostLevel;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.UserAndPostLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户岗级关系表接口
 * @author 郑为中
 */
public interface IUserAndPostLevelService extends IService<UserAndPostLevel> {
    int deleteAllByUserId(String userId);

    List<PostLevel> selectByUserId(String userId);
}