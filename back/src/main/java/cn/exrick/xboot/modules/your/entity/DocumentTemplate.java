package cn.exrick.xboot.modules.your.entity;

import cn.exrick.xboot.base.XbootBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 郑为中
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_document_template")
@TableName("t_document_template")
@ApiModel(value = "文档模板")
public class DocumentTemplate extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资料名称")
    private String dataName;

    @ApiModelProperty(value = "是否上传")
    private String isHave;

    @ApiModelProperty(value = "后缀名")
    private String hou;

    @ApiModelProperty(value = "下载次数")
    private String times;

}