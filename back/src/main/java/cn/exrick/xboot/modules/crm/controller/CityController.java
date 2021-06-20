package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.entity.City;
import cn.exrick.xboot.modules.crm.service.ICityService;
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
@Api(description = "省市区档案管理接口")
@RequestMapping("/xboot/city")
@Transactional
public class CityController {

    @Autowired
    private ICityService iCityService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<City> get(@PathVariable Long id){

        City city = iCityService.getById(id);
        return new ResultUtil<City>().setData(city);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<City>> getAll(){

        List<City> list = iCityService.list();
        return new ResultUtil<List<City>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<City>> getByPage(@ModelAttribute City city, @ModelAttribute PageVo page){
        QueryWrapper<City> qw = new QueryWrapper<City>();
        if(StrUtil.isNotBlank(city.getBan())) {
            qw.eq("ban", city.getBan());
        }
        if(StrUtil.isNotBlank(city.getNumber())) {
            qw.like("number", city.getNumber());
        }
        if(StrUtil.isNotBlank(city.getCityLevel())) {
            qw.like("city_level", city.getCityLevel());
        }
        if(StrUtil.isNotBlank(city.getCityType())) {
            qw.like("city_type", city.getCityType());
        }
        if(StrUtil.isNotBlank(city.getRemark())) {
            qw.like("remark", city.getRemark());
        }
        if(StrUtil.isNotBlank(city.getSheng())) {
            qw.like("sheng", city.getSheng());
        }
        if(StrUtil.isNotBlank(city.getTitle())) {
            qw.like("title", city.getTitle());
        }
        IPage<City> data = iCityService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<City>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<City> saveOrUpdate(City city){

        if(iCityService.saveOrUpdate(city)){
            return new ResultUtil<City>().setData(city);
        }
        return new ResultUtil<City>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iCityService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
