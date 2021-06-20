package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.CountyTypeMapper;
import cn.exrick.xboot.modules.crm.entity.CountyType;
import cn.exrick.xboot.modules.crm.service.ICountyTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 县级类型接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class ICountyTypeServiceImpl extends ServiceImpl<CountyTypeMapper, CountyType> implements ICountyTypeService {

    @Autowired
    private CountyTypeMapper countyTypeMapper;
}