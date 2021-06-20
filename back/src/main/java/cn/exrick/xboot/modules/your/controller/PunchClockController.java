package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.entity.Role;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.entity.UserRole;
import cn.exrick.xboot.modules.your.entity.PunchClock;
import cn.exrick.xboot.modules.your.entity.PunchClockExcel;
import cn.exrick.xboot.modules.your.entity.WaterCost;
import cn.exrick.xboot.modules.your.service.IPunchClockService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "考勤打卡管理接口")
@RequestMapping("/xboot/punchClock")
@Transactional
public class PunchClockController {

    @Autowired
    private IPunchClockService iPunchClockService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<PunchClock> get(@PathVariable String id){

        PunchClock punchClock = iPunchClockService.getById(id);
        return new ResultUtil<PunchClock>().setData(punchClock);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<PunchClock>> getAll(){

        List<PunchClock> list = iPunchClockService.list();
        return new ResultUtil<List<PunchClock>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<PunchClock>> getByPage(@ModelAttribute PunchClock punchClock, @ModelAttribute PageVo page){
        QueryWrapper<PunchClock> qw = new QueryWrapper<PunchClock>();
        if(StrUtil.isNotBlank(punchClock.getYear())) {
            qw.eq("year", punchClock.getYear());
        }
        if(StrUtil.isNotBlank(punchClock.getMouth())) {
            qw.eq("mouth", punchClock.getMouth());
        }
        if(StrUtil.isNotBlank(punchClock.getUserId())) {
            qw.eq("user_id", punchClock.getUserId());
        }
        if(StrUtil.isNotBlank(punchClock.getUserName())) {
            qw.like("user_name", punchClock.getUserName());
        }
        IPage<PunchClock> data = iPunchClockService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<PunchClock>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<PunchClock> saveOrUpdate(PunchClock punchClock){

        if(iPunchClockService.saveOrUpdate(punchClock)){
            return new ResultUtil<PunchClock>().setData(punchClock);
        }
        return new ResultUtil<PunchClock>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iPunchClockService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/importData", method = RequestMethod.POST)
    @ApiOperation(value = "导入用户数据")
    public Result<Object> importData(@RequestBody List<PunchClock> punchClocks){

        List<Integer> errors = new ArrayList<>();
        List<String> reasons = new ArrayList<>();
        int count = 0;

        for(PunchClock pce: punchClocks){
            count++;
            // 验证用户名密码不为空
            if(StrUtil.isBlank(pce.getUserId())){
                errors.add(count);
                reasons.add("用户工号为空");
                continue;
            }
            if(StrUtil.isBlank(pce.getYear())){
                errors.add(count);
                reasons.add("年份为空");
                continue;
            }
            if(StrUtil.isBlank(pce.getMouth())){
                errors.add(count);
                reasons.add("月份为空");
                continue;
            }
            // 计算应出勤天数和实际出勤天数
            List<String> workDays = calWorkDays(pce);
            pce.setShouldDay(workDays.get(0));
            pce.setActualDay(workDays.get(1));
            iPunchClockService.saveOrUpdate(pce);
        }
        // 批量保存数据
        int successCount = punchClocks.size() - errors.size();
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
     * 计算应出勤天数和实际出勤天数
     * @param pce
     * @return
     */
    public List<String> calWorkDays(PunchClock pce){
        // 0 应该出勤但未出勤
        // 1 出勤成功
        // 2 休假日
        int sum1 = 0,sum2 = 0; // sum1 出勤成功天数  , sum2先放未出勤天数后算总天数
        if(pce.getDay1().equals("1")) sum1 ++;
        else if(pce.getDay1().equals("0")) sum2 ++;
        if(pce.getDay2().equals("1")) sum1 ++;
        else if(pce.getDay2().equals("0")) sum2 ++;
        if(pce.getDay3().equals("1")) sum1 ++;
        else if(pce.getDay3().equals("0")) sum2 ++;
        if(pce.getDay4().equals("1")) sum1 ++;
        else if(pce.getDay4().equals("0")) sum2 ++;
        if(pce.getDay5().equals("1")) sum1 ++;
        else if(pce.getDay5().equals("0")) sum2 ++;
        if(pce.getDay6().equals("1")) sum1 ++;
        else if(pce.getDay6().equals("0")) sum2 ++;
        if(pce.getDay7().equals("1")) sum1 ++;
        else if(pce.getDay7().equals("0")) sum2 ++;
        if(pce.getDay8().equals("1")) sum1 ++;
        else if(pce.getDay8().equals("0")) sum2 ++;
        if(pce.getDay9().equals("1")) sum1 ++;
        else if(pce.getDay9().equals("0")) sum2 ++;
        if(pce.getDay10().equals("1")) sum1 ++;
        else if(pce.getDay10().equals("0")) sum2 ++;
        if(pce.getDay11().equals("1")) sum1 ++;
        else if(pce.getDay11().equals("0")) sum2 ++;
        if(pce.getDay12().equals("1")) sum1 ++;
        else if(pce.getDay12().equals("0")) sum2 ++;
        if(pce.getDay13().equals("1")) sum1 ++;
        else if(pce.getDay13().equals("0")) sum2 ++;
        if(pce.getDay14().equals("1")) sum1 ++;
        else if(pce.getDay14().equals("0")) sum2 ++;
        if(pce.getDay15().equals("1")) sum1 ++;
        else if(pce.getDay15().equals("0")) sum2 ++;
        if(pce.getDay16().equals("1")) sum1 ++;
        else if(pce.getDay16().equals("0")) sum2 ++;
        if(pce.getDay17().equals("1")) sum1 ++;
        else if(pce.getDay17().equals("0")) sum2 ++;
        if(pce.getDay18().equals("1")) sum1 ++;
        else if(pce.getDay18().equals("0")) sum2 ++;
        if(pce.getDay19().equals("1")) sum1 ++;
        else if(pce.getDay19().equals("0")) sum2 ++;
        if(pce.getDay20().equals("1")) sum1 ++;
        else if(pce.getDay20().equals("0")) sum2 ++;
        if(pce.getDay21().equals("1")) sum1 ++;
        else if(pce.getDay21().equals("0")) sum2 ++;
        if(pce.getDay22().equals("1")) sum1 ++;
        else if(pce.getDay22().equals("0")) sum2 ++;
        if(pce.getDay23().equals("1")) sum1 ++;
        else if(pce.getDay23().equals("0")) sum2 ++;
        if(pce.getDay24().equals("1")) sum1 ++;
        else if(pce.getDay24().equals("0")) sum2 ++;
        if(pce.getDay25().equals("1")) sum1 ++;
        else if(pce.getDay25().equals("0")) sum2 ++;
        if(pce.getDay26().equals("1")) sum1 ++;
        else if(pce.getDay26().equals("0")) sum2 ++;
        if(pce.getDay27().equals("1")) sum1 ++;
        else if(pce.getDay27().equals("0")) sum2 ++;
        if(pce.getDay28().equals("1")) sum1 ++;
        else if(pce.getDay28().equals("0")) sum2 ++;
        if(pce.getDay29().equals("1")) sum1 ++;
        else if(pce.getDay29().equals("0")) sum2 ++;
        if(pce.getDay30().equals("1")) sum1 ++;
        else if(pce.getDay30().equals("0")) sum2 ++;
        if(pce.getDay31().equals("1")) sum1 ++;
        else if(pce.getDay31().equals("0")) sum2 ++;
        sum2 = sum1 + sum2;
        List<String> ans = new ArrayList<>();
        ans.add(sum1 + "");
        ans.add(sum2 + "");
        return ans;
    }
}
