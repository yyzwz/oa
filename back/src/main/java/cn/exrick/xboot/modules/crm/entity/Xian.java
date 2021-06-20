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
@Table(name = "t_xian")
@TableName("t_xian")
@ApiModel(value = "县区定义")
public class Xian extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编码")
    private String number;

    @ApiModelProperty(value = "省份")
    private String sheng;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "县区")
    private String title;

    @ApiModelProperty(value = "县级类型")
    private String countyType;

    @ApiModelProperty(value = "店面密度")
    private String miDu;

    @ApiModelProperty(value = "单店销量")
    private String sellNumber;

    @ApiModelProperty(value = "是否封存")
    private String ban;

    @ApiModelProperty(value = "备注")
    private String remark;
}