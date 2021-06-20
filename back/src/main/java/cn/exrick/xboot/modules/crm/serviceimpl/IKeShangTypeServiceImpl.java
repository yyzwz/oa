package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.KeShangTypeMapper;
import cn.exrick.xboot.modules.crm.entity.KeShangType;
import cn.exrick.xboot.modules.crm.service.IKeShangTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 客商类型接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IKeShangTypeServiceImpl extends ServiceImpl<KeShangTypeMapper, KeShangType> implements IKeShangTypeService {

    @Autowired
    private KeShangTypeMapper keShangTypeMapper;
}