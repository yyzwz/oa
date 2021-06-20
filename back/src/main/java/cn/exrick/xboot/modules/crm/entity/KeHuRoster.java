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
@Table(name = "t_ke_hu_roster")
@TableName("t_ke_hu_roster")
@ApiModel(value = "客户档案")
public class KeHuRoster extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否封存")
    private String ban;

    @ApiModelProperty(value = "客户姓名")
    private String name;

    @ApiModelProperty(value = "客户分类")
    private String huType;

    @ApiModelProperty(value = "客商编码")
    private String shangNumber;

    @ApiModelProperty(value = "客商名称")
    private String shangName;

    @ApiModelProperty(value = "客户来源")
    private String huForm;

    @ApiModelProperty(value = "客商类型")
    private String shangForm;

    @ApiModelProperty(value = "信用额度")
    private String xinMoney;

    @ApiModelProperty(value = "客户等级")
    private String huLevel;

    @ApiModelProperty(value = "客户质量")
    private String huLiang;

    @ApiModelProperty(value = "分公司")
    private String fenSi;

    @ApiModelProperty(value = "大区")
    private String bigQu;

    @ApiModelProperty(value = "城市类型")
    private String cityType;

    @ApiModelProperty(value = "城市等级")
    private String cityLevel;

    @ApiModelProperty(value = "县级类型")
    private String xianType;

    @ApiModelProperty(value = "店面密度")
    private String dianMi;

    @ApiModelProperty(value = "单店销量")
    private String sellNumber;

    @ApiModelProperty(value = "区域编码")
    private String areaNumber;

    @ApiModelProperty(value = "区域经理")
    private String areaManger;

    @ApiModelProperty(value = "业务员")
    private String yeWu;

    @ApiModelProperty(value = "客服")
    private String keFu;

    @ApiModelProperty(value = "省份")
    private String sheng;

    @ApiModelProperty(value = "地级市")
    private String shi;

    @ApiModelProperty(value = "联系人")
    private String callPeople;

    @ApiModelProperty(value = "市县镇区")
    private String peopleForm;

    @ApiModelProperty(value = "街道")
    private String jieDao;

    @ApiModelProperty(value = "建材市场")
    private String jianCai;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "开户行")
    private String kaiHang;

    @ApiModelProperty(value = "银行账户")
    private String yinHang;
}