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
@Table(name = "t_laboratory_information")
@TableName("t_laboratory_information")
@ApiModel(value = "实验室信息")
public class LaboratoryInformation extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "实验室编号")
    private String number;

    @ApiModelProperty(value = "实验室名称")
    private String name;

    @ApiModelProperty(value = "分类")
    private String type;

    @ApiModelProperty(value = "人容量")
    private String volume;

    @ApiModelProperty(value = "温度")
    private String temperature;

    @ApiModelProperty(value = "湿度")
    private String humidity;

    @ApiModelProperty(value = "甲醛浓度")
    private String formaldehyde;

    @ApiModelProperty(value = "二氧化碳浓度")
    private String carbonDioxide;
}