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
@Table(name = "t_roster_enclosure")
@TableName("t_roster_enclosure")
@ApiModel(value = "花名册附件")
public class RosterEnclosure extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "花名册ID")
    private String rosterId;

    @ApiModelProperty(value = "花名册姓名")
    private String rosterName;

    @ApiModelProperty(value = "资料名称")
    private String enclosureName;

    @ApiModelProperty(value = "是否上传")
    private String isUpload;

    @ApiModelProperty(value = "后缀名")
    private String suffixName;
}