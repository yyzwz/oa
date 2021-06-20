package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.ShiChangGuiLei;
import cn.exrick.xboot.modules.crm.entity.ShiChangManage;
import cn.exrick.xboot.modules.crm.service.IShiChangManageService;
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
@Api(description = "市场占有率管理档案管理接口")
@RequestMapping("/xboot/shiChangManage")
@Transactional
public class ShiChangManageController {

    @Autowired
    private IShiChangManageService iShiChangManageService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ShiChangManage> get(@PathVariable Long id){

        ShiChangManage shiChangManage = iShiChangManageService.getById(id);
        return new ResultUtil<ShiChangManage>().setData(shiChangManage);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ShiChangManage>> getAll(){

        List<ShiChangManage> list = iShiChangManageService.list();
        return new ResultUtil<List<ShiChangManage>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ShiChangManage>> getByPage(@ModelAttribute ShiChangManage shiChangManage, @ModelAttribute PageVo page){
        QueryWrapper<ShiChangManage> qw = new QueryWrapper<ShiChangManage>();
        if(StrUtil.isNotBlank(shiChangManage.getBan())) {
            qw.like("ban", shiChangManage.getBan());
        }
        if(StrUtil.isNotBlank(shiChangManage.getNumber())) {
            qw.like("number", shiChangManage.getNumber());
        }
        if(StrUtil.isNotBlank(shiChangManage.getRemark())) {
            qw.like("remark", shiChangManage.getRemark());
        }
        if(StrUtil.isNotBlank(shiChangManage.getTitle())) {
            qw.like("title", shiChangManage.getTitle());
        }
        IPage<ShiChangManage> data = iShiChangManageService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ShiChangManage>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ShiChangManage> saveOrUpdate(ShiChangManage shiChangManage){

        if(iShiChangManageService.saveOrUpdate(shiChangManage)){
            return new ResultUtil<ShiChangManage>().setData(shiChangManage);
        }
        return new ResultUtil<ShiChangManage>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShiChangManageService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
