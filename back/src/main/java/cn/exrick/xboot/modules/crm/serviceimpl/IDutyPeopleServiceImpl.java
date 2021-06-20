package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.DutyPeopleMapper;
import cn.exrick.xboot.modules.crm.entity.DutyPeople;
import cn.exrick.xboot.modules.crm.service.IDutyPeopleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责人档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IDutyPeopleServiceImpl extends ServiceImpl<DutyPeopleMapper, DutyPeople> implements IDutyPeopleService {

    @Autowired
    private DutyPeopleMapper dutyPeopleMapper;
}