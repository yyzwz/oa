package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.entity.UserDepartment;
import cn.exrick.xboot.modules.base.entity.UserRole;
import cn.exrick.xboot.modules.base.entity.dingtalk.DingtalkUser;
import cn.exrick.xboot.modules.base.service.DepartmentHeaderService;
import cn.exrick.xboot.modules.base.service.UserDepartmentService;
import cn.exrick.xboot.modules.base.service.UserRoleService;
import cn.exrick.xboot.modules.base.service.UserService;
import cn.exrick.xboot.modules.base.service.mybatis.IUserService;
import cn.exrick.xboot.modules.base.utils.DingtalkUtils;
import cn.exrick.xboot.modules.your.entity.*;
import cn.exrick.xboot.modules.your.service.IRosterEnclosureService;
import cn.exrick.xboot.modules.your.service.IRosterPostService;
import cn.exrick.xboot.modules.your.service.IRosterService;
import cn.exrick.xboot.modules.your.service.IWelfareService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "员工花名册管理接口")
@RequestMapping("/xboot/roster")
@Transactional
public class RosterController {

    @Autowired
    private IRosterService iRosterService;

    @Autowired
    private IRosterPostService iRosterPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserDepartmentService userDepartmentService;

    @Autowired
    private IRosterEnclosureService iRosterEnclosureService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private DepartmentHeaderService departmentHeaderService;

    /**
     * 获取指定ID的花名册员工信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Roster> get(@RequestParam String id){

        Roster roster = iRosterService.getById(id);
        return new ResultUtil<Roster>().setData(roster);
    }

    /**
     * 获取所有花名册员工信息  不分页
     * @return
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Roster>> getAll(){

        List<Roster> list = iRosterService.list();
        return new ResultUtil<List<Roster>>().setData(list);
    }

    /**
     * 分页  获取所有花名册员工信息
     * @param roster
     * @param page
     * @return
     */
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Roster>> getByPage(@ModelAttribute Roster roster, @ModelAttribute PageVo page){
        QueryWrapper<Roster> qw = new QueryWrapper<Roster>();
        if(StrUtil.isNotBlank(roster.getUserName())) {
            qw.like("user_name", roster.getUserName());
        }
        IPage<Roster> data = iRosterService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Roster>>().setData(data);
    }

    /**
     * 分页 获取未导入到用户管理的花名册员工信息
     * @param roster
     * @param page
     * @return
     */
    @RequestMapping(value = "/getNotImportByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Roster>> getNotImportByPage(@ModelAttribute Roster roster, @ModelAttribute PageVo page){
        IPage<Roster> data = iRosterService.page(PageUtil.initMpPage(page));
        List<Roster> result = userService.findNotImportUserListByName(roster.getUserName());
        data.setRecords(result);
        data.setTotal(result.size());
        data.setSize(page.getPageSize());
        data.setPages(page.getPageNumber());
//        List<Roster> ans = new ArrayList<>();
//        for(int i = (page.getPageNumber()-1) * page.getPageSize() ; i < page.getPageSize() * page.getPageNumber() && i < result.size(); i ++){
//            ans.add(result.get(i));
//        }
//        data.setRecords(ans);
        return new ResultUtil<IPage<Roster>>().setData(data);
    }

    /**
     * 分页 获取未导入到指定部门的员工信息
     * @return
     */
    @RequestMapping(value = "/getNotImportUserByDepartmentByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<User>> getNotImportUserByDepartmentByPage(@RequestParam String name,
                                                                    @RequestParam String departmentId, @ModelAttribute PageVo page){
        IPage<User> data = iRosterService.page(PageUtil.initMpPage(page));
        List<User> result = userService.findNotImportUserByDepartmentAndName(departmentId,name);
        data.setRecords(result);
        data.setTotal(result.size());
        data.setSize(page.getPageSize());
        data.setPages(page.getPageNumber());
//        List<User> ans = new ArrayList<>();
//        for(int i = (page.getPageNumber()-1) * page.getPageSize() ; i < page.getPageSize() * page.getPageNumber() && i < result.size(); i ++){
//            ans.add(result.get(i));
//        }
//        data.setRecords(ans);
        return new ResultUtil<IPage<User>>().setData(data);
    }

