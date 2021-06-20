package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.OneMianJiMapper;
import cn.exrick.xboot.modules.crm.entity.OneMianJi;
import cn.exrick.xboot.modules.crm.service.IOneMianJiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 单品类面积接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IOneMianJiServiceImpl extends ServiceImpl<OneMianJiMapper, OneMianJi> implements IOneMianJiService {

    @Autowired
    private OneMianJiMapper oneMianJiMapper;
}