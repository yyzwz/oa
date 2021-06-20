package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AnnualRent;
import cn.exrick.xboot.modules.crm.service.IAnnualRentService;
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
@Api(description = "年租金管理接口")
@RequestMapping("/xboot/annualRent")
@Transactional
public class AnnualRentController {

    @Autowired
    private IAnnualRentService iAnnualRentService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<AnnualRent> get(@PathVariable Long id){

        AnnualRent annualRent = iAnnualRentService.getById(id);
        return new ResultUtil<AnnualRent>().setData(annualRent);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<AnnualRent>> getAll(){

        List<AnnualRent> list = iAnnualRentService.list();
        return new ResultUtil<List<AnnualRent>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<AnnualRent>> getByPage(@ModelAttribute AnnualRent annualRent, @ModelAttribute PageVo page){
        QueryWrapper<AnnualRent> qw = new QueryWrapper<AnnualRent>();
        if(StrUtil.isNotBlank(annualRent.getBan())) {
            qw.like("ban", annualRent.getBan());
        }
        if(StrUtil.isNotBlank(annualRent.getNumber())) {
            qw.like("number", annualRent.getNumber());
        }
        if(StrUtil.isNotBlank(annualRent.getRemark())) {
            qw.like("remark", annualRent.getRemark());
        }
        if(StrUtil.isNotBlank(annualRent.getTitle())) {
            qw.like("title", annualRent.getTitle());
        }
        IPage<AnnualRent> data = iAnnualRentService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<AnnualRent>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<AnnualRent> saveOrUpdate(AnnualRent annualRent){

        if(iAnnualRentService.saveOrUpdate(annualRent)){
            return new ResultUtil<AnnualRent>().setData(annualRent);
        }
        return new ResultUtil<AnnualRent>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iAnnualRentService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
