package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.entity.Businessman;
import cn.exrick.xboot.modules.crm.mapper.FrameworkAndPeopleMapper;
import cn.exrick.xboot.modules.crm.entity.FrameworkAndPeople;
import cn.exrick.xboot.modules.crm.service.IFrameworkAndPeopleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 经销商组织人员关系表接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IFrameworkAndPeopleServiceImpl extends ServiceImpl<FrameworkAndPeopleMapper, FrameworkAndPeople> implements IFrameworkAndPeopleService {

    @Autowired
    private FrameworkAndPeopleMapper frameworkAndPeopleMapper;

    @Override
    public List<Businessman> findByLi(String frameworkId) {
        return frameworkAndPeopleMapper.findByLi(frameworkId);
    }

    @Override
    public int deleteJAD(String frameworkId, String peopleId) {
        return frameworkAndPeopleMapper.deleteJAD(frameworkId,peopleId);
    }

    @Override
    public List<Businessman> findNotImport(String name, String frameworkId) {
        return frameworkAndPeopleMapper.findNotImport("%"+name+"%",frameworkId);
    }
}