package cn.exrick.xboot.modules.crm.serviceimpl;

import cn.exrick.xboot.modules.crm.mapper.SonCompanyMapper;
import cn.exrick.xboot.modules.crm.entity.SonCompany;
import cn.exrick.xboot.modules.crm.service.ISonCompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 分公司定义接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class ISonCompanyServiceImpl extends ServiceImpl<SonCompanyMapper, SonCompany> implements ISonCompanyService {

    @Autowired
    private SonCompanyMapper sonCompanyMapper;
}