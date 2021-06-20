package cn.exrick.xboot.modules.crm.entity;

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
import java.util.List;

/**
 * @author 郑为中
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_dian_mian")
@TableName("t_dian_mian")
@ApiModel(value = "店面维护")
public class DianMian extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分公司")
    private String sonCompany;

    @ApiModelProperty(value = "大区")
    private String bigArea;

    @ApiModelProperty(value = "区域")
    private String area;

    @ApiModelProperty(value = "省份")
    private String sheng;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "县区")
    private String xian;

    @ApiModelProperty(value = "法人")
    private String faPeople;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "负责人")
    private String fuZe;

    @ApiModelProperty(value = "店名")
    private String dianName;

    @ApiModelProperty(value = "经理ID")
    private String manager;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "花名册搜索客销经理ID")
    private String pid;
}