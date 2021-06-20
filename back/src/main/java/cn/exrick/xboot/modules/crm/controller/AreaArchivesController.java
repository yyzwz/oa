package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.entity.KeShangZiZhi;
import cn.exrick.xboot.modules.crm.service.IAreaArchivesService;
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
@Api(description = "区域档案管理接口")
@RequestMapping("/xboot/areaArchives")
@Transactional
public class AreaArchivesController {

    @Autowired
    private IAreaArchivesService iAreaArchivesService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<AreaArchives> get(@PathVariable Long id){

        AreaArchives areaArchives = iAreaArchivesService.getById(id);
        return new ResultUtil<AreaArchives>().setData(areaArchives);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<AreaArchives>> getAll(){

        List<AreaArchives> list = iAreaArchivesService.list();
        return new ResultUtil<List<AreaArchives>>().setData(list);
    }

    @RequestMapping(value = "/getAllArea", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部区域")
    public Result<List<String>> getAllArea(){
        List<String> list = iAreaArchivesService.findAllArea();
        return new ResultUtil<List<String>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<AreaArchives>> getByPage(@ModelAttribute AreaArchives areaArchives, @ModelAttribute PageVo page){
        QueryWrapper<AreaArchives> qw = new QueryWrapper<AreaArchives>();
        if(StrUtil.isNotBlank(areaArchives.getBan())) {
            qw.eq("ban", areaArchives.getBan());
        }
        if(StrUtil.isNotBlank(areaArchives.getNumber())) {
            qw.like("number", areaArchives.getNumber());
        }
        if(StrUtil.isNotBlank(areaArchives.getRemark())) {
            qw.like("remark", areaArchives.getRemark());
        }
        if(StrUtil.isNotBlank(areaArchives.getTitle())) {
            qw.like("title", areaArchives.getTitle());
        }
        if(StrUtil.isNotBlank(areaArchives.getBigArea())) {
            qw.like("big_area", areaArchives.getBigArea());
        }
        if(StrUtil.isNotBlank(areaArchives.getSonCompany())) {
            qw.like("son_company", areaArchives.getSonCompany());
        }
        IPage<AreaArchives> data = iAreaArchivesService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<AreaArchives>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<AreaArchives> saveOrUpdate(AreaArchives areaArchives){

        if(iAreaArchivesService.saveOrUpdate(areaArchives)){
            return new ResultUtil<AreaArchives>().setData(areaArchives);
        }
        return new ResultUtil<AreaArchives>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iAreaArchivesService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
