package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AnnualCost;
import cn.exrick.xboot.modules.crm.entity.ShiChangRent;
import cn.exrick.xboot.modules.crm.service.IAnnualCostService;
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
@Api(description = "人员年费用管理接口")
@RequestMapping("/xboot/annualCost")
@Transactional
public class AnnualCostController {

    @Autowired
    private IAnnualCostService iAnnualCostService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<AnnualCost> get(@PathVariable Long id){

        AnnualCost annualCost = iAnnualCostService.getById(id);
        return new ResultUtil<AnnualCost>().setData(annualCost);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<AnnualCost>> getAll(){

        List<AnnualCost> list = iAnnualCostService.list();
        return new ResultUtil<List<AnnualCost>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<AnnualCost>> getByPage(@ModelAttribute AnnualCost annualCost, @ModelAttribute PageVo page){
        QueryWrapper<AnnualCost> qw = new QueryWrapper<AnnualCost>();
        if(StrUtil.isNotBlank(annualCost.getBan())) {
            qw.like("ban", annualCost.getBan());
        }
        if(StrUtil.isNotBlank(annualCost.getNumber())) {
            qw.like("number", annualCost.getNumber());
        }
        if(StrUtil.isNotBlank(annualCost.getRemark())) {
            qw.like("remark", annualCost.getRemark());
        }
        if(StrUtil.isNotBlank(annualCost.getTitle())) {
            qw.like("title", annualCost.getTitle());
        }
        IPage<AnnualCost> data = iAnnualCostService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<AnnualCost>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<AnnualCost> saveOrUpdate(AnnualCost annualCost){

        if(iAnnualCostService.saveOrUpdate(annualCost)){
            return new ResultUtil<AnnualCost>().setData(annualCost);
        }
        return new ResultUtil<AnnualCost>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iAnnualCostService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
