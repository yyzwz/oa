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
@Table(name = "t_insurance")
@TableName("t_insurance")
@ApiModel(value = "保险管理")
public class Insurance extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;

    @ApiModelProperty(value = "保险类型")
    private String insuranceType;

    @ApiModelProperty(value = "公缴保险金额")
    private String insurancePublicCost;

    @ApiModelProperty(value = "自缴保险金额")
    private String insurancePrivateCost;

    @ApiModelProperty(value = "保险备注")
    private String insuranceRemarks;

}