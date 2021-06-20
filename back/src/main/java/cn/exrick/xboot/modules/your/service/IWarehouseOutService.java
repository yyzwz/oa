package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.WarehouseOut;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库管理接口
 * @author 郑为中
 */
public interface IWarehouseOutService extends IService<WarehouseOut> {
    WarehouseOut findByIdZwz(String id);
}