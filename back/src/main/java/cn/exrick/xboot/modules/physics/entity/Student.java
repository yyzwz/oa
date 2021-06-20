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
@Table(name = "t_student")
@TableName("t_student")
@ApiModel(value = "学生")
public class Student extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "学号")
    private String number;

    @ApiModelProperty(value = "班级")
    private String className;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "专业")
    private String major;

    @ApiModelProperty(value = "备注")
    private String remarks;
}