package cn.exrick.xboot.modules.base.entity;

import cn.exrick.xboot.base.XbootBaseEntity;
import cn.exrick.xboot.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郑为中
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_department")
@TableName("t_department")
@ApiModel(value = "部门")
public class Department extends XbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "档案ID")
    private String archiveId;

    @ApiModelProperty(value = "部门名称")
    private String title;

    @ApiModelProperty(value = "部门职责")
    private String duty;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "是否为父节点(含子节点) 默认false")
    private Boolean isParent = false;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Integer status = CommonConstant.STATUS_NORMAL;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "父节点名称")
    private String parentTitle;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "主负责人ID")
    private String mainHeaderId;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "主负责人姓名")
    private String mainHeaderName;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "主负责人ID")
    private String viceHeaderId;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "副负责人姓名")
    private String viceHeaderName;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "子部门")
    private List<Department> children=new ArrayList<Department>();

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "扩展")
    private boolean expand=true;

    @TableField(exist=false)
    @ApiModelProperty(value = "临时ID")
    private String tempIds;

    @TableField(exist=false)
    @ApiModelProperty(value = "上级部门ID，用于从部门档案导入到组织架构")
    private String parentDepartmentId;
}