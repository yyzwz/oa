package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.RosterEnclosure;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 花名册附件接口
 * @author 郑为中
 */
public interface IRosterEnclosureService extends IService<RosterEnclosure> {
    RosterEnclosure findById(String id);

    List<RosterEnclosure> findByRosterId(String rosterId);
}