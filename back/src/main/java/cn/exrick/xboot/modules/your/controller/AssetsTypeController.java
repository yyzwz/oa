package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.AssetsType;
import cn.exrick.xboot.modules.your.entity.Warehousing;
import cn.exrick.xboot.modules.your.service.IAssetsTypeService;
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
@Api(description = "资产种类管理接口")
@RequestMapping("/xboot/assetsType")
@Transactional
public class AssetsTypeController {

    @Autowired
    private IAssetsTypeService iAssetsTypeService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<AssetsType> get(@PathVariable String id){

        AssetsType assetsType = iAssetsTypeService.getById(id);
        return new ResultUtil<AssetsType>().setData(assetsType);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<AssetsType>> getAll(){

        List<AssetsType> list = iAssetsTypeService.list();
        return new ResultUtil<List<AssetsType>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<AssetsType>> getByPage(@ModelAttribute AssetsType assetsType, @ModelAttribute PageVo page,@RequestParam(required = false) String natureType){
        QueryWrapper<AssetsType> qw = new QueryWrapper<AssetsType>();
        if(StrUtil.isNotBlank(natureType)) {
            if(natureType.equals("1")){
                qw.eq("nature", "固定资产");
            }else{
                qw.eq("nature", "耗材");
            }
        }
        if(StrUtil.isNotBlank(assetsType.getNature())) {
            qw.eq("nature", assetsType.getNature());
        }
        if(StrUtil.isNotBlank(assetsType.getAssetName())) {
            qw.like("asset_name", assetsType.getAssetName());
        }
        IPage<AssetsType> data = iAssetsTypeService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<AssetsType>>().setData(data);
    }

    @SystemLog(description = "新增/修改资产种类", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<AssetsType> saveOrUpdate(AssetsType assetsType){
        if(assetsType.getNumber() == null){
            assetsType.setNumber("0");
            assetsType.setTotalPrice("0");
        }
        if(assetsType.getExistingNumber() == null){
            assetsType.setExistingNumber("0");
        }
        if(iAssetsTypeService.saveOrUpdate(assetsType)){
            return new ResultUtil<AssetsType>().setData(assetsType);
        }
        return new ResultUtil<AssetsType>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除资产种类", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iAssetsTypeService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
