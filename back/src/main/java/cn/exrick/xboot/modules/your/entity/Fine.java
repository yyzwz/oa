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
@Table(name = "t_fine")
@TableName("t_fine")
@ApiModel(value = "罚款登记")
public class Fine extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "被罚款人ID")
    private String personId;

    @ApiModelProperty(value = "被罚款人")
    private String person;

    @ApiModelProperty(value = "罚款原因")
    private String reason;

    @ApiModelProperty(value = "罚款数额")
    private String amount;

    @ApiModelProperty(value = "罚款备注")
    private String remarks;
}