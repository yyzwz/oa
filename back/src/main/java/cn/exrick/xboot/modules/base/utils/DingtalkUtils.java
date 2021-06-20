package cn.exrick.xboot.modules.base.utils;

import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.dingtalk.DingtalkDepartment;
import cn.exrick.xboot.modules.base.entity.dingtalk.DingtalkUser;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class DingtalkUtils {

    /**
     * 创建新部门
     * 返回部门ID,失败返回null
     * @param dingtalkDepartment
     * @return
     */
    public static String createDepartment(DingtalkDepartment dingtalkDepartment){
        String token = DingtalkUtils.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/create");
            OapiDepartmentCreateRequest req = new OapiDepartmentCreateRequest();
            req.setParentid(dingtalkDepartment.getParentid().equals("0")?"1":dingtalkDepartment.getParentid());
            req.setOrder(dingtalkDepartment.getOrder());
            req.setName(dingtalkDepartment.getName());
//            req.setId(dingtalkDepartment.getId());
            OapiDepartmentCreateResponse rsp = client.execute(req, token);
            if(JSON.parseObject(rsp.getBody()).getString("errmsg").equals("ok")){
                return JSON.parseObject(rsp.getBody()).getString("id");
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改部门信息
     * 返回部门ID,失败返回null
     * @param dingtalkDepartment
     * @return
     */
    public static String updateDepartment(DingtalkDepartment dingtalkDepartment){
        String token = DingtalkUtils.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/update");
            OapiDepartmentUpdateRequest req = new OapiDepartmentUpdateRequest();
            req.setId(Long.parseLong(dingtalkDepartment.getId()));
            req.setOrder(dingtalkDepartment.getOrder());
            req.setParentid(dingtalkDepartment.getParentid().equals("0")?"1":dingtalkDepartment.getParentid());
            req.setName(dingtalkDepartment.getName());
            OapiDepartmentUpdateResponse rsp = client.execute(req, token);
            if(JSON.parseObject(rsp.getBody()).getString("errmsg").equals("ok")){
                return JSON.parseObject(rsp.getBody()).getString("id");
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除部门
     */
    public static boolean deleteDepartment(String departmentId){
        String token = DingtalkUtils.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/delete");
            OapiDepartmentDeleteRequest req = new OapiDepartmentDeleteRequest();
            req.setHttpMethod("GET");
            req.setId(departmentId);
            OapiDepartmentDeleteResponse rsp = client.execute(req, token);
            if(JSON.parseObject(rsp.getBody()).getString("errmsg").equals("ok")){
                return true;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询所有部门
     */
    public static List<DingtalkDepartment> selectDepartmentList(){
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
            List<DingtalkDepartment> departments = JSON.parseArray(jsonStr,DingtalkDepartment.class);
//            DingtalkUtils dingtalkUtils = JSON.parseObject(jsonStr, DingtalkUtils.class);
            return departments;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建用户
     * 返回用户ID,失败返回null
     */
    public static String createUser(DingtalkUser user){
        String token = DingtalkUtils.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/create");
            OapiUserCreateRequest req = new OapiUserCreateRequest();
//            req.setUserid(user.getUserid());
            req.setJobnumber(user.getJobnumber());
            req.setEmail(user.getEmail());
            req.setMobile(user.getMobile());
            req.setPosition(user.getPosition());
            req.setDepartment(JSON.toJSONString(user.getDepartment()));
            req.setName(user.getName());
            OapiUserCreateResponse rsp = client.execute(req, token);
            if(JSON.parseObject(rsp.getBody()).getString("errmsg").equals("ok")){
                return JSON.parseObject(rsp.getBody()).getString("userid");
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建用户
     * 返回用户ID,失败返回null
     */
    public static String createUser(DingtalkUser user,String departmentId){
        String token = DingtalkUtils.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/create");
            OapiUserCreateRequest req = new OapiUserCreateRequest();
//            req.setUserid(user.getUserid());
            req.setJobnumber(user.getJobnumber());
            req.setEmail(user.getEmail());
            req.setMobile(user.getMobile());
            req.setPosition(user.getPosition());
            List<Long> departments = new ArrayList<>();
            departments.add(Long.parseLong(departmentId));
            req.setDepartment(JSON.toJSONString(departments));
            req.setName(user.getName());
            OapiUserCreateResponse rsp = client.execute(req, token);
            if(JSON.parseObject(rsp.getBody()).getString("errmsg").equals("ok")){
                return JSON.parseObject(rsp.getBody()).getString("userid");
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新用户信息
     * 返回 0,失败返回null
     */
    public static String updateUser(DingtalkUser user,List<Long> departmentIds){
        String token = DingtalkUtils.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/update");
            OapiUserUpdateRequest req = new OapiUserUpdateRequest();
            req.setUserid(user.getUserid());
            req.setName(user.getName());
            req.setMobile(user.getMobile());
            req.setDepartment(departmentIds);
            req.setEmail(user.getEmail());
            req.setPosition(user.getPosition());
            req.setJobnumber(user.getJobnumber());
            OapiUserUpdateResponse rsp = client.execute(req, token);
            if(JSON.parseObject(rsp.getBody()).getString("errmsg").equals("ok")){
                return JSON.parseObject(rsp.getBody()).getString("errcode");
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 删除用户
     * @param userId
     * @return
     */
    public static boolean deleteUser(String userId){
        String token = DingtalkUtils.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/delete");
            OapiUserDeleteRequest req = new OapiUserDeleteRequest();
            req.setHttpMethod("GET");
            req.setUserid(userId);
            OapiUserDeleteResponse rsp = client.execute(req, token);
            if(JSON.parseObject(rsp.getBody()).getString("errmsg").equals("ok")){
                return true;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 查询部门下的所有用户
     */
    public List<DingtalkUser> selectUsersInDepartment(String departmentId){
        String token = DingtalkUtils.getToken();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/listbypage");
            OapiUserListbypageRequest req = new OapiUserListbypageRequest();
            req.setDepartmentId(Long.parseLong(departmentId));
            req.setOffset(0L);
            req.setSize(100L);
            req.setHttpMethod("GET");
            OapiUserListbypageResponse rsp = client.execute(req, token);
            JSONObject ans = JSONObject.parseObject(rsp.getBody());
            if(ans.getString("errmsg").equals("ok")){
                String jsonStr = ans.getString("userlist");
                return  JSON.parseArray(jsonStr,DingtalkUser.class);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 token
     * @return
     */
    public static String getToken(){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey("dingjjthxuaipcdqsq8u");
            req.setAppsecret("wofhi927RMf0AfBbAoadR9Sjo42rsLnHjLTDkwNIbIAywAZz-ljq5_Ih5IE51FlR");
            req.setHttpMethod("GET");
            OapiGettokenResponse rsp = client.execute(req);
            int index = rsp.getBody().indexOf("access_token");
            return rsp.getBody().substring(index+15,index+47);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Long> getDepartmentIdByDepartment(List<Department> departmentList){
        List<Long> result = new ArrayList<>();
        for (Department department : departmentList) {
            result.add(Long.parseLong(department.getId()));
        }
        return result;
    }
}
