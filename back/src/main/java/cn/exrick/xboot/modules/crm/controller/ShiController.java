package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.entity.City;
import cn.exrick.xboot.modules.crm.entity.Shi;
import cn.exrick.xboot.modules.crm.service.IShiService;
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
@Api(description = "城市定义管理接口")
@RequestMapping("/xboot/shi")
@Transactional
public class ShiController {

    @Autowired
    private IShiService iShiService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Shi> get(@PathVariable Long id){

        Shi shi = iShiService.getById(id);
        return new ResultUtil<Shi>().setData(shi);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Shi>> getAll(){

        List<Shi> list = iShiService.list();
        return new ResultUtil<List<Shi>>().setData(list);
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Shi>> findByName(@RequestParam(required = false) String sheng,@RequestParam(required = false) String shi){
        QueryWrapper<Shi> qw = new QueryWrapper<Shi>();
        if(StrUtil.isNotBlank(sheng)) {
            qw.eq("sheng", sheng);
        }
        if(StrUtil.isNotBlank(shi)) {
            qw.eq("title", shi);
        }
        List<Shi> list = iShiService.list(qw);
        return new ResultUtil<List<Shi>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Shi>> getByPage(@ModelAttribute Shi shi, @ModelAttribute PageVo page){
        QueryWrapper<Shi> qw = new QueryWrapper<Shi>();
        if(StrUtil.isNotBlank(shi.getBan())) {
            qw.eq("ban", shi.getBan());
        }
        if(StrUtil.isNotBlank(shi.getNumber())) {
            qw.like("number", shi.getNumber());
        }
        if(StrUtil.isNotBlank(shi.getCityLevel())) {
            qw.like("city_level", shi.getCityLevel());
        }
        if(StrUtil.isNotBlank(shi.getCityType())) {
            qw.like("city_type", shi.getCityType());
        }
        if(StrUtil.isNotBlank(shi.getRemark())) {
            qw.like("remark", shi.getRemark());
        }
        if(StrUtil.isNotBlank(shi.getSheng())) {
            qw.like("sheng", shi.getSheng());
        }
        if(StrUtil.isNotBlank(shi.getTitle())) {
            qw.like("title", shi.getTitle());
        }
        IPage<Shi> data = iShiService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Shi>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Shi> saveOrUpdate(Shi shi){

        if(iShiService.saveOrUpdate(shi)){
            return new ResultUtil<Shi>().setData(shi);
        }
        return new ResultUtil<Shi>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShiService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
