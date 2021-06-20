package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.DocumentTemplateMapper;
import cn.exrick.xboot.modules.your.entity.DocumentTemplate;
import cn.exrick.xboot.modules.your.service.IDocumentTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档模板接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IDocumentTemplateServiceImpl extends ServiceImpl<DocumentTemplateMapper, DocumentTemplate> implements IDocumentTemplateService {

    @Autowired
    private DocumentTemplateMapper documentTemplateMapper;


    @Override
    public DocumentTemplate findByIdZwz(String id) {
        return documentTemplateMapper.findByIdZwz(id);
    }

    @Override
    public List<String> findAllHou() {
        return documentTemplateMapper.findAllHou();
    }
}