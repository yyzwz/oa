package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.PaySlip;
import cn.exrick.xboot.modules.your.entity.PunchClock;
import cn.exrick.xboot.modules.your.entity.Roster;
import cn.exrick.xboot.modules.your.entity.Welfare;
import cn.exrick.xboot.modules.your.service.IPaySlipService;
import cn.exrick.xboot.modules.your.service.IRosterService;
import cn.exrick.xboot.modules.your.service.IWelfareService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "工资条管理接口")
@RequestMapping("/xboot/paySlip")
@Transactional
public class PaySlipController {

    @Autowired
    private IPaySlipService iPaySlipService;

    @Autowired
    private IRosterService iRosterService;

    @Autowired
    private IWelfareService iWelfareService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<PaySlip> get(@PathVariable String id){

        PaySlip paySlip = iPaySlipService.getById(id);
        return new ResultUtil<PaySlip>().setData(paySlip);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<PaySlip>> getAll(){

        List<PaySlip> list = iPaySlipService.list();
        return new ResultUtil<List<PaySlip>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<PaySlip>> getByPage(@ModelAttribute PaySlip paySlip, @ModelAttribute PageVo page){
        QueryWrapper<PaySlip> qw = new QueryWrapper<PaySlip>();
        if(StrUtil.isNotBlank(paySlip.getUserName())) {
            qw.like("user_name", paySlip.getUserName());
        }
        if(StrUtil.isNotBlank(paySlip.getUserId())) {
            qw.eq("user_id", paySlip.getUserId());
        }
        if(StrUtil.isNotBlank(paySlip.getYear())) {
            qw.eq("year", paySlip.getYear());
        }
        if(StrUtil.isNotBlank(paySlip.getMouth())) {
            qw.eq("mouth", paySlip.getMouth());
        }
        IPage<PaySlip> data = iPaySlipService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<PaySlip>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<PaySlip> saveOrUpdate(PaySlip paySlip){

        if(iPaySlipService.saveOrUpdate(paySlip)){
            return new ResultUtil<PaySlip>().setData(paySlip);
        }
        return new ResultUtil<PaySlip>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iPaySlipService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/batchGeneration", method = RequestMethod.GET)
    public Result<Object> batchGeneration(@RequestParam String year,@RequestParam String mouth){
        if(!StrUtil.isNotBlank(year) || !StrUtil.isNotBlank(mouth)) {
            return ResultUtil.error("年份月份输入错误");
        }
        // 删除原有数据
        iPaySlipService.deleteByYearAndMouth(year,mouth);
        List<Roster> rosters = iRosterService.list();
        for (Roster roster : rosters) {
            Welfare welfare = iWelfareService.findByRosterIdZwz(roster.getId());
            PaySlip paySlip = new PaySlip();
            paySlip.setYear(year);
            paySlip.setMouth(mouth);
            paySlip.setUserId(roster.getId());
            paySlip.setUserName(roster.getUserName());
            if(welfare != null){
                paySlip.setBaseSalary(welfare.getBaseSalary());
                paySlip.setBirthdayBonus(welfare.getBirthdayBonus());
                paySlip.setCommunicationSubsidy(welfare.getCommunicationSubsidy());;
                paySlip.setTransportationSubsidy(welfare.getTransportationSubsidy());
                paySlip.setFullReward(welfare.getFullReward());
                paySlip.setMalAllowance(welfare.getMalAllowance());
                double sum = Double.parseDouble(paySlip.getBaseSalary()) + Double.parseDouble(paySlip.getBirthdayBonus()) +
                        Double.parseDouble(paySlip.getCommunicationSubsidy()) + Double.parseDouble(paySlip.getTransportationSubsidy()) +
                        Double.parseDouble(paySlip.getMalAllowance()) + Double.parseDouble(paySlip.getFullReward());
                paySlip.setSum(sum + "");
            }
            iPaySlipService.save(paySlip);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
