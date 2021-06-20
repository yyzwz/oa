package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.ShengMapper;
import cn.exrick.xboot.modules.crm.entity.Sheng;
import cn.exrick.xboot.modules.crm.service.IShengService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 省份定义接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IShengServiceImpl extends ServiceImpl<ShengMapper, Sheng> implements IShengService {

    @Autowired
    private ShengMapper shengMapper;

    @Override
    public List<String> findAllSheng() {
        return shengMapper.findAllSheng();
    }

    @Override
    public List<Sheng> findShengByArea(String area) {
        return shengMapper.findShengByArea(area);
    }
}