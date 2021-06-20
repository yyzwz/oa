package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.DocumentTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文档模板接口
 * @author 郑为中
 */
public interface IDocumentTemplateService extends IService<DocumentTemplate> {
    DocumentTemplate findByIdZwz(String id);

    List<String> findAllHou();
}