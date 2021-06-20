package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.KeHuSellTypeMapper;
import cn.exrick.xboot.modules.crm.entity.KeHuSellType;
import cn.exrick.xboot.modules.crm.service.IKeHuSellTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户销售类型接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IKeHuSellTypeServiceImpl extends ServiceImpl<KeHuSellTypeMapper, KeHuSellType> implements IKeHuSellTypeService {

    @Autowired
    private KeHuSellTypeMapper keHuSellTypeMapper;
}