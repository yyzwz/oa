package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.KeHuRosterMapper;
import cn.exrick.xboot.modules.crm.entity.KeHuRoster;
import cn.exrick.xboot.modules.crm.service.IKeHuRosterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IKeHuRosterServiceImpl extends ServiceImpl<KeHuRosterMapper, KeHuRoster> implements IKeHuRosterService {

    @Autowired
    private KeHuRosterMapper keHuRosterMapper;
}