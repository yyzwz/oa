package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.entity.CountyType;
import cn.exrick.xboot.modules.crm.service.ICountyTypeService;
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
@Api(description = "县级类型管理接口")
@RequestMapping("/xboot/countyType")
@Transactional
public class CountyTypeController {

    @Autowired
    private ICountyTypeService iCountyTypeService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<CountyType> get(@PathVariable Long id){

        CountyType countyType = iCountyTypeService.getById(id);
        return new ResultUtil<CountyType>().setData(countyType);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<CountyType>> getAll(){

        List<CountyType> list = iCountyTypeService.list();
        return new ResultUtil<List<CountyType>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<CountyType>> getByPage(@ModelAttribute CountyType countyType, @ModelAttribute PageVo page){
        QueryWrapper<CountyType> qw = new QueryWrapper<CountyType>();
        if(StrUtil.isNotBlank(countyType.getBan())) {
            qw.eq("ban", countyType.getBan());
        }
        if(StrUtil.isNotBlank(countyType.getNumber())) {
            qw.like("number", countyType.getNumber());
        }
        if(StrUtil.isNotBlank(countyType.getRemark())) {
            qw.like("remark", countyType.getRemark());
        }
        if(StrUtil.isNotBlank(countyType.getTitle())) {
            qw.like("title", countyType.getTitle());
        }
        IPage<CountyType> data = iCountyTypeService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<CountyType>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<CountyType> saveOrUpdate(CountyType countyType){

        if(iCountyTypeService.saveOrUpdate(countyType)){
            return new ResultUtil<CountyType>().setData(countyType);
        }
        return new ResultUtil<CountyType>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iCountyTypeService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
