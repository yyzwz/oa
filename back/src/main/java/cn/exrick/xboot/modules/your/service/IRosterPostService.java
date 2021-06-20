package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.RosterPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 花名册岗位信息接口
 * @author 郑为中
 */
public interface IRosterPostService extends IService<RosterPost> {
    RosterPost findById(String id);
    RosterPost findByRosterId(String rosterId);
    int deleteByRosterIdZwz(String rosterId);
}