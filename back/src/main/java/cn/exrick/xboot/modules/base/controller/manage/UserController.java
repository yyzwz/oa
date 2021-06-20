package cn.exrick.xboot.modules.base.controller.manage;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.redis.RedisTemplateHelper;
import cn.exrick.xboot.common.utils.*;
import cn.exrick.xboot.config.security.SecurityUserDetails;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.exception.XbootException;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.common.vo.SearchVo;
import cn.exrick.xboot.modules.base.dao.mapper.DeleteMapper;
import cn.exrick.xboot.modules.base.entity.*;
import cn.exrick.xboot.modules.base.entity.dingtalk.DingtalkUser;
import cn.exrick.xboot.modules.base.service.*;
import cn.exrick.xboot.modules.base.service.mybatis.IUserRoleService;
import cn.exrick.xboot.modules.base.service.mybatis.IUserService;
import cn.exrick.xboot.modules.base.utils.CityUtil;
import cn.exrick.xboot.modules.base.utils.DingtalkUtils;
import cn.exrick.xboot.modules.base.vo.RoleDTO;
import cn.exrick.xboot.modules.your.entity.PostLevel;
import cn.exrick.xboot.modules.your.entity.Roster;
import cn.exrick.xboot.modules.your.service.IPostLevelService;
import cn.exrick.xboot.modules.your.service.IRosterService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import cn.exrick.xboot.modules.base.utils.RandomCode;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "用户接口")
@RequestMapping("/xboot/user")
@CacheConfig(cacheNames = "user")
@Transactional
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentHeaderService departmentHeaderService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private DeleteMapper deleteMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IRosterService iRosterService;

    @Autowired
    private UserDepartmentService userDepartmentService;

    @Autowired
    private IPostLevelService iPostLevelService;

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping(value = "/QCloudSmsLogin", method = RequestMethod.POST)
    @ApiOperation(value = "发送验证码")
    public Result<Object> QCloudSmsLogin(@RequestParam(value = "mobile") String mobile, HttpServletRequest request){
        User u = userService.findByMobile(mobile);
        if(u==null){
            throw new XbootException("手机号不存在");
        }
        HttpSession session = request.getSession();
        // 短信应用 SDK AppID
        int appid = 1400435511; // SDK AppID 以1400开头
        // 短信应用 SDK AppKey
        String appkey = "a7d937297f1ce4064c078624e1e13eb2";
        // 需要发送短信的手机号码
        String[] phoneNumbers = {""};
        phoneNumbers[0] =  mobile;
        // 短信模板 ID，需要在短信应用中申请
        int templateId = 740449; // NOTE: 这里的模板 ID`7839`只是示例，真实的模板 ID 需要在短信控制台中申请
        // 签名

        String smsSign = "IT微简历"; // NOTE: 签名参数使用的是`签名内容`，而不是`签名ID`。这里的签名"腾讯云"只是示例，真实的签名需要在短信控制台申请
        try {
            String randomCode = RandomCode.getRandomCode();
            String[] params = {randomCode,"5"};
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers[0],
                    templateId, params, smsSign, "", "");
            System.out.println("发送的验证码 = " + randomCode);
            System.out.println(result);
            session.setAttribute("tel"+mobile, randomCode);
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
        }

        return ResultUtil.success("批量通过id导入花名册数据成功");
    }
    @RequestMapping(value = "/QCloudSmsLoginIn", method = RequestMethod.POST)
    @ApiOperation(value = "手机验证码登入")
    public Result<Object> QCloudSmsLoginIn(@RequestParam(value = "mobile") String mobile, @RequestParam(value = "code") String code, HttpServletRequest request){
        HttpSession session = request.getSession();
        String codeAns = (String) session.getAttribute("tel" + mobile);
        if(codeAns.equals(code)){
            User u = userService.findByMobile(mobile);
            String accessToken = securityUtil.getToken(u.getUsername(), true);
            // 记录日志使用
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new SecurityUserDetails(u), null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResultUtil.data(accessToken);
        }
        return ResultUtil.data(false,"NO");
    }

    @RequestMapping(value = "/smsLogin", method = RequestMethod.POST)
    @SystemLog(description = "短信登录", type = LogType.LOGIN)
    @ApiOperation(value = "短信登录接口")
    public Result<Object> smsLogin(@RequestParam String mobile,
                                   @RequestParam(required = false) Boolean saveLogin){

        User u = userService.findByMobile(mobile);
        if(u==null){
            throw new XbootException("手机号不存在");
        }
        String accessToken = securityUtil.getToken(u.getUsername(), saveLogin);
        // 记录日志使用
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new SecurityUserDetails(u), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResultUtil.data(accessToken);
    }

    // 有某个权限的用户列表
    @SystemLog(description = "查询用户列表", type = LogType.OPERATION)
    @RequestMapping(value = "/getByCondition2", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<List<User>> getByCondition2(String roleid){

        List<User> users = userService.findByRolesId(roleid);
        return new ResultUtil<List<User>>().setData(users);
    }

    @RequestMapping(value = "/getNewJobNumber", method = RequestMethod.GET)
    @ApiOperation(value = "获取最新未注册工号")
    public Result<String> getNewJobNumber(){
        List<User> list = userService.findAllZwz();
        if(list.size() < 2) return new ResultUtil<String>().setData("00" + list.size());
        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User user, User user2) {
                if(user.getUsername().equals("admin")) return 1;
                if(user2.getUsername().equals("admin")) return 0;
                return Integer.parseInt(user.getUsername()) - Integer.parseInt(user2.getUsername());
            }
        });
        // 最后一位是admin
        int jobNumber = -1;
        if(list.get(list.size() - 1).getUsername().equals("admin")){
            jobNumber =Integer.parseInt(list.get(list.size() - 2).getUsername()) + 1;
        }else{
            jobNumber =Integer.parseInt(list.get(list.size() - 1).getUsername()) + 1;
        }

        String result = String.format("%03d", jobNumber);
        return new ResultUtil<String>().setData(result);
    }

    /**
     * 导入花名册的数据
     * @param ids
     * @return
     * @throws ParseException
     */
    // 事务 注解 否则try catch无法正常获取抛出的异常
    @SystemLog(description = "花名册导入用户", type = LogType.OPERATION)
    @Transactional(propagation = Propagation.SUPPORTS)
    @RequestMapping(value = "/importFromRoster", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id花名册导入")
    public Result<Object> importAllByIds(@RequestParam String[] ids) throws ParseException {
        Result<Object> a = ResultUtil.success("批量通过id导入花名册数据成功");
        for(String id : ids){
             Roster roster = iRosterService.findById(id);
            if(roster != null){
                User user = new User();
                user.setUsername(roster.getJobNumber());
                user.setNickname(roster.getUserName());
                user.setEmail(roster.getEmail() == null ? roster.getJobNumber() + "@zwz.com" : roster.getEmail());
                user.setMobile(roster.getMobile());
                user.setPassword(new BCryptPasswordEncoder().encode("123456"));
                user.setType(0);
                //user.setDepartmentId("[380137548]");
                user.setStatus(-1);
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
                try{
                    String dingtalkId = DingtalkUtils.createUser(userToDingtalkUser(user),"394196146");
                    user.setId(dingtalkId);
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
                    a = ResultUtil.error("存在已导入的用户，请检查姓名、手机号、邮箱");
                }
            }
        }
        return a;
    }



    @RequestMapping(value = "/resetByMobile", method = RequestMethod.POST)
    @ApiOperation(value = "通过短信重置密码")
    public Result<Object> resetByMobile(@RequestParam String mobile,
                                        @RequestParam String password,
                                        @RequestParam String passStrength){

        User u = userService.findByMobile(mobile);
        String encryptPass = new BCryptPasswordEncoder().encode(password);
        u.setPassword(encryptPass).setPassStrength(passStrength);
        userService.update(u);
        // 删除缓存
        redisTemplate.delete("user::"+u.getUsername());
        return ResultUtil.success("重置密码成功");
    }

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ApiOperation(value = "注册用户")
    public Result<Object> regist(@Valid User u){

        // 校验是否已存在
        checkUserInfo(u.getUsername(), u.getMobile(), u.getEmail());

        String encryptPass = new BCryptPasswordEncoder().encode(u.getPassword());
        u.setPassword(encryptPass).setType(CommonConstant.USER_TYPE_NORMAL);
        User user = userService.save(u);

        // 默认角色
        List<Role> roleList = roleService.findByDefaultRole(true);
        if(roleList!=null&&roleList.size()>0){
            for(Role role : roleList){
                UserRole ur = new UserRole().setUserId(user.getId()).setRoleId(role.getId());
                userRoleService.save(ur);
            }
        }
        return ResultUtil.data(user);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录用户接口")
    public Result<User> getUserInfo(){

        User u = securityUtil.getCurrUser();
        // 清除持久上下文环境 避免后面语句导致持久化
        entityManager.clear();
        u.setPassword(null);
        return new ResultUtil<User>().setData(u);
    }

    @RequestMapping(value = "/changeMobile", method = RequestMethod.POST)
    @ApiOperation(value = "修改绑定手机")
    public Result<Object> changeMobile(@RequestParam String mobile,String code,HttpServletRequest request){
        HttpSession session = request.getSession();
        String codeAns = (String) session.getAttribute("tel" + mobile);
        if(codeAns == null){
            return ResultUtil.error("请先获取验证码!");
        }
        if(codeAns.equals(code)){
            User u = securityUtil.getCurrUser();
            u.setMobile(mobile);
            userService.update(u);
            // 删除缓存
            redisTemplate.delete("user::"+u.getUsername());
            return ResultUtil.success("修改手机号成功!");
        }
        return ResultUtil.error("验证码错误!");
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    @ApiOperation(value = "解锁验证密码")
    public Result<Object> unLock(@RequestParam String password){

        User u = securityUtil.getCurrUser();
        if(!new BCryptPasswordEncoder().matches(password, u.getPassword())){
            return ResultUtil.error("密码不正确");
        }
        return ResultUtil.data(null);
    }

    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    @ApiOperation(value = "重置密码")
    public Result<Object> resetPass(@RequestParam String[] ids){

        for(String id:ids){
            User u = userService.get(id);
            // 在线DEMO所需
            if("test".equals(u.getUsername())||"test2".equals(u.getUsername())||"admin".equals(u.getUsername())){
                throw new XbootException("测试账号及管理员账号不得重置");
            }
            u.setPassword(new BCryptPasswordEncoder().encode("123456"));
            userService.update(u);
            redisTemplate.delete("user::"+u.getUsername());
        }
        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户自己资料",notes = "用户名密码不会修改 需要username更新缓存")
    @CacheEvict(key = "#u.username")
    public Result<Object> editOwn(User u){

        User old = securityUtil.getCurrUser();
        u.setUsername(old.getUsername());
        u.setPassword(old.getPassword());
        User user = userService.update(u);
        if(user==null){
            return ResultUtil.error("修改失败");
        }
        return ResultUtil.success("修改成功");
    }

    /**
     * 线上demo不允许测试账号改密码
     * @param password
     * @param newPass
     * @return
     */
    @RequestMapping(value = "/modifyPass", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码")
    public Result<Object> modifyPass(@ApiParam("旧密码") @RequestParam String password,
                                     @ApiParam("新密码") @RequestParam String newPass,
                                     @ApiParam("密码强度") @RequestParam String passStrength){

        User user = securityUtil.getCurrUser();
        // 在线DEMO所需
        if("test".equals(user.getUsername())||"test2".equals(user.getUsername())){
            return ResultUtil.error("演示账号不支持修改密码");
        }

        if(!new BCryptPasswordEncoder().matches(password, user.getPassword())){
            return ResultUtil.error("旧密码不正确");
        }

        String newEncryptPass= new BCryptPasswordEncoder().encode(newPass);
        user.setPassword(newEncryptPass);
        user.setPassStrength(passStrength);
        userService.update(user);

        // 手动更新缓存
        redisTemplate.delete("user::"+user.getUsername());

        return ResultUtil.success("修改密码成功");
    }

    @SystemLog(description = "查询用户列表", type = LogType.OPERATION)
    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<IPage<User>> getByCondition(@ModelAttribute User user,
                                              @ModelAttribute PageVo page){
        List<User> users = null;
        if(StrUtil.isNotBlank(user.getDepartmentId())) {
            users = userService.findByDepartmentAndNameZwz(user.getNickname(),user.getDepartmentId());
        }else{
            users =  userService.findByNameZwz(user.getNickname());
        }
        for (User u : users) {
            // 关联角色
            List<Role> list = iUserRoleService.findByUserIdZwz(u.getId());
            u.setNewRoles(list);
            // 关联部门
            List<String> titles = userDepartmentService.findDepartmentsTitleByUserIdZwz(u.getId());
            List<String> ids = userDepartmentService.findDepartmentsIdByUserIdZwz(u.getId());
            String titleTemp = "";
            for (String title : titles) {
                if(titleTemp.equals("")) titleTemp +=title;
                else titleTemp += "," + title;
            }
            u.setDepartmentIds(ids);
            u.setDepartmentTitle(titleTemp);
            // 游离态 避免后面语句导致持久化
            u.setPassword(null);
            u.setRoles(new ArrayList<>());
        }
        IPage<User> data = iUserService.page(PageUtil.initMpPage(page));
        List<User> ans = new ArrayList<>();
        for(int i = (page.getPageNumber()-1) * page.getPageSize() ; i < page.getPageSize() * page.getPageNumber() && i < users.size(); i ++){
            ans.add(users.get(i));
        }
        data.setRecords(ans);
//        data.setPages(page.getPageNumber());
//        data.setSize(page.getPageSize());
//        data.setTotal(users.size());
        return new ResultUtil<IPage<User>>().setData(data);
    }

    @RequestMapping(value = "/getUserByPostLevel", method = RequestMethod.GET)
    @ApiOperation(value = "根据职位分页获取")
    public Result<IPage<User>> getUserByPostLevel(@RequestParam String id,@ModelAttribute PageVo page){
        List<User> users = null;
        if(id.equals("-1")){
            users = iUserService.list();
        }else {
            users = userService.findByPostZwz(id);
        }
        for (User u : users) {
            // 关联角色
            List<Role> list = iUserRoleService.findByUserId(u.getId());
            List<RoleDTO> roleDTOList = list.stream().map(e->{
                return new RoleDTO().setId(e.getId()).setName(e.getName()).setDescription(e.getDescription());
            }).collect(Collectors.toList());
            u.setRoles(roleDTOList);
            // 关联部门
            List<String> titles = userDepartmentService.findDepartmentsTitleByUserIdZwz(u.getId());
            List<String> ids = userDepartmentService.findDepartmentsIdByUserIdZwz(u.getId());
            String titleTemp = "";
            for (String title : titles) {
                if(titleTemp.equals("")) titleTemp +=title;
                else titleTemp += "," + title;
            }
            u.setDepartmentIds(ids);
            u.setDepartmentTitle(titleTemp);
            // 游离态 避免后面语句导致持久化
            u.setPassword(null);
        }
        IPage<User> data = iUserService.page(PageUtil.initMpPage(page));
        data.setRecords(users);
        data.setPages(page.getPageNumber());
        data.setSize(page.getPageSize());
        data.setTotal(users.size());
        return new ResultUtil<IPage<User>>().setData(data);
    }


    @RequestMapping(value = "/getByDepartmentId/{departmentId}", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<List<User>> getByCondition(@PathVariable String departmentId){

        List<User> list = userService.findByDepartmentId(departmentId);
        entityManager.clear();
        list.forEach(u -> {
            u.setPassword(null);
        });
        return new ResultUtil<List<User>>().setData(list);
    }

    @RequestMapping(value = "/searchByName/{username}", method = RequestMethod.GET)
    @ApiOperation(value = "通过用户名搜索用户")
    public Result<List<User>> searchByName(@PathVariable String username) throws UnsupportedEncodingException {

        List<User> list = userService.findByUsernameLikeAndStatus(URLDecoder.decode(username, "utf-8"), CommonConstant.STATUS_NORMAL);
        entityManager.clear();
        list.forEach(u -> {
            u.setPassword(null);
        });
        return new ResultUtil<List<User>>().setData(list);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部用户数据")
    public Result<List<User>> getByCondition(){

        List<User> list = userService.getAll();
        for(User u: list){
            // 清除持久上下文环境 避免后面语句导致持久化
            entityManager.clear();
            u.setPassword(null);
        }
        return new ResultUtil<List<User>>().setData(list);
    }

    // 废弃
    @SystemLog(description = "新增用户", type = LogType.INSERT)
    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加用户")
    public Result<Object> add(@Valid User u,
                              @RequestParam(required = false) String[] departmentIds,
                              @RequestParam(required = false) String[] roleIds){

        // 校验是否已存在
        checkUserInfo(u.getUsername(), u.getMobile(), u.getEmail());

        String encryptPass = new BCryptPasswordEncoder().encode(u.getPassword());
        u.setPassword(encryptPass);
        if(StrUtil.isNotBlank(u.getDepartmentId())){
            Department d = departmentService.get(u.getDepartmentId());
            if(d!=null){
                u.setDepartmentTitle(d.getTitle());
            }
        }else{
            u.setDepartmentId(null);
            u.setDepartmentTitle("");
        }
        User user = userService.save(u);
        //一人多部门
        if(departmentIds != null){
            userDepartmentService.deleteByUserId(u.getId());
            for (String id : departmentIds) {
                UserDepartment ud = new UserDepartment();
                ud.setUserId(u.getId()).setDepartmentId(id);
                userDepartmentService.save(ud);
            }
        }
        if(roleIds!=null){
            // 添加角色
            List<UserRole> userRoles = Arrays.asList(roleIds).stream().map(e -> {
                return new UserRole().setUserId(u.getId()).setRoleId(e);
            }).collect(Collectors.toList());
            userRoleService.saveOrUpdateAll(userRoles);
        }
        return ResultUtil.success("添加成功");
    }

    @SystemLog(description = "修改用户资料", type = LogType.INSERT)
    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    @ApiOperation(value = "管理员修改资料",notes = "需要通过id获取原用户信息 需要username更新缓存")
    @CacheEvict(key = "#u.username")
    public Result<Object> edit(User u,
                               @RequestParam(required = false) String[] departmentIds,
                               @RequestParam(required = false) String roleIds){

        SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd");
        Roster roster = iRosterService.findById(u.getRosterId());
        // 修改用户信息,同步修改花名册信息
        if(roster != null){
            roster.setMobile(u.getMobile());
            roster.setIntroduce(u.getDescription());
            roster.setUserName(u.getNickname());
            roster.setSex(u.getSex());
            roster.setEmail(u.getEmail());
            if(u.getAddress() != null && u.getAddress().length()>8){
                roster.setNativePlace(new CityUtil().getNativePlace(Integer.parseInt(u.getAddress().substring(u.getAddress().length()-8, u.getAddress().length()-2))));
            }
            roster.setBirthday(format.format(u.getBirth()));
            iRosterService.saveOrUpdate(roster);
        }
        //部门
        userDepartmentService.deleteByUserId(u.getId());
        if(departmentIds != null && departmentIds.length > 0){
            for (String id : departmentIds) {
                UserDepartment ud = new UserDepartment();
                ud.setUserId(u.getId()).setDepartmentId(id);
                userDepartmentService.save(ud);
            }
        }else if(departmentIds != null){
            // 部门为空,添加到未挂靠部门
            UserDepartment ud = new UserDepartment();
            ud.setUserId(u.getId()).setDepartmentId("394196146");
            userDepartmentService.save(ud);
        }
        User old = userService.get(u.getId());
        u.setUsername(old.getUsername());
        // 若修改了手机和邮箱判断是否唯一
        if(!old.getMobile().equals(u.getMobile())&&userService.findByMobile(u.getMobile())!=null){
            return ResultUtil.error("该手机号已绑定其他账户");
        }
        if(old.getEmail()!= null && !old.getEmail().equals(u.getEmail())&&userService.findByEmail(u.getEmail())!=null){
            return ResultUtil.error("该邮箱已绑定其他账户");
        }

        if(StrUtil.isNotBlank(u.getDepartmentId())){
            Department d = departmentService.get(u.getDepartmentId());
            if(d!=null){
                u.setDepartmentTitle(d.getTitle());
            }
        }else{
            u.setDepartmentId(null);
            u.setDepartmentTitle("");
        }
        // 修改岗位名称
        if(u.getPost() != null){
            PostLevel postLevel = iPostLevelService.findByIdZwz(u.getPost());
            u.setPostName(postLevel == null ? null : postLevel.getTitle());
        }
        u.setPassword(old.getPassword());
        userService.update(u);
        // 同步钉钉
        List<Department> departments = userDepartmentService.findDepartmentsByUserIdZwz(u.getId());
        DingtalkUtils.updateUser(userToDingtalkUser(u),DingtalkUtils.getDepartmentIdByDepartment(departments));
        // 删除该用户角色
        userRoleService.deleteByUserId(u.getId());
        if(u.getRoleIds() != null){
            String[] roleEditRoleIds = u.getRoleIds().split(",");
            for (String roleId : roleEditRoleIds) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(u.getId());
                userRoleService.save(userRole);
            }
        }
        // 手动删除缓存
        redisTemplate.delete("userRole::"+u.getId());
        redisTemplate.delete("userRole::depIds:"+u.getId());
        redisTemplate.delete("permission::userMenuList:"+u.getId());
        return ResultUtil.success("修改成功");
    }

    @RequestMapping(value = "/admin/disable/{userId}", method = RequestMethod.POST)
    @ApiOperation(value = "后台禁用用户")
    public Result<Object> disable(@ApiParam("用户唯一id标识") @PathVariable String userId){

        User user = userService.get(userId);
        if(user==null){
            return ResultUtil.error("通过userId获取用户失败");
        }
        // 查询是否部门负责人
        List<Department> headerDepartment = departmentHeaderService.findDepartmentHeaderByUserIdZwz(user.getId());
        if(!headerDepartment.isEmpty()){
            String headerErrorMsg = "请先取消以下部门的负责人资格: ";
            for (Department department : headerDepartment) {
                headerErrorMsg += department.getTitle();
            }
            return ResultUtil.error(headerErrorMsg);
        }
        user.setStatus(CommonConstant.USER_STATUS_LOCK);
        userService.update(user);
        // 找到对应花名册 设置离职状态
        if(user.getRosterId() != null){
            Roster roster = iRosterService.findById(user.getRosterId());
            if(roster != null){
                roster.setStatus("-1");
                iRosterService.saveOrUpdate(roster);
            }
        }
        // 因为钉钉暂不支持指定ID的添加,所以禁用启用同步钉钉暂且关闭
        // DingtalkUtils.deleteUser(userId);
        // 手动更新缓存
        redisTemplate.delete("user::"+user.getUsername());
        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/admin/enable/{userId}", method = RequestMethod.POST)
    @ApiOperation(value = "后台启用用户")
    public Result<Object> enable(@ApiParam("用户唯一id标识") @PathVariable String userId){

        User user = userService.get(userId);
        if(user==null){
            return ResultUtil.error("通过userId获取用户失败");
        }
        user.setStatus(CommonConstant.USER_STATUS_NORMAL);
        userService.update(user);
        // 找到对应花名册 设置复职状态
        if(user.getRosterId() != null){
            Roster roster = iRosterService.findById(user.getRosterId());
            if(roster != null){
                roster.setStatus("0");
                iRosterService.saveOrUpdate(roster);
            }
        }
        // 因为钉钉暂不支持指定ID的添加,所以禁用启用同步钉钉暂且关闭
        // DingtalkUtils.createUser(userToDingtalkUser(user));
        // 手动更新缓存
        redisTemplate.delete("user::"+user.getUsername());
        return ResultUtil.success("操作成功");
    }

    @SystemLog(description = "删除用户", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过ids删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id:ids){
            // 超级管理员 不能删除
            if(id.equals("201726206536267972")) continue;
            // 查询是否部门负责人
            List<Department> headerDepartment = departmentHeaderService.findDepartmentHeaderByUserIdZwz(id);
            if(!headerDepartment.isEmpty()){
                String headerErrorMsg = "请先取消以下部门的负责人资格: ";
                for (Department department : headerDepartment) {
                    headerErrorMsg += department.getTitle();
                }
                return ResultUtil.error(headerErrorMsg);
            }
            // 花名册设置禁用状态
            User u = userService.get(id);
            String rosterId = u.getRosterId();
            if(rosterId != null){
                Roster roster = iRosterService.findById(rosterId);
                if(roster != null){
                    roster.setStatus("-1");
                    iRosterService.saveOrUpdate(roster);
                }
            }
            // 删除用户的部门
            userDepartmentService.deleteByUserId(u.getId());
            // 删除相关缓存
            redisTemplate.delete("user::" + u.getUsername());
            redisTemplate.delete("userRole::" + u.getId());
            redisTemplate.delete("userRole::depIds:" + u.getId());
            redisTemplate.delete("permission::userMenuList:" + u.getId());
            Set<String> keys = redisTemplateHelper.keys("department::*");
            redisTemplate.delete(keys);
            DingtalkUtils.deleteUser(id);
            userService.delete(id);

            // 删除关联角色
            userRoleService.deleteByUserId(id);
            // 删除关联部门负责人
            departmentHeaderService.deleteByUserId(id);

            // 删除关联流程、社交账号数据
            try {
                deleteMapper.deleteActNode(u.getId());
                deleteMapper.deleteActStarter(u.getId());
                deleteMapper.deleteSocial(u.getUsername());
            }catch (Exception e){
                log.warn(e.toString());
            }
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/importData", method = RequestMethod.POST)
    @ApiOperation(value = "导入用户数据")
    public Result<Object> importData(@RequestBody List<User> users){

        List<Integer> errors = new ArrayList<>();
        List<String> reasons = new ArrayList<>();
        int count = 0;
        for(User u: users){
            count++;
            // 验证用户名密码不为空
            if(StrUtil.isBlank(u.getUsername())||StrUtil.isBlank(u.getPassword())){
                errors.add(count);
                reasons.add("用户名或密码为空");
                continue;
            }
            // 验证用户名唯一
            if(userService.findByUsername(u.getUsername())!=null){
                errors.add(count);
                reasons.add("用户名已存在");
                continue;
            }
            // 加密密码
            u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
            // 验证部门id正确性
            if(StrUtil.isNotBlank(u.getDepartmentId())){
                try {
                    Department d = departmentService.get(u.getDepartmentId());
                    log.info(d.toString());
                }catch (Exception e){
                    errors.add(count);
                    reasons.add("部门id不存在");
                    continue;
                }
            }
            if(u.getStatus()==null){
                u.setStatus(CommonConstant.USER_STATUS_NORMAL);
            }
            userService.save(u);
            // 分配默认角色
            if(u.getDefaultRole()!=null&&u.getDefaultRole()==1){
                List<Role> roleList = roleService.findByDefaultRole(true);
                if(roleList!=null&&roleList.size()>0){
                    for(Role role : roleList){
                        UserRole ur = new UserRole().setUserId(u.getId()).setRoleId(role.getId());
                        userRoleService.save(ur);
                    }
                }
            }
        }
        // 批量保存数据
        int successCount = users.size() - errors.size();
        String successMessage = "全部导入成功，共计 " + successCount + " 条数据";
        String failMessage = "导入成功 " + successCount + " 条，失败 " + errors.size() + " 条数据。<br>" +
                "第 " + errors.toString() + " 行数据导入出错，错误原因分别为：<br>" + reasons.toString();
        String message = "";
        if(errors.size()==0){
            message = successMessage;
        }else{
            message = failMessage;
        }
        return ResultUtil.success(message);
    }

    /**
     * 校验
     * @param username 用户名 不校验传空字符或null 下同
     * @param mobile 手机号
     * @param email 邮箱
     */
    public void checkUserInfo(String username, String mobile, String email){

        // 禁用词
        CommonUtil.stopwords(username);

        if(StrUtil.isNotBlank(username)&&userService.findByUsername(username)!=null){
            throw new XbootException("该登录账号已被注册");
        }
        if(StrUtil.isNotBlank(email)&&userService.findByEmail(email)!=null){
            throw new XbootException("该邮箱已被注册");
        }
        if(StrUtil.isNotBlank(mobile)&&userService.findByMobile(mobile)!=null){
            throw new XbootException("该手机号已被注册");
        }
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
}
