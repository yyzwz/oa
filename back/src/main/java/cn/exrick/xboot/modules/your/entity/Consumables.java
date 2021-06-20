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
@Table(name = "t_consumables")
@TableName("t_consumables")
@ApiModel(value = "耗材管理")
public class Consumables extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "领用人")
    private String recipients;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "数量")
    private String number;

    @ApiModelProperty(value = "单价")
    private String unitPrice;

    @ApiModelProperty(value = "总价")
    private String totalPrice;

    @ApiModelProperty(value = "备注")
    private String remarks;
}