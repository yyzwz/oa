package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.AreaArchivesMapper;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.service.IAreaArchivesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IAreaArchivesServiceImpl extends ServiceImpl<AreaArchivesMapper, AreaArchives> implements IAreaArchivesService {

    @Autowired
    private AreaArchivesMapper areaArchivesMapper;

    @Override
    public List<String> findAllArea() {
        return areaArchivesMapper.findAllArea();
    }

    @Override
    public List<AreaArchives> findAreaByBigArea(String area, String company) {
        return areaArchivesMapper.findAreaByBigArea(area,company);
    }
}