package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.ShiChangArea;
import cn.exrick.xboot.modules.crm.entity.ShiChangGuiLei;
import cn.exrick.xboot.modules.crm.service.IShiChangGuiLeiService;
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
@Api(description = "市场归类档案管理接口")
@RequestMapping("/xboot/shiChangGuiLei")
@Transactional
public class ShiChangGuiLeiController {

    @Autowired
    private IShiChangGuiLeiService iShiChangGuiLeiService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ShiChangGuiLei> get(@PathVariable Long id){

        ShiChangGuiLei shiChangGuiLei = iShiChangGuiLeiService.getById(id);
        return new ResultUtil<ShiChangGuiLei>().setData(shiChangGuiLei);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ShiChangGuiLei>> getAll(){

        List<ShiChangGuiLei> list = iShiChangGuiLeiService.list();
        return new ResultUtil<List<ShiChangGuiLei>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ShiChangGuiLei>> getByPage(@ModelAttribute ShiChangGuiLei shiChangGuiLei,@ModelAttribute PageVo page){
        QueryWrapper<ShiChangGuiLei> qw = new QueryWrapper<ShiChangGuiLei>();
        if(StrUtil.isNotBlank(shiChangGuiLei.getBan())) {
            qw.like("ban", shiChangGuiLei.getBan());
        }
        if(StrUtil.isNotBlank(shiChangGuiLei.getNumber())) {
            qw.like("number", shiChangGuiLei.getNumber());
        }
        if(StrUtil.isNotBlank(shiChangGuiLei.getRemark())) {
            qw.like("remark", shiChangGuiLei.getRemark());
        }
        if(StrUtil.isNotBlank(shiChangGuiLei.getTitle())) {
            qw.like("title", shiChangGuiLei.getTitle());
        }
        IPage<ShiChangGuiLei> data = iShiChangGuiLeiService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ShiChangGuiLei>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ShiChangGuiLei> saveOrUpdate(ShiChangGuiLei shiChangGuiLei){

        if(iShiChangGuiLeiService.saveOrUpdate(shiChangGuiLei)){
            return new ResultUtil<ShiChangGuiLei>().setData(shiChangGuiLei);
        }
        return new ResultUtil<ShiChangGuiLei>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShiChangGuiLeiService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
