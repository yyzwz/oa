package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.entity.ShiChangArea;
import cn.exrick.xboot.modules.crm.service.IShiChangAreaService;
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
@Api(description = "市场区域种类档案管理接口")
@RequestMapping("/xboot/shiChangArea")
@Transactional
public class ShiChangAreaController {

    @Autowired
    private IShiChangAreaService iShiChangAreaService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ShiChangArea> get(@PathVariable Long id){

        ShiChangArea shiChangArea = iShiChangAreaService.getById(id);
        return new ResultUtil<ShiChangArea>().setData(shiChangArea);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ShiChangArea>> getAll(){

        List<ShiChangArea> list = iShiChangAreaService.list();
        return new ResultUtil<List<ShiChangArea>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ShiChangArea>> getByPage(@ModelAttribute ShiChangArea shiChangArea, @ModelAttribute PageVo page){
        QueryWrapper<ShiChangArea> qw = new QueryWrapper<ShiChangArea>();
        if(StrUtil.isNotBlank(shiChangArea.getBan())) {
            qw.like("ban", shiChangArea.getBan());
        }
        if(StrUtil.isNotBlank(shiChangArea.getNumber())) {
            qw.like("number", shiChangArea.getNumber());
        }
        if(StrUtil.isNotBlank(shiChangArea.getRemark())) {
            qw.like("remark", shiChangArea.getRemark());
        }
        if(StrUtil.isNotBlank(shiChangArea.getTitle())) {
            qw.like("title", shiChangArea.getTitle());
        }
        IPage<ShiChangArea> data = iShiChangAreaService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ShiChangArea>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ShiChangArea> saveOrUpdate(ShiChangArea shiChangArea){

        if(iShiChangAreaService.saveOrUpdate(shiChangArea)){
            return new ResultUtil<ShiChangArea>().setData(shiChangArea);
        }
        return new ResultUtil<ShiChangArea>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShiChangAreaService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
