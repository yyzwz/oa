package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.MonthlyRent;
import cn.exrick.xboot.modules.crm.entity.ShiChangRent;
import cn.exrick.xboot.modules.crm.service.IMonthlyRentService;
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
@Api(description = "月租金管理接口")
@RequestMapping("/xboot/monthlyRent")
@Transactional
public class MonthlyRentController {

    @Autowired
    private IMonthlyRentService iMonthlyRentService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<MonthlyRent> get(@PathVariable Long id){

        MonthlyRent monthlyRent = iMonthlyRentService.getById(id);
        return new ResultUtil<MonthlyRent>().setData(monthlyRent);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<MonthlyRent>> getAll(){

        List<MonthlyRent> list = iMonthlyRentService.list();
        return new ResultUtil<List<MonthlyRent>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<MonthlyRent>> getByPage(@ModelAttribute MonthlyRent monthlyRent, @ModelAttribute PageVo page){
        QueryWrapper<MonthlyRent> qw = new QueryWrapper<MonthlyRent>();
        if(StrUtil.isNotBlank(monthlyRent.getBan())) {
            qw.like("ban", monthlyRent.getBan());
        }
        if(StrUtil.isNotBlank(monthlyRent.getNumber())) {
            qw.like("number", monthlyRent.getNumber());
        }
        if(StrUtil.isNotBlank(monthlyRent.getRemark())) {
            qw.like("remark", monthlyRent.getRemark());
        }
        if(StrUtil.isNotBlank(monthlyRent.getTitle())) {
            qw.like("title", monthlyRent.getTitle());
        }
        IPage<MonthlyRent> data = iMonthlyRentService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<MonthlyRent>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<MonthlyRent> saveOrUpdate(MonthlyRent monthlyRent){

        if(iMonthlyRentService.saveOrUpdate(monthlyRent)){
            return new ResultUtil<MonthlyRent>().setData(monthlyRent);
        }
        return new ResultUtil<MonthlyRent>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iMonthlyRentService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
