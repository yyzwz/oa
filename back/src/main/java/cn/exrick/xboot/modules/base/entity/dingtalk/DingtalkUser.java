package cn.exrick.xboot.modules.base.entity.dingtalk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DingtalkUser {

    // 用户ID
    private String userid;

    // 工号
    private String jobnumber;

    // 邮箱
    private String email;

    // 职位
    private String position;

    // 手机号码
    private String mobile;

    // 姓名
    private String name;

    // 部门
    private List<Long> department = new ArrayList<>();
}
