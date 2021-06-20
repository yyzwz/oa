package cn.exrick.xboot.modules.crm.entity;

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
@Table(name = "t_shi")
@TableName("t_shi")
@ApiModel(value = "城市定义")
public class Shi extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "编码")
    private String number;

    @ApiModelProperty(value = "城市")
    private String title;

    @ApiModelProperty(value = "省份")
    private String sheng;

    @ApiModelProperty(value = "城市等级")
    private String cityLevel;

    @ApiModelProperty(value = "城市类型")
    private String cityType;

    @ApiModelProperty(value = "是否封存")
    private String ban;

    @ApiModelProperty(value = "备注")
    private String remark;
}