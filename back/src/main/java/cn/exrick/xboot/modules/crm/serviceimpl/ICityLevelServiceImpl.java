package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.CityLevelMapper;
import cn.exrick.xboot.modules.crm.entity.CityLevel;
import cn.exrick.xboot.modules.crm.service.ICityLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市级别接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class ICityLevelServiceImpl extends ServiceImpl<CityLevelMapper, CityLevel> implements ICityLevelService {

    @Autowired
    private CityLevelMapper cityLevelMapper;
}