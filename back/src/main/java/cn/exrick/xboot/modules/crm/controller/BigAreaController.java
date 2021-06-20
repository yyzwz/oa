package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.entity.BigArea;
import cn.exrick.xboot.modules.crm.service.IBigAreaService;
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
@Api(description = "大区定义管理接口")
@RequestMapping("/xboot/bigArea")
@Transactional
public class BigAreaController {

    @Autowired
    private IBigAreaService iBigAreaService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<BigArea> get(@PathVariable Long id){

        BigArea bigArea = iBigAreaService.getById(id);
        return new ResultUtil<BigArea>().setData(bigArea);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<BigArea>> getAll(){

        List<BigArea> list = iBigAreaService.list();
        return new ResultUtil<List<BigArea>>().setData(list);
    }

    @RequestMapping(value = "/getByFenGongSi", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<BigArea>> getByFenGongSi(@RequestParam String title){
        QueryWrapper<BigArea> qw = new QueryWrapper<BigArea>();
        if(StrUtil.isNotBlank(title)) {
            qw.eq("son_company", title);
        }
        List<BigArea> list = iBigAreaService.list(qw);
        return new ResultUtil<List<BigArea>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<BigArea>> getByPage(@ModelAttribute BigArea bigArea, @ModelAttribute PageVo page){
        QueryWrapper<BigArea> qw = new QueryWrapper<BigArea>();
        if(StrUtil.isNotBlank(bigArea.getBan())) {
            qw.eq("ban", bigArea.getBan());
        }
        if(StrUtil.isNotBlank(bigArea.getDingId())) {
            qw.like("ding_id", bigArea.getDingId());
        }
        if(StrUtil.isNotBlank(bigArea.getNumber())) {
            qw.like("number", bigArea.getNumber());
        }
        if(StrUtil.isNotBlank(bigArea.getRemark())) {
            qw.like("remark", bigArea.getRemark());
        }
        if(StrUtil.isNotBlank(bigArea.getSonCompany())) {
            qw.like("son_company", bigArea.getSonCompany());
        }
        if(StrUtil.isNotBlank(bigArea.getTitle())) {
            qw.like("title", bigArea.getTitle());
        }
        IPage<BigArea> data = iBigAreaService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<BigArea>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<BigArea> saveOrUpdate(BigArea bigArea){

        if(iBigAreaService.saveOrUpdate(bigArea)){
            return new ResultUtil<BigArea>().setData(bigArea);
        }
        return new ResultUtil<BigArea>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iBigAreaService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
