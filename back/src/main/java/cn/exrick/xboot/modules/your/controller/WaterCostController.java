package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.ElectricCost;
import cn.exrick.xboot.modules.your.entity.WaterCost;
import cn.exrick.xboot.modules.your.service.IWaterCostService;
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
@Api(description = "水费管理管理接口")
@RequestMapping("/xboot/waterCost")
@Transactional
public class WaterCostController {

    @Autowired
    private IWaterCostService iWaterCostService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<WaterCost> get(@PathVariable String id){

        WaterCost waterCost = iWaterCostService.getById(id);
        return new ResultUtil<WaterCost>().setData(waterCost);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<WaterCost>> getAll(){

        List<WaterCost> list = iWaterCostService.list();
        return new ResultUtil<List<WaterCost>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<WaterCost>> getByPage(@ModelAttribute WaterCost waterCost, @ModelAttribute PageVo page){
        QueryWrapper<WaterCost> qw = new QueryWrapper<WaterCost>();
        if(StrUtil.isNotBlank(waterCost.getYear())) {
            qw.eq("year", waterCost.getYear());
        }
        if(StrUtil.isNotBlank(waterCost.getMouth())) {
            qw.eq("mouth", waterCost.getMouth());
        }
        IPage<WaterCost> data = iWaterCostService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<WaterCost>>().setData(data);
    }

    @SystemLog(description = "新增/修改水费记录", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<WaterCost> saveOrUpdate(WaterCost waterCost){

        if(iWaterCostService.saveOrUpdate(waterCost)){
            return new ResultUtil<WaterCost>().setData(waterCost);
        }
        return new ResultUtil<WaterCost>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除水费记录", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iWaterCostService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
