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
@Table(name = "t_roster_post")
@TableName("t_roster_post")
@ApiModel(value = "花名册岗位信息")
public class RosterPost extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联花名册ID")
    private String rosterId;

    @ApiModelProperty(value = "关联花名册姓名")
    private String rosterName;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "岗级ID")
    private String postLevelId;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "岗级名称")
    private String postLevelName;

    @ApiModelProperty(value = "合同日期")
    private String contractDate;

    @ApiModelProperty(value = "合同期限")
    private String contractPeriod;

    @ApiModelProperty(value = "合同到期")
    private String contractExpire;

    @ApiModelProperty(value = "入职时间")
    private String entryTime;

    @ApiModelProperty(value = "试用期")
    private String probationPeriod;

    @ApiModelProperty(value = "转正日期")
    private String becomeFormal;

    @ApiModelProperty(value = "社保")
    private String socialInsurance;

    @ApiModelProperty(value = "商保")
    private String commercialInsurance;

}