package cn.exrick.xboot.modules.your.entity;

import cn.exrick.xboot.base.XbootBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author 郑为中
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_dormitory_lease")
@TableName("t_dormitory_lease")
@ApiModel(value = "宿舍租借")
public class DormitoryLease extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "宿舍ID")
    private String houseId;

    @ApiModelProperty(value = "宿舍门牌号")
    private String houseNumber;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "入住时间")
    private String checkTime;

    @ApiModelProperty(value = "租借状态")
    private String rentState;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @Transient
    @TableField(exist=false)
    private String selectStartDate;

    @Transient
    @TableField(exist=false)
    private String selectEndDate;
}