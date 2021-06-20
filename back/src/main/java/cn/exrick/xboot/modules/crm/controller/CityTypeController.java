package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.CityLevel;
import cn.exrick.xboot.modules.crm.entity.CityType;
import cn.exrick.xboot.modules.crm.service.ICityTypeService;
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
@Api(description = "城市类型管理接口")
@RequestMapping("/xboot/cityType")
@Transactional
public class CityTypeController {

    @Autowired
    private ICityTypeService iCityTypeService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<CityType> get(@PathVariable Long id){

        CityType cityType = iCityTypeService.getById(id);
        return new ResultUtil<CityType>().setData(cityType);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<CityType>> getAll(){

        List<CityType> list = iCityTypeService.list();
        return new ResultUtil<List<CityType>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<CityType>> getByPage(@ModelAttribute CityType cityType, @ModelAttribute PageVo page){
        QueryWrapper<CityType> qw = new QueryWrapper<CityType>();
        if(StrUtil.isNotBlank(cityType.getBan())) {
            qw.eq("ban", cityType.getBan());
        }
        if(StrUtil.isNotBlank(cityType.getNumber())) {
            qw.like("number", cityType.getNumber());
        }
        if(StrUtil.isNotBlank(cityType.getRemark())) {
            qw.like("remark", cityType.getRemark());
        }
        if(StrUtil.isNotBlank(cityType.getTitle())) {
            qw.like("title", cityType.getTitle());
        }
        IPage<CityType> data = iCityTypeService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<CityType>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<CityType> saveOrUpdate(CityType cityType){

        if(iCityTypeService.saveOrUpdate(cityType)){
            return new ResultUtil<CityType>().setData(cityType);
        }
        return new ResultUtil<CityType>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iCityTypeService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
