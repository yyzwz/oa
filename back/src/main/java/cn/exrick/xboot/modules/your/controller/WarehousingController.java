package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.AssetsType;
import cn.exrick.xboot.modules.your.entity.Warehousing;
import cn.exrick.xboot.modules.your.entity.WaterCost;
import cn.exrick.xboot.modules.your.service.IAssetsTypeService;
import cn.exrick.xboot.modules.your.service.IWarehousingService;
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
@Api(description = "入库管理管理接口")
@RequestMapping("/xboot/warehousing")
@Transactional
public class WarehousingController {

    @Autowired
    private IWarehousingService iWarehousingService;

    @Autowired
    private IAssetsTypeService iAssetsTypeServicel;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Warehousing> get(@PathVariable String id){

        Warehousing warehousing = iWarehousingService.getById(id);
        return new ResultUtil<Warehousing>().setData(warehousing);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Warehousing>> getAll(){

        List<Warehousing> list = iWarehousingService.list();
        return new ResultUtil<List<Warehousing>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Warehousing>> getByPage(@ModelAttribute Warehousing warehousing, @ModelAttribute PageVo page){
        QueryWrapper<Warehousing> qw = new QueryWrapper<Warehousing>();
        if(StrUtil.isNotBlank(warehousing.getNature())) {
            qw.eq("nature", warehousing.getNature());
        }
        if(StrUtil.isNotBlank(warehousing.getAssetName())) {
            qw.like("asset_name", warehousing.getAssetName());
        }
        if(StrUtil.isNotBlank(warehousing.getInvoice())) {
            qw.like("invoice", warehousing.getInvoice());
        }
        IPage<Warehousing> data = iWarehousingService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Warehousing>>().setData(data);
    }

    @SystemLog(description = "新增/修改入库记录", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Warehousing> insertOrUpdate(Warehousing warehousing){
        Warehousing oldWarehousing = iWarehousingService.findByIdZwz(warehousing.getId());
        double oldNumber = 0.0;
        if(oldWarehousing != null){
            oldNumber = oldWarehousing.getNumber() == null ? 0.0 : Double.parseDouble(oldWarehousing.getNumber());
        }
        // 入库,更新库存
        AssetsType assetsType = iAssetsTypeServicel.findByIdZwz(warehousing.getAssetId());
        if(assetsType != null){
            // number = 现在仓库总数量 + 该入库单现增加数量 - 该入库单原有增加数量
            double number = Double.parseDouble(assetsType.getNumber()) + Double.parseDouble(warehousing.getNumber()) - oldNumber;
            assetsType.setNumber(number + "");
            assetsType.setTotalPrice((Double.parseDouble(assetsType.getUnitPrice()) * number) + "");
            // existNumber = 现在仓库存在数量 + 该入库单现增加数量 - 该入库单原有增加数量
            double existNumber = Double.parseDouble(assetsType.getExistingNumber()) + Double.parseDouble(warehousing.getNumber()) - oldNumber;
            if(existNumber < 0)  return new ResultUtil<Warehousing>().setErrorMsg("入库数量大于已出库数量!");
            assetsType.setExistingNumber(existNumber + "");
            iAssetsTypeServicel.saveOrUpdate(assetsType);
        }
        if(iWarehousingService.saveOrUpdate(warehousing)){
            return new ResultUtil<Warehousing>().setData(warehousing);
        }
        return new ResultUtil<Warehousing>().setErrorMsg("操作失败");
    }


    @SystemLog(description = "删除入库记录", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            // 删除仓库内的数量
            Warehousing warehousing = iWarehousingService.findByIdZwz(id);
            if(warehousing != null){
                // 获取入库单的数量
                Double number = Double.parseDouble(warehousing.getNumber());
                // 获取入库单的资产ID
                String assetId = warehousing.getAssetId();
                AssetsType assetsType = iAssetsTypeServicel.findByIdZwz(assetId);
                if(assetsType != null){
                    double newNumber = Double.parseDouble(assetsType.getNumber()) - number;
                    assetsType.setNumber(newNumber < 0 ? "0" : "" + newNumber);
                    double totalPrice = newNumber * Double.parseDouble(assetsType.getUnitPrice());
                    assetsType.setTotalPrice(totalPrice < 0 ? "0" : "" + totalPrice);
                    double existNumber = Double.parseDouble(assetsType.getExistingNumber()) - number;
                    if(existNumber < 0) return ResultUtil.error(assetsType.getAssetName() + "出库数量大于库存,删除失败!");
                    assetsType.setExistingNumber(existNumber + "");
                    iAssetsTypeServicel.saveOrUpdate(assetsType);
                }
            }
            iWarehousingService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
