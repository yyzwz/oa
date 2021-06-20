package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.KeShangZiZhiMapper;
import cn.exrick.xboot.modules.crm.entity.KeShangZiZhi;
import cn.exrick.xboot.modules.crm.service.IKeShangZiZhiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 客商资质接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IKeShangZiZhiServiceImpl extends ServiceImpl<KeShangZiZhiMapper, KeShangZiZhi> implements IKeShangZiZhiService {

    @Autowired
    private KeShangZiZhiMapper keShangZiZhiMapper;
}