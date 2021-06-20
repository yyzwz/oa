package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.MonthCost;
import cn.exrick.xboot.modules.crm.entity.ShiChangRent;
import cn.exrick.xboot.modules.crm.service.IMonthCostService;
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
@Api(description = "人员月费用管理接口")
@RequestMapping("/xboot/monthCost")
@Transactional
public class MonthCostController {

    @Autowired
    private IMonthCostService iMonthCostService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<MonthCost> get(@PathVariable Long id){

        MonthCost monthCost = iMonthCostService.getById(id);
        return new ResultUtil<MonthCost>().setData(monthCost);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<MonthCost>> getAll(){

        List<MonthCost> list = iMonthCostService.list();
        return new ResultUtil<List<MonthCost>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<MonthCost>> getByPage(@ModelAttribute MonthCost monthCost, @ModelAttribute PageVo page){
        QueryWrapper<MonthCost> qw = new QueryWrapper<MonthCost>();
        if(StrUtil.isNotBlank(monthCost.getBan())) {
            qw.like("ban", monthCost.getBan());
        }
        if(StrUtil.isNotBlank(monthCost.getNumber())) {
            qw.like("number", monthCost.getNumber());
        }
        if(StrUtil.isNotBlank(monthCost.getRemark())) {
            qw.like("remark", monthCost.getRemark());
        }
        if(StrUtil.isNotBlank(monthCost.getTitle())) {
            qw.like("title", monthCost.getTitle());
        }
        IPage<MonthCost> data = iMonthCostService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<MonthCost>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<MonthCost> saveOrUpdate(MonthCost monthCost){

        if(iMonthCostService.saveOrUpdate(monthCost)){
            return new ResultUtil<MonthCost>().setData(monthCost);
        }
        return new ResultUtil<MonthCost>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iMonthCostService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
