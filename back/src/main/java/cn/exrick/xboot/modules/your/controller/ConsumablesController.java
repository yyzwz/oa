package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.Consumables;
import cn.exrick.xboot.modules.your.entity.WaterCost;
import cn.exrick.xboot.modules.your.service.IConsumablesService;
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
@Api(description = "耗材管理管理接口")
@RequestMapping("/xboot/consumables")
@Transactional
public class ConsumablesController {

    @Autowired
    private IConsumablesService iConsumablesService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Consumables> get(@PathVariable String id){

        Consumables consumables = iConsumablesService.getById(id);
        return new ResultUtil<Consumables>().setData(consumables);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Consumables>> getAll(){

        List<Consumables> list = iConsumablesService.list();
        return new ResultUtil<List<Consumables>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Consumables>> getByPage(@ModelAttribute Consumables consumables, @ModelAttribute PageVo page){
        QueryWrapper<Consumables> qw = new QueryWrapper<Consumables>();
        if(StrUtil.isNotBlank(consumables.getAssetName())) {
            qw.eq("asset_name", consumables.getAssetName());
        }
        if(StrUtil.isNotBlank(consumables.getRecipients())) {
            qw.eq("recipients", consumables.getRecipients());
        }
        IPage<Consumables> data = iConsumablesService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Consumables>>().setData(data);
    }

    @SystemLog(description = "新增/修改耗材", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Consumables> saveOrUpdate(Consumables consumables){
        consumables.setTotalPrice(Double.parseDouble(consumables.getNumber()) * Double.parseDouble(consumables.getUnitPrice()) + "");
        if(iConsumablesService.saveOrUpdate(consumables)){
            return new ResultUtil<Consumables>().setData(consumables);
        }
        return new ResultUtil<Consumables>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除耗材", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iConsumablesService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
