package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.ShiChangRent;
import cn.exrick.xboot.modules.crm.entity.ShiChangType;
import cn.exrick.xboot.modules.crm.service.IShiChangTypeService;
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
@Api(description = "市场类型档案管理接口")
@RequestMapping("/xboot/shiChangType")
@Transactional
public class ShiChangTypeController {

    @Autowired
    private IShiChangTypeService iShiChangTypeService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ShiChangType> get(@PathVariable Long id){

        ShiChangType shiChangType = iShiChangTypeService.getById(id);
        return new ResultUtil<ShiChangType>().setData(shiChangType);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ShiChangType>> getAll(){

        List<ShiChangType> list = iShiChangTypeService.list();
        return new ResultUtil<List<ShiChangType>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ShiChangType>> getByPage(@ModelAttribute ShiChangType shiChangType, @ModelAttribute PageVo page){
        QueryWrapper<ShiChangType> qw = new QueryWrapper<ShiChangType>();
        if(StrUtil.isNotBlank(shiChangType.getBan())) {
            qw.like("ban", shiChangType.getBan());
        }
        if(StrUtil.isNotBlank(shiChangType.getNumber())) {
            qw.like("number", shiChangType.getNumber());
        }
        if(StrUtil.isNotBlank(shiChangType.getRemark())) {
            qw.like("remark", shiChangType.getRemark());
        }
        if(StrUtil.isNotBlank(shiChangType.getTitle())) {
            qw.like("title", shiChangType.getTitle());
        }
        IPage<ShiChangType> data = iShiChangTypeService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ShiChangType>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ShiChangType> saveOrUpdate(ShiChangType shiChangType){

        if(iShiChangTypeService.saveOrUpdate(shiChangType)){
            return new ResultUtil<ShiChangType>().setData(shiChangType);
        }
        return new ResultUtil<ShiChangType>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShiChangTypeService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
