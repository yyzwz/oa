package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.DianMian;
import cn.exrick.xboot.modules.crm.entity.KeHuSellType;
import cn.exrick.xboot.modules.crm.service.IDianMianService;
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
@Api(description = "店面维护管理接口")
@RequestMapping("/xboot/dianMian")
@Transactional
public class DianMianController {

    @Autowired
    private IDianMianService iDianMianService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DianMian> get(@PathVariable Long id){

        DianMian dianMian = iDianMianService.getById(id);
        return new ResultUtil<DianMian>().setData(dianMian);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DianMian>> getAll(){

        List<DianMian> list = iDianMianService.list();
        return new ResultUtil<List<DianMian>>().setData(list);
    }


    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DianMian>> getByPage(@ModelAttribute DianMian dianMian, @ModelAttribute PageVo page){
        QueryWrapper<DianMian> qw = new QueryWrapper<DianMian>();
        if(StrUtil.isNotBlank(dianMian.getAddress())) {
            qw.like("address", dianMian.getAddress());
        }
        if(StrUtil.isNotBlank(dianMian.getArea())) {
            qw.like("area", dianMian.getArea());
        }
        if(StrUtil.isNotBlank(dianMian.getBigArea())) {
            qw.like("big_area", dianMian.getBigArea());
        }
        if(StrUtil.isNotBlank(dianMian.getCity())) {
            qw.like("city", dianMian.getCity());
        }
        if(StrUtil.isNotBlank(dianMian.getDianName())) {
            qw.like("dian_name", dianMian.getDianName());
        }
        if(StrUtil.isNotBlank(dianMian.getFaPeople())) {
            qw.like("fa_people", dianMian.getFaPeople());
        }
        if(StrUtil.isNotBlank(dianMian.getFuZe())) {
            qw.like("fu_ze", dianMian.getFuZe());
        }
        if(StrUtil.isNotBlank(dianMian.getManager())) {
            qw.like("manager", dianMian.getManager());
        }
        if(StrUtil.isNotBlank(dianMian.getSheng())) {
            qw.like("sheng", dianMian.getSheng());
        }
        if(StrUtil.isNotBlank(dianMian.getSonCompany())) {
            qw.like("son_company", dianMian.getSonCompany());
        }
        if(StrUtil.isNotBlank(dianMian.getXian())) {
            qw.like("xian", dianMian.getXian());
        }
        IPage<DianMian> data = iDianMianService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<DianMian>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DianMian> saveOrUpdate(DianMian dianMian){

        if(iDianMianService.saveOrUpdate(dianMian)){
            return new ResultUtil<DianMian>().setData(dianMian);
        }
        return new ResultUtil<DianMian>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iDianMianService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
