package cn.exrick.xboot.modules.base.utils;

import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.entity.UserDepartment;
import cn.exrick.xboot.modules.base.entity.UserRole;
import cn.exrick.xboot.modules.base.entity.dingtalk.DingtalkDepartment;
import cn.exrick.xboot.modules.base.entity.dingtalk.DingtalkUser;
import cn.exrick.xboot.modules.base.service.DepartmentService;
import cn.exrick.xboot.modules.base.service.UserDepartmentService;
import cn.exrick.xboot.modules.base.service.mybatis.IUserRoleService;
import cn.exrick.xboot.modules.base.service.mybatis.IUserService;
import cn.exrick.xboot.modules.your.entity.Archive;
import cn.exrick.xboot.modules.your.entity.Pattern;
import cn.exrick.xboot.modules.your.entity.Roster;
import cn.exrick.xboot.modules.your.entity.UserAndPostLevel;
import cn.exrick.xboot.modules.your.service.IArchiveService;
import cn.exrick.xboot.modules.your.service.IRosterService;
import cn.exrick.xboot.modules.your.service.IUserAndPostLevelService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiUserListbypageRequest;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserListbypageResponse;
import com.taobao.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "钉钉一键同步")
@RequestMapping("/xboot/dingtalk")
@Transactional
public class DingtalkConnect {

    @Autowired
    private IArchiveService iArchiveService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private IRosterService iRosterService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private UserDepartmentService userDepartmentService;

    @Autowired
    private IUserAndPostLevelService iUserAndPostLevelService;

    /**
     * 一键同步钉钉部门用户
     *  1. 同步钉钉所有部门到OA部门档案和OA组织架构
     *  2. 同步部门员工到OA花名册和OA用户
     */
    @RequestMapping(value = "/connect", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Boolean> connectDingtalk(){
        List<DingtalkDepartment> departments = getDingtalkDepartmentList();
        for (DingtalkDepartment department : departments) {
            // 1. 同步钉钉所有部门到OA部门档案和OA组织架构
            saveDingtalkDepartmentToOaDepartment(department);
            List<DingtalkUser> users = getDingtalkUserByDepartmentId(department.getId());
            for (DingtalkUser user : users) {
                // 2. 同步部门员工到OA花名册和OA用户
                saveDingtalkUserToRoster(user,department.getId());
            }
        }
        return new ResultUtil<Boolean>().setData(true);
    }

    @RequestMapping(value = "/dfs", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Boolean> dfs(){
        dfsUser("0");
        return new ResultUtil<Boolean>().setData(true);
    }
    /**
     * 钉钉User保存花名册和用户
     * @param user
     * @return
     */
    private boolean saveDingtalkUserToRoster(DingtalkUser user,String departmentId){
        // 保存OA花名册
        Roster roster = new Roster();
        roster.setStatus("0");
        roster.setEmail(user.getJobnumber() + "@zwz.com");
        // roster.setEmail(user.getEmail());
        roster.setUserName(user.getName());
        roster.setJobNumber(user.getJobnumber());
        roster.setId(user.getUserid());
        roster.setMobile(user.getMobile());
        iRosterService.saveOrUpdate(roster);
        // 保存OA用户
        User oaUser = new User();
        oaUser.setId(user.getUserid());
        oaUser.setStatus(CommonConstant.USER_STATUS_NORMAL);
        oaUser.setUsername(user.getJobnumber());
        oaUser.setNickname(user.getName());
        oaUser.setEmail(user.getJobnumber() + "@zwz.com");
        // oaUser.setEmail(user.getEmail());
        iUserService.save(oaUser);
        // 保存用户部门
        UserDepartment ud = new UserDepartment();
        ud.setDepartmentId(departmentId);
        ud.setUserId(oaUser.getId());
        userDepartmentService.save(ud);
        // 设置新注册无权限
        UserRole ur = new UserRole();
        ur.setUserId(oaUser.getId());
        ur.setRoleId("1293777172439371777");
        iUserRoleService.save(ur);
        // 设置未挂靠岗级
        UserAndPostLevel uapl = new UserAndPostLevel();
        uapl.setUserId(oaUser.getId());
        uapl.setLevelId("1294462401827180544");
        iUserAndPostLevelService.save(uapl);
        return true;
    }

    /**
     * 根据部门ID 查询钉钉用户
     * @param departmentId
     * @return
     */
    public List<DingtalkUser> getDingtalkUserByDepartmentId(String departmentId){
        try{
            String token = DingtalkUtils.getToken();
            DingTalkClient client1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/listbypage");
            OapiUserListbypageRequest req1 = new OapiUserListbypageRequest();
            req1.setDepartmentId(Long.parseLong(departmentId));
            req1.setOffset(0L);
            req1.setSize(100L);
            req1.setHttpMethod("GET");
            OapiUserListbypageResponse rsp1 = client1.execute(req1, token);
            JSONObject ans1 = JSONObject.parseObject(rsp1.getBody());
            String jsonStr1 = ans1.getString("userlist");
            return JSON.parseArray(jsonStr1,DingtalkUser.class);
        }catch (ApiException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有钉钉部门
     * @return
     */
    public List<DingtalkDepartment> getDingtalkDepartmentList(){
        String token = DingtalkUtils.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
            OapiDepartmentListRequest req = new OapiDepartmentListRequest();
            req.setFetchChild(true);
            req.setId("1");
            req.setHttpMethod("GET");
            OapiDepartmentListResponse rsp = client.execute(req, token);
            JSONObject ans = JSONObject.parseObject(rsp.getBody());
            String jsonStr = ans.getString("department");
            return JSON.parseArray(jsonStr,DingtalkDepartment.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 钉钉部门保存到OA部门档案和组织架构
     * @param department
     * @return
     */
    private boolean saveDingtalkDepartmentToOaDepartment(DingtalkDepartment department) {
        Archive oaArchive = new Archive();
        oaArchive.setTitle(department.getName());
        oaArchive.setStatus(CommonConstant.STATUS_NORMAL);
        oaArchive.setDuty(department.getName());
        iArchiveService.save(oaArchive);
        Department oaDepartment = new Department();
        oaDepartment.setArchiveId(oaArchive.getId());
        oaDepartment.setTitle(department.getName());
        oaDepartment.setDuty(department.getName());
        // 钉钉默认顶级部门ID 1，OA默认为0
        oaDepartment.setParentId(department.getParentid().equals("1") ? "0" : department.getParentid());
        oaDepartment.setId(department.getId());
        return departmentService.save(oaDepartment) == null;
    }

    /**
     * 填补是否父部门
     * @param id
     */
    private void dfsUser(String id){
        List<Department> departments = departmentService.findByParentId(id);
        if(departments.isEmpty()){
            Department department = departmentService.findByIdZwz(id);
            department.setIsParent(false);
            departmentService.update(department);
        }else{
            if(!id.equals("0")){
                Department department = departmentService.findByIdZwz(id);
                department.setIsParent(true);
                departmentService.update(department);
            }
            for (Department chr : departments) {
                dfsUser(chr.getId());
            }
        }
    }
}
