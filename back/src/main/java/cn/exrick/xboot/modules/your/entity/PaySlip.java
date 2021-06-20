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
@Table(name = "t_pay_slip")
@TableName("t_pay_slip")
@ApiModel(value = "工资条")
public class PaySlip extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工ID")
    private String userId;

    @ApiModelProperty(value = "员工姓名")
    private String userName;

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "月份")
    private String mouth;

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

    @ApiModelProperty(value = "总计")
    private String sum;
}