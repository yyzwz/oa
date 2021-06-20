package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.WarehouseOutMapper;
import cn.exrick.xboot.modules.your.entity.WarehouseOut;
import cn.exrick.xboot.modules.your.service.IWarehouseOutService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 出库管理接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IWarehouseOutServiceImpl extends ServiceImpl<WarehouseOutMapper, WarehouseOut> implements IWarehouseOutService {

    @Autowired
    private WarehouseOutMapper warehouseOutMapper;

    @Override
    public WarehouseOut findByIdZwz(String id) {
        return warehouseOutMapper.findByIdZwz(id);
    }
}