package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.OneMianJi;
import cn.exrick.xboot.modules.crm.entity.ShiChangPosition;
import cn.exrick.xboot.modules.crm.service.IShiChangPositionService;
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
@Api(description = "市场位置管理接口")
@RequestMapping("/xboot/shiChangPosition")
@Transactional
public class ShiChangPositionController {

    @Autowired
    private IShiChangPositionService iShiChangPositionService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ShiChangPosition> get(@PathVariable Long id){

        ShiChangPosition shiChangPosition = iShiChangPositionService.getById(id);
        return new ResultUtil<ShiChangPosition>().setData(shiChangPosition);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ShiChangPosition>> getAll(){

        List<ShiChangPosition> list = iShiChangPositionService.list();
        return new ResultUtil<List<ShiChangPosition>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ShiChangPosition>> getByPage(@ModelAttribute ShiChangPosition shiChangPosition, @ModelAttribute PageVo page){
        QueryWrapper<ShiChangPosition> qw = new QueryWrapper<ShiChangPosition>();
        if(StrUtil.isNotBlank(shiChangPosition.getBan())) {
            qw.like("ban", shiChangPosition.getBan());
        }
        if(StrUtil.isNotBlank(shiChangPosition.getNumber())) {
            qw.like("number", shiChangPosition.getNumber());
        }
        if(StrUtil.isNotBlank(shiChangPosition.getRemark())) {
            qw.like("remark", shiChangPosition.getRemark());
        }
        if(StrUtil.isNotBlank(shiChangPosition.getTitle())) {
            qw.like("title", shiChangPosition.getTitle());
        }
        IPage<ShiChangPosition> data = iShiChangPositionService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ShiChangPosition>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ShiChangPosition> saveOrUpdate(ShiChangPosition shiChangPosition){

        if(iShiChangPositionService.saveOrUpdate(shiChangPosition)){
            return new ResultUtil<ShiChangPosition>().setData(shiChangPosition);
        }
        return new ResultUtil<ShiChangPosition>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShiChangPositionService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
