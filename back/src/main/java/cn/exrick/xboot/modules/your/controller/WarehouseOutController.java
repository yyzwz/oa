package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.AssetsType;
import cn.exrick.xboot.modules.your.entity.WarehouseOut;
import cn.exrick.xboot.modules.your.entity.Warehousing;
import cn.exrick.xboot.modules.your.service.IAssetsTypeService;
import cn.exrick.xboot.modules.your.service.IWarehouseOutService;
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
@Api(description = "出库管理管理接口")
@RequestMapping("/xboot/warehouseOut")
@Transactional
public class WarehouseOutController {

    @Autowired
    private IWarehouseOutService iWarehouseOutService;

    @Autowired
    private IAssetsTypeService iAssetsTypeService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<WarehouseOut> get(@PathVariable String id){

        WarehouseOut warehouseOut = iWarehouseOutService.getById(id);
        return new ResultUtil<WarehouseOut>().setData(warehouseOut);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<WarehouseOut>> getAll(){

        List<WarehouseOut> list = iWarehouseOutService.list();
        return new ResultUtil<List<WarehouseOut>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<WarehouseOut>> getByPage(@ModelAttribute WarehouseOut warehouseOut, @ModelAttribute PageVo page){

        QueryWrapper<WarehouseOut> qw = new QueryWrapper<WarehouseOut>();
        if(StrUtil.isNotBlank(warehouseOut.getNature())) {
            qw.eq("nature", warehouseOut.getNature());
        }
        if(StrUtil.isNotBlank(warehouseOut.getAssetName())) {
            qw.like("asset_name", warehouseOut.getAssetName());
        }
        if(StrUtil.isNotBlank(warehouseOut.getRecipients())) {
            qw.like("recipients", warehouseOut.getRecipients());
        }
        IPage<WarehouseOut> data = iWarehouseOutService.page(PageUtil.initMpPage(page),qw);
        for (WarehouseOut wh : data.getRecords()) {
            AssetsType assetsType = iAssetsTypeService.findByIdZwz(wh.getAssetId());
            double existnumber = Double.parseDouble(assetsType.getExistingNumber()) + Double.parseDouble(wh.getNumber());
            wh.setExistNumber("" + existnumber);
        }
        return new ResultUtil<IPage<WarehouseOut>>().setData(data);
    }

    @SystemLog(description = "新增/修改出库记录", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<WarehouseOut> saveOrUpdate(WarehouseOut warehouseOut){
        WarehouseOut oldWarehouseOut = iWarehouseOutService.findByIdZwz(warehouseOut.getId());
        double oldNumber = 0.0;
        if(oldWarehouseOut != null){
            oldNumber = Double.parseDouble(oldWarehouseOut.getNumber());
        }
        // outNumber 要出库的数量
        double outNumber = Double.parseDouble(warehouseOut.getNumber());
        AssetsType oldAssetsType = iAssetsTypeService.findByIdZwz(warehouseOut.getAssetId());
        if(oldAssetsType != null){
            // newNumber 出库后还有的数量 = 仓库原本还有的数量 - 出库单的出库数量 + 原有出库单的出库数量
            Double newNumber = Double.parseDouble(oldAssetsType.getExistingNumber()) - outNumber + oldNumber;
            if(newNumber >= 0){
                oldAssetsType.setExistingNumber(newNumber + "");
                iAssetsTypeService.saveOrUpdate(oldAssetsType);
            }else{
                return new ResultUtil<WarehouseOut>().setErrorMsg("手慢啦!库存不足!");
            }
        }
        if(iWarehouseOutService.saveOrUpdate(warehouseOut)){
            return new ResultUtil<WarehouseOut>().setData(warehouseOut);
        }
        return new ResultUtil<WarehouseOut>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除出库记录", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            WarehouseOut warehouseOut = iWarehouseOutService.findByIdZwz(id);
            if(warehouseOut != null){
                AssetsType assetsType = iAssetsTypeService.findByIdZwz(warehouseOut.getAssetId());
                // 出库单删除之后的库存 = 现有库存 + 出库单的资产数量
                double newNumber = Double.parseDouble(assetsType.getExistingNumber()) + Double.parseDouble(warehouseOut.getNumber());
                assetsType.setExistingNumber("" + newNumber);
                iAssetsTypeService.saveOrUpdate(assetsType);
            }
            iWarehouseOutService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
