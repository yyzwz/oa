package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.BusinessmanMapper;
import cn.exrick.xboot.modules.crm.entity.Businessman;
import cn.exrick.xboot.modules.crm.service.IBusinessmanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 经销商人员档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IBusinessmanServiceImpl extends ServiceImpl<BusinessmanMapper, Businessman> implements IBusinessmanService {

    @Autowired
    private BusinessmanMapper businessmanMapper;
}