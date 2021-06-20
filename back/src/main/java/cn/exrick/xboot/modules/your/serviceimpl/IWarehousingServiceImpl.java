package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.WarehousingMapper;
import cn.exrick.xboot.modules.your.entity.Warehousing;
import cn.exrick.xboot.modules.your.service.IWarehousingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 入库管理接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IWarehousingServiceImpl extends ServiceImpl<WarehousingMapper, Warehousing> implements IWarehousingService {

    @Autowired
    private WarehousingMapper warehousingMapper;

    @Override
    public Warehousing findByIdZwz(String id) {
        return warehousingMapper.findByIdZwz(id);
    }
}