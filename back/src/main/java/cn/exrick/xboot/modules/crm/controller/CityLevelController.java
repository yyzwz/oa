package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.CityLevel;
import cn.exrick.xboot.modules.crm.entity.KeHuRoster;
import cn.exrick.xboot.modules.crm.service.ICityLevelService;
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
@Api(description = "城市级别管理接口")
@RequestMapping("/xboot/cityLevel")
@Transactional
public class CityLevelController {

    @Autowired
    private ICityLevelService iCityLevelService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<CityLevel> get(@PathVariable Long id){

        CityLevel cityLevel = iCityLevelService.getById(id);
        return new ResultUtil<CityLevel>().setData(cityLevel);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<CityLevel>> getAll(){
        List<CityLevel> list = iCityLevelService.list();
        return new ResultUtil<List<CityLevel>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<CityLevel>> getByPage(@ModelAttribute CityLevel cityLevel, @ModelAttribute PageVo page){
        QueryWrapper<CityLevel> qw = new QueryWrapper<CityLevel>();
        if(StrUtil.isNotBlank(cityLevel.getBan())) {
            qw.eq("ban", cityLevel.getBan());
        }
        if(StrUtil.isNotBlank(cityLevel.getNumber())) {
            qw.like("number", cityLevel.getNumber());
        }
        if(StrUtil.isNotBlank(cityLevel.getRemark())) {
            qw.like("remark", cityLevel.getRemark());
        }
        if(StrUtil.isNotBlank(cityLevel.getTitle())) {
            qw.like("title", cityLevel.getTitle());
        }
        if(StrUtil.isNotBlank(cityLevel.getWuLiao())) {
            qw.like("wu_liao", cityLevel.getWuLiao());
        }
        IPage<CityLevel> data = iCityLevelService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<CityLevel>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<CityLevel> saveOrUpdate(CityLevel cityLevel){

        if(iCityLevelService.saveOrUpdate(cityLevel)){
            return new ResultUtil<CityLevel>().setData(cityLevel);
        }
        return new ResultUtil<CityLevel>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iCityLevelService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
