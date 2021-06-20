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
@Table(name = "t_pattern")
@TableName("t_pattern")
@ApiModel(value = "薪资模式")
public class Pattern extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模式名称")
    private String patternName;

    @ApiModelProperty(value = "底薪")
    private String baseSalary;

    @ApiModelProperty(value = "标准件工资")
    private String standardParts;

    @ApiModelProperty(value = "上限工资")
    private String upperLimit;

    @ApiModelProperty(value = "全勤奖")
    private String industrious;

    @ApiModelProperty(value = "高温费")
    private String highTemperature;

    @ApiModelProperty(value = "通讯补贴")
    private String communicationSubsidy;

    @ApiModelProperty(value = "交通补贴")
    private String transportationSubsidy;

    @ApiModelProperty(value = "备注")
    private String remarks;
}