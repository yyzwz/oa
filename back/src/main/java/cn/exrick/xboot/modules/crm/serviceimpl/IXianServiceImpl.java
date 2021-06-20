package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.entity.Shi;
import cn.exrick.xboot.modules.crm.mapper.XianMapper;
import cn.exrick.xboot.modules.crm.entity.Xian;
import cn.exrick.xboot.modules.crm.service.IXianService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 县区定义接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IXianServiceImpl extends ServiceImpl<XianMapper, Xian> implements IXianService {

    @Autowired
    private XianMapper xianMapper;

    @Override
    public List<Xian> findShiByShengShi(String sheng, String city) {
        return xianMapper.findShiByShengShi(sheng,city);
    }
}