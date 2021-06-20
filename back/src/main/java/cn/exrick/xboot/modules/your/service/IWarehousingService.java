package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.Warehousing;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库管理接口
 * @author 郑为中
 */
public interface IWarehousingService extends IService<Warehousing> {
    Warehousing findByIdZwz(String id);
}