package cn.exrick.xboot.modules.your.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.DocumentTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文档模板数据处理层
 * @author 郑为中
 */
public interface DocumentTemplateMapper extends BaseMapper<DocumentTemplate> {
    DocumentTemplate findByIdZwz(@Param("id") String id);

    List<String> findAllHou();
}