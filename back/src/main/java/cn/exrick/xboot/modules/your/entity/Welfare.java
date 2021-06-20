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
@Table(name = "t_welfare")
@TableName("t_welfare")
@ApiModel(value = "岗级福利")
public class Welfare extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "基本工资")
    private String baseSalary;

    @ApiModelProperty(value = "全勤奖")
    private String fullReward;

    @ApiModelProperty(value = "餐费补贴")
    private String malAllowance;

    @ApiModelProperty(value = "交通补贴")
    private String transportationSubsidy;

    @ApiModelProperty(value = "通信补贴")
    private String communicationSubsidy;

    @ApiModelProperty(value = "生日奖金")
    private String birthdayBonus;
}