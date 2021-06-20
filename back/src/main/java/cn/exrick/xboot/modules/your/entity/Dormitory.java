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
@Table(name = "t_dormitory")
@TableName("t_dormitory")
@ApiModel(value = "宿舍管理")
public class Dormitory extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "宿舍门牌号")
    private String houseNumber;

    @ApiModelProperty(value = "宿舍地址")
    private String houseAddress;

    @ApiModelProperty(value = "房型")
    private String houseType;

    @ApiModelProperty(value = "配置")
    private String houseSetting;

    @ApiModelProperty(value = "面积")
    private String areaMeasure;

    @ApiModelProperty(value = "租金")
    private String rent;

    @ApiModelProperty(value = "备注")
    private String remarks;
}