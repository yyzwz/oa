package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.OneMianJi;
import cn.exrick.xboot.modules.crm.entity.ShangChangPosition;
import cn.exrick.xboot.modules.crm.service.IShangChangPositionService;
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
@Api(description = "商场位置管理接口")
@RequestMapping("/xboot/shangChangPosition")
@Transactional
public class ShangChangPositionController {

    @Autowired
    private IShangChangPositionService iShangChangPositionService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ShangChangPosition> get(@PathVariable Long id){

        ShangChangPosition shangChangPosition = iShangChangPositionService.getById(id);
        return new ResultUtil<ShangChangPosition>().setData(shangChangPosition);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ShangChangPosition>> getAll(){

        List<ShangChangPosition> list = iShangChangPositionService.list();
        return new ResultUtil<List<ShangChangPosition>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ShangChangPosition>> getByPage(@ModelAttribute ShangChangPosition shangChangPosition, @ModelAttribute PageVo page){
        QueryWrapper<ShangChangPosition> qw = new QueryWrapper<ShangChangPosition>();
        if(StrUtil.isNotBlank(shangChangPosition.getBan())) {
            qw.like("ban", shangChangPosition.getBan());
        }
        if(StrUtil.isNotBlank(shangChangPosition.getNumber())) {
            qw.like("number", shangChangPosition.getNumber());
        }
        if(StrUtil.isNotBlank(shangChangPosition.getRemark())) {
            qw.like("remark", shangChangPosition.getRemark());
        }
        if(StrUtil.isNotBlank(shangChangPosition.getTitle())) {
            qw.like("title", shangChangPosition.getTitle());
        }
        IPage<ShangChangPosition> data = iShangChangPositionService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ShangChangPosition>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ShangChangPosition> saveOrUpdate(ShangChangPosition shangChangPosition){

        if(iShangChangPositionService.saveOrUpdate(shangChangPosition)){
            return new ResultUtil<ShangChangPosition>().setData(shangChangPosition);
        }
        return new ResultUtil<ShangChangPosition>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShangChangPositionService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
