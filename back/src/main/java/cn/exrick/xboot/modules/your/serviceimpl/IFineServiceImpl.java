package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.FineMapper;
import cn.exrick.xboot.modules.your.entity.Fine;
import cn.exrick.xboot.modules.your.service.IFineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 罚款登记接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IFineServiceImpl extends ServiceImpl<FineMapper, Fine> implements IFineService {

    @Autowired
    private FineMapper fineMapper;
}