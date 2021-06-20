package cn.exrick.xboot.modules.physics.entity;

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
@Table(name = "t_class_information")
@TableName("t_class_information")
@ApiModel(value = "班级信息")
public class ClassInformation extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "班级简称")
    private String classSimpleName;

    @ApiModelProperty(value = "班级全称")
    private String classAllName;

    @ApiModelProperty(value = "班主任")
    private String headMaster;

    @ApiModelProperty(value = "备注")
    private String remarks;
}