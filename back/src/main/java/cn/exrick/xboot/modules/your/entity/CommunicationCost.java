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
@Table(name = "t_communication_cost")
@TableName("t_communication_cost")
@ApiModel(value = "通讯费")
public class CommunicationCost extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "月份")
    private String mouth;

    @ApiModelProperty(value = "套餐名称")
    private String degrees;

    @ApiModelProperty(value = "通讯费")
    private String cost;

    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;
}