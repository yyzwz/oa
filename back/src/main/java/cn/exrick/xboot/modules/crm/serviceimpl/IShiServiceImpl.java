package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.ShiMapper;
import cn.exrick.xboot.modules.crm.entity.Shi;
import cn.exrick.xboot.modules.crm.service.IShiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市定义接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IShiServiceImpl extends ServiceImpl<ShiMapper, Shi> implements IShiService {

    @Autowired
    private ShiMapper shiMapper;

    @Override
    public List<Shi> findShiBySheng(String sheng) {
        return shiMapper.findShiBySheng(sheng);
    }
}