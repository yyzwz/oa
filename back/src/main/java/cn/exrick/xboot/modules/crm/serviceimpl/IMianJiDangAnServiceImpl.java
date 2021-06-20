package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.MianJiDangAnMapper;
import cn.exrick.xboot.modules.crm.entity.MianJiDangAn;
import cn.exrick.xboot.modules.crm.service.IMianJiDangAnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 面积档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IMianJiDangAnServiceImpl extends ServiceImpl<MianJiDangAnMapper, MianJiDangAn> implements IMianJiDangAnService {

    @Autowired
    private MianJiDangAnMapper mianJiDangAnMapper;
}