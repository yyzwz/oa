package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.JingLiMapper;
import cn.exrick.xboot.modules.crm.entity.JingLi;
import cn.exrick.xboot.modules.crm.service.IJingLiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 经理档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IJingLiServiceImpl extends ServiceImpl<JingLiMapper, JingLi> implements IJingLiService {

    @Autowired
    private JingLiMapper jingLiMapper;
}