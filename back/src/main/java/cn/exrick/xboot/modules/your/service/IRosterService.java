package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.Roster;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工花名册接口
 * @author 郑为中
 */
public interface IRosterService extends IService<Roster> {
    Roster findById(String id);

    Roster findByMobile(String mobile);
}