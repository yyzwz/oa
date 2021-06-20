package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.entity.SellArea;
import cn.exrick.xboot.modules.crm.service.ISellAreaService;
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
@Api(description = "销售区域管理管理接口")
@RequestMapping("/xboot/sellArea")
@Transactional
public class SellAreaController {

    @Autowired
    private ISellAreaService iSellAreaService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<SellArea> get(@PathVariable Long id){

        SellArea sellArea = iSellAreaService.getById(id);
        return new ResultUtil<SellArea>().setData(sellArea);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<SellArea>> getAll(){

        List<SellArea> list = iSellAreaService.list();
        return new ResultUtil<List<SellArea>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<SellArea>> getByPage(@ModelAttribute SellArea sellArea, @ModelAttribute PageVo page){
        QueryWrapper<SellArea> qw = new QueryWrapper<SellArea>();
        if(StrUtil.isNotBlank(sellArea.getBan())) {
            qw.eq("ban", sellArea.getBan());
        }
        if(StrUtil.isNotBlank(sellArea.getNumber())) {
            qw.like("number", sellArea.getNumber());
        }
        if(StrUtil.isNotBlank(sellArea.getRemark())) {
            qw.like("remark", sellArea.getRemark());
        }
        if(StrUtil.isNotBlank(sellArea.getSellThing())) {
            qw.like("sell_thing", sellArea.getSellThing());
        }
        if(StrUtil.isNotBlank(sellArea.getArea())) {
            qw.like("area", sellArea.getArea());
        }
        IPage<SellArea> data = iSellAreaService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<SellArea>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<SellArea> saveOrUpdate(SellArea sellArea){

        if(iSellAreaService.saveOrUpdate(sellArea)){
            return new ResultUtil<SellArea>().setData(sellArea);
        }
        return new ResultUtil<SellArea>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iSellAreaService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
