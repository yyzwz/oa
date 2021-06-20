package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.ShiChangPinPai;
import cn.exrick.xboot.modules.crm.entity.ShiChangRent;
import cn.exrick.xboot.modules.crm.service.IShiChangRentService;
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
@Api(description = "市场店面租金档案管理接口")
@RequestMapping("/xboot/shiChangRent")
@Transactional
public class ShiChangRentController {

    @Autowired
    private IShiChangRentService iShiChangRentService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ShiChangRent> get(@PathVariable Long id){

        ShiChangRent shiChangRent = iShiChangRentService.getById(id);
        return new ResultUtil<ShiChangRent>().setData(shiChangRent);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ShiChangRent>> getAll(){

        List<ShiChangRent> list = iShiChangRentService.list();
        return new ResultUtil<List<ShiChangRent>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ShiChangRent>> getByPage(@ModelAttribute ShiChangRent shiChangRent, @ModelAttribute PageVo page){
        QueryWrapper<ShiChangRent> qw = new QueryWrapper<ShiChangRent>();
        if(StrUtil.isNotBlank(shiChangRent.getBan())) {
            qw.like("ban", shiChangRent.getBan());
        }
        if(StrUtil.isNotBlank(shiChangRent.getNumber())) {
            qw.like("number", shiChangRent.getNumber());
        }
        if(StrUtil.isNotBlank(shiChangRent.getRemark())) {
            qw.like("remark", shiChangRent.getRemark());
        }
        if(StrUtil.isNotBlank(shiChangRent.getTitle())) {
            qw.like("title", shiChangRent.getTitle());
        }
        IPage<ShiChangRent> data = iShiChangRentService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ShiChangRent>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ShiChangRent> saveOrUpdate(ShiChangRent shiChangRent){

        if(iShiChangRentService.saveOrUpdate(shiChangRent)){
            return new ResultUtil<ShiChangRent>().setData(shiChangRent);
        }
        return new ResultUtil<ShiChangRent>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShiChangRentService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
