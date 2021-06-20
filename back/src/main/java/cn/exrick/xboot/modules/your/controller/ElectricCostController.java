package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.ElectricCost;
import cn.exrick.xboot.modules.your.service.IElectricCostService;
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
@Api(description = "电费管理管理接口")
@RequestMapping("/xboot/electricCost")
@Transactional
public class ElectricCostController {

    @Autowired
    private IElectricCostService iElectricCostService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ElectricCost> get(@PathVariable String id){

        ElectricCost electricCost = iElectricCostService.getById(id);
        return new ResultUtil<ElectricCost>().setData(electricCost);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ElectricCost>> getAll(){

        List<ElectricCost> list = iElectricCostService.list();
        return new ResultUtil<List<ElectricCost>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ElectricCost>> getByPage(@ModelAttribute ElectricCost electricCost,@ModelAttribute PageVo page){
        QueryWrapper<ElectricCost> qw = new QueryWrapper<ElectricCost>();
        if(StrUtil.isNotBlank(electricCost.getYear())) {
            qw.eq("year", electricCost.getYear());
        }
        if(StrUtil.isNotBlank(electricCost.getMouth())) {
            qw.eq("mouth", electricCost.getMouth());
        }
        IPage<ElectricCost> data = iElectricCostService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ElectricCost>>().setData(data);
    }

    @SystemLog(description = "新增/修改电费记录", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ElectricCost> saveOrUpdate(ElectricCost electricCost){

        if(iElectricCostService.saveOrUpdate(electricCost)){
            return new ResultUtil<ElectricCost>().setData(electricCost);
        }
        return new ResultUtil<ElectricCost>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除电费记录", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iElectricCostService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