    /**
     * 添加员工花名册
     * @param roster
     * @return
     */
    @SystemLog(description = "新增花名册员工", type = LogType.INSERT)
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "添加数据")
    public Result<Roster> insert(Roster roster) throws ParseException {
        iRosterService.saveOrUpdate(roster);
        RosterPost rp = new RosterPost();
        rp.setRosterId(roster.getId());
        rp.setRosterName(roster.getUserName());
        iRosterPostService.saveOrUpdate(rp);
        // 用户选择导入到用户
        if(roster.getStatus().equals("0")){
            importToUser(roster);
        }
        return new ResultUtil<Roster>().setData(roster);
    }

    /**
     * 更新员工花名册信息
     * @param roster
     * @return
     */
    @SystemLog(description = "花名册修改员工信息", type = LogType.INSERT)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "添加数据")
    public Result<Roster> update(Roster roster) throws ParseException {
        iRosterService.saveOrUpdate(roster);
        updateToUser(roster);
        return new ResultUtil<Roster>().setData(roster);
    }

    /**
     * 删除指定ID的花名册信息
     * @param id
     * @return
     */
    @SystemLog(description = "永久删除花名册人员", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String id){
        if(id.equals("1293722691228012544")){
            return ResultUtil.error("超级管理员无法删除");
        }
        User user = userService.findByRosterId(id);
        if(user != null){
            // 删除部门关系
            List<UserDepartment> uds = userDepartmentService.findByUserIdZwz(user.getId());
            for (UserDepartment ud : uds) {
                userDepartmentService.delete(ud);
            }
            // 删除权限关系
            List<UserRole> urs = userRoleService.findUserRoleByUserId(user.getId());
            for (UserRole ur : urs) {
                userRoleService.delete(ur);
            }
            // 删除关联岗位信息
            iRosterPostService.deleteByRosterIdZwz(id);
            // 删除附件
            List<RosterEnclosure> enclosures = iRosterEnclosureService.findByRosterId(id);
            for (RosterEnclosure enclosure : enclosures) {
                File file = new File("C:\\OA\\" + id + "." + enclosure.getSuffixName());
                if(file != null){
                    file.delete();
                }
                iRosterEnclosureService.removeById(enclosure.getId());
            }
            // 删除用户
            DingtalkUtils.deleteUser(user.getId());
            userService.delete(user);
        }
        // 最后删除花名册
        iRosterService.removeById(id);
        return ResultUtil.success("批量通过id删除数据成功");
    }

    /**
     * 检查指定手机是否存在
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/checkMobile", method = RequestMethod.GET)
    @ApiOperation(value = "批量通过id删除")
    public Result<Roster> checkMobile(@RequestParam String mobile){
        return new ResultUtil<Roster>().setData(iRosterService.findByMobile(mobile));
    }

    /**
     * 获取指定员工的自我介绍
     * @param id
     * @return
     */
    @RequestMapping(value = "/getIntroduce", method = RequestMethod.GET)
    @ApiOperation(value = "获取指定员工的自我介绍")
    public Result<Roster> getIntroduce(@RequestParam String id){
        return new ResultUtil<Roster>().setData(iRosterService.findById(id));
    }

    /**
     * 更新指定员工的自我介绍
     * @param id
     * @param introduce
     * @return
     */
    @RequestMapping(value = "/updateIntroduce", method = RequestMethod.GET)
    @ApiOperation(value = "更新指定员工的自我介绍")
    public Result<Roster> updateIntroduce(@RequestParam String id,@RequestParam String introduce){
        Roster roster = iRosterService.findById(id);
        if(roster != null){
            roster.setIntroduce(introduce);
            iRosterService.saveOrUpdate(roster);
        }
        return new ResultUtil<Roster>().setData(roster);
    }

    /**
     * 花名册辞退员工
     * @param id
     * @return
     */
    @SystemLog(description = "花名册辞退员工", type = LogType.OPERATION)
    @RequestMapping(value = "/tui", method = RequestMethod.POST)
    @ApiOperation(value = "花名册辞退员工")
    public Result<Object> tui(@RequestParam String id){
        User user = iUserService.findByRosterIdZwz(id);
        // 查询是否部门负责人
        List<Department> headerDepartment = departmentHeaderService.findDepartmentHeaderByUserIdZwz(user.getId());
        if(!headerDepartment.isEmpty()){
            String headerErrorMsg = "请先取消以下部门的负责人资格: ";
            for (Department department : headerDepartment) {
                headerErrorMsg += department.getTitle();
            }
            return ResultUtil.error(headerErrorMsg);
        }
        Roster z = iRosterService.findById(id);
        z.setStatus("-1");
        iRosterService.saveOrUpdate(z);

        user.setStatus(CommonConstant.USER_STATUS_LOCK);
        iUserService.saveOrUpdate(user);
        return ResultUtil.success("辞退成功");
    }

    /**
     * 花名册复职员工
     * @param id
     * @return
     */
    @SystemLog(description = "花名册复职员工", type = LogType.OPERATION)
    @RequestMapping(value = "/fu", method = RequestMethod.POST)
    @ApiOperation(value = "花名册复职员工")
    public Result<Object> fu(@RequestParam String id) throws ParseException {
        Roster z = iRosterService.findById(id);
        z.setStatus("0");
        iRosterService.saveOrUpdate(z);
        User resultUser = iUserService.findByRosterIdZwz(id);
        if(resultUser == null){
            User user = new User();
            user.setUsername(z.getJobNumber());
            user.setNickname(z.getUserName());
            user.setEmail(z.getEmail() == null ? z.getJobNumber() + "@zwz.com" : z.getEmail());
            user.setMobile(z.getMobile());
            user.setPassword(new BCryptPasswordEncoder().encode("123456"));
            user.setType(0);
            //user.setDepartmentId("[380137548]");
            user.setStatus(-1);
            user.setRosterId(z.getId());
            // 初始化岗级
            user.setPost("1294462401827180544");
            user.setPostName("未设置岗级");
            // 身份证读取 性别 生日 籍贯
            String idcard = z.getIdCard();
            if(idcard != null && idcard.length()>17){
                SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd");
                user.setBirth(new Date(format.parse(z.getBirthday()).getTime()));
                user.setSex((idcard.charAt(16) - '0') % 2 == 1 ?"男":"女");
                user.setAddress("[\"" + idcard.substring(0,2) + "0000\",\"" + idcard.substring(0,4) + "00\",\"" + idcard.substring(0,6) + "\"]");
            }
            try{
                userService.save(user);
                // 默认无权限
                UserRole ur = new UserRole();
                ur.setUserId(user.getId());
                ur.setRoleId("1293777172439371777");
                userRoleService.save(ur);
                // 加入未挂靠部门
                UserDepartment ud = new UserDepartment();
                ud.setDepartmentId("394196146");
                ud.setUserId(user.getId());
                userDepartmentService.save(ud);
            }
            catch (Exception e){
                return ResultUtil.error("存在已导入的用户，请检查姓名、手机号、邮箱");
            }
        }else{
            resultUser.setStatus(CommonConstant.USER_STATUS_NORMAL);
            iUserService.saveOrUpdate(resultUser);
        }

        return ResultUtil.success("复职成功");
    }

    @SystemLog(description = "花名册权限更改", type = LogType.OPERATION)
    @RequestMapping(value = "/savePermissionPreset", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Roster> savePermissionPreset(Roster roster){
        if(roster != null){
            User user = iUserService.findByRosterIdZwz(roster.getId());
            user.setPermissionPreset(roster.getPermissionPreset());
            iUserService.saveOrUpdate(user);
            Roster rosterUser = iRosterService.findById(roster.getId());
            rosterUser.setPermissionPreset(roster.getPermissionPreset());
            iRosterService.saveOrUpdate(roster);
        }
        return new ResultUtil<Roster>().setSuccessMsg("权限更改成功");
    }

    private boolean importToUser(Roster roster) throws ParseException {
        User user = new User();
        user.setUsername(roster.getJobNumber());
        user.setNickname(roster.getUserName());
        user.setEmail(roster.getJobNumber() + "@zwz.com");
        user.setMobile(roster.getMobile());
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        user.setType(0);
        user.setStatus(0);
        user.setRosterId(roster.getId());
        // 初始化岗级
        user.setPost("1294462401827180544");
        user.setPostName("未设置岗级");
        // 身份证读取 性别 生日 籍贯
        String idcard = roster.getIdCard();
        if(idcard != null && idcard.length()>17){
            SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd");
            user.setBirth(new Date(format.parse(roster.getBirthday()).getTime()));
            user.setSex((idcard.charAt(16) - '0') % 2 == 1 ?"男":"女");
            user.setAddress("[\"" + idcard.substring(0,2) + "0000\",\"" + idcard.substring(0,4) + "00\",\"" + idcard.substring(0,6) + "\"]");
        }
        String dingtalkId = DingtalkUtils.createUser(userToDingtalkUser(user),"394196146");
        user.setId(dingtalkId);
        userService.save(user);
        // 加入未挂靠部门
        UserDepartment ud = new UserDepartment();
        ud.setDepartmentId("394196146");
        ud.setUserId(user.getId());
        userDepartmentService.save(ud);
        // 赋予无权限
        UserRole ur = new UserRole();
        ur.setUserId(user.getId());
        ur.setRoleId("1293777172439371777");
        userRoleService.save(ur);
        // 同步钉钉

        return true;
    }

    /**
     * OA用户转换成钉钉用户
     * @param user
     * @return
     */
    private DingtalkUser userToDingtalkUser(User user){
        DingtalkUser dingtalkUser = new DingtalkUser();
        dingtalkUser.setEmail(user.getEmail());
        dingtalkUser.setJobnumber(user.getUsername());
        dingtalkUser.setMobile(user.getMobile());
        dingtalkUser.setName(user.getNickname());
        dingtalkUser.setUserid(user.getId());
        List<Long> depertmentId = new ArrayList<>();
        List<String> ids = userDepartmentService.findDepartmentsIdByUserIdZwz(user.getId());
        for (String id : ids) {
            depertmentId.add(Long.parseLong(id));
        }
        dingtalkUser.setDepartment(depertmentId);
        return dingtalkUser;
    }

    /**
     * 花名册更新,同步更新用户管理,钉钉信息
     * @param roster
     * @return
     */
    private boolean updateToUser(Roster roster) throws ParseException {
        User user = userService.findByRosterId(roster.getId());
        if(user != null){
            user.setNickname(roster.getUserName());
            user.setUsername(roster.getJobNumber());
            user.setEmail(roster.getEmail());
            user.setMobile(roster.getMobile());
            user.setSex(roster.getSex());
            String idcard = roster.getIdCard();
            if(idcard != null && idcard.length()>17){
                SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd");
                user.setBirth(new Date(format.parse(roster.getBirthday()).getTime()));
                // ["330000","330200","330281"]
                user.setAddress("[\"" + idcard.substring(0,2) + "0000\",\"" + idcard.substring(0,4) + "00\",\"" + idcard.substring(0,6) + "\"]");
            }
            userService.update(user);
            // 同步钉钉
            List<Department> departments = userDepartmentService.findDepartmentsByUserIdZwz(user.getId());
            DingtalkUtils.updateUser(userToDingtalkUser(user),DingtalkUtils.getDepartmentIdByDepartment(departments));
            return true;
        }
        return false;
    }
}
