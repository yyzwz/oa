package cn.exrick.xboot.modules.base.entity.dingtalk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DingtalkDepartment {

    // 钉钉ID
    private String id;

    // 部门名称
    private String name;

    // 父部门ID
    private String parentid;

    // 在父部门的排序值
    private String order;
}
