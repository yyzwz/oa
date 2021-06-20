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
@Table(name = "t_roster")
@TableName("t_roster")
@ApiModel(value = "员工花名册")
public class Roster extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限预设")
    private String permissionPreset;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "工号")
    private String jobNumber;

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    @ApiModelProperty(value = "身份证号码")
    private String idCard;

    @ApiModelProperty(value = "是否禁用 -1禁用 0正常")
    private String status;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "生日")
    private String birthday;

    @ApiModelProperty(value = "年龄")
    private String age;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;

    @ApiModelProperty(value = "政治面貌")
    private String face;

    @ApiModelProperty(value = "户口地址")
    private String address;

    @ApiModelProperty(value = "婚姻状态")
    private String love;

    @ApiModelProperty(value = "毕业高校")
    private String colleges;

    @ApiModelProperty(value = "毕业时间")
    private String graduationTime;

    @ApiModelProperty(value = "专业")
    private String major;

    @ApiModelProperty(value = "学历")
    private String education;

    @ApiModelProperty(value = "钉钉ID")
    private String dingCode;

    @ApiModelProperty(value = "自我介绍")
    private String introduce;
}