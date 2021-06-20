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
@Table(name = "t_laboratory_order")
@TableName("t_laboratory_order")
@ApiModel(value = "实验室预约")
public class LaboratoryOrder extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预约者ID")
    private String userId;

    @ApiModelProperty(value = "预约者姓名")
    private String userName;

    @ApiModelProperty(value = "实验室ID")
    private String laboratoryId;

    @ApiModelProperty(value = "实验室名称")
    private String laboratoryName;

    @ApiModelProperty(value = "预约时间")
    private String orderTime;
}