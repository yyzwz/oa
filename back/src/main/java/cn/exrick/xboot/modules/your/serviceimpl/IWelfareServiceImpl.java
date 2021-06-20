package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.WelfareMapper;
import cn.exrick.xboot.modules.your.entity.Welfare;
import cn.exrick.xboot.modules.your.service.IWelfareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 岗级福利接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IWelfareServiceImpl extends ServiceImpl<WelfareMapper, Welfare> implements IWelfareService {

    @Autowired
    private WelfareMapper welfareMapper;

    @Override
    public Welfare findByIdZwz(String id) {
        return welfareMapper.findByIdZwz(id);
    }

    @Override
    public Welfare findByRosterIdZwz(String rosterId) {
        return welfareMapper.findByRosterIdZwz(rosterId);
    }
}