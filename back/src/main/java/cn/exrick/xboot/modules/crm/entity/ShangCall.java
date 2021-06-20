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
@Table(name = "t_shang_call")
@TableName("t_shang_call")
@ApiModel(value = "客商联系人")
public class ShangCall extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客商编号")
    private String shangId;

    @ApiModelProperty(value = "客商类型")
    private String shangType;

    @ApiModelProperty(value = "客商姓名")
    private String name;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "传真")
    private String chuanZhen;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "是否接受短信")
    private String canDuan;

    @ApiModelProperty(value = "是否封存")
    private String ban;

    @ApiModelProperty(value = "备注")
    private String remark;
}