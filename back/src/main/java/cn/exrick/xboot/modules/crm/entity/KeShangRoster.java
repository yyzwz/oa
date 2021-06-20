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
@Table(name = "t_ke_shang_roster")
@TableName("t_ke_shang_roster")
@ApiModel(value = "客商档案")
public class KeShangRoster extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客商编码")
    private String shangNumber;

    @ApiModelProperty(value = "客商名称")
    private String shangName;

    @ApiModelProperty(value = "客商类型")
    private String shangType;

    @ApiModelProperty(value = "区域")
    private String area;

    @ApiModelProperty(value = "省份")
    private String sheng;

    @ApiModelProperty(value = "地级市")
    private String city;

    @ApiModelProperty(value = "县")
    private String xian;

    @ApiModelProperty(value = "街道")
    private String jieDao;

    @ApiModelProperty(value = "具体地址")
    private String address;

    @ApiModelProperty(value = "开户行")
    private String openHang;

    @ApiModelProperty(value = "银行账户")
    private String hangNumber;

    @ApiModelProperty(value = "信用额度")
    private String xinMoney;

    @ApiModelProperty(value = "是否封存")
    private String ban;

    @ApiModelProperty(value = "备注")
    private String remark;
}