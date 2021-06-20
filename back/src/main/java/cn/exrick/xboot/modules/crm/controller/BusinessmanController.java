package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.Businessman;
import cn.exrick.xboot.modules.crm.entity.ShiChangRent;
import cn.exrick.xboot.modules.crm.service.IBusinessmanService;
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
@Api(description = "经销商人员档案管理接口")
@RequestMapping("/xboot/businessman")
@Transactional
public class BusinessmanController {

    @Autowired
    private IBusinessmanService iBusinessmanService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Businessman> get(@PathVariable Long id){

        Businessman businessman = iBusinessmanService.getById(id);
        return new ResultUtil<Businessman>().setData(businessman);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Businessman>> getAll(){

        List<Businessman> list = iBusinessmanService.list();
        return new ResultUtil<List<Businessman>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Businessman>> getByPage(@ModelAttribute Businessman businessman, @ModelAttribute PageVo page){
        QueryWrapper<Businessman> qw = new QueryWrapper<Businessman>();
        if(StrUtil.isNotBlank(businessman.getBan())) {
            qw.like("ban", businessman.getBan());
        }
        if(StrUtil.isNotBlank(businessman.getNumber())) {
            qw.like("number", businessman.getNumber());
        }
        if(StrUtil.isNotBlank(businessman.getRemark())) {
            qw.like("remark", businessman.getRemark());
        }
        if(StrUtil.isNotBlank(businessman.getMobile())) {
            qw.like("mobile", businessman.getMobile());
        }
        if(StrUtil.isNotBlank(businessman.getBirthday())) {
            qw.like("birthday", businessman.getBirthday());
        }
        if(StrUtil.isNotBlank(businessman.getMajor())) {
            qw.like("major", businessman.getMajor());
        }
        if(StrUtil.isNotBlank(businessman.getName())) {
            qw.like("name", businessman.getName());
        }
        if(StrUtil.isNotBlank(businessman.getSchool())) {
            qw.like("school", businessman.getSchool());
        }
        if(StrUtil.isNotBlank(businessman.getSchoolLevel())) {
            qw.like("school_level", businessman.getSchoolLevel());
        }
        if(StrUtil.isNotBlank(businessman.getSex())) {
            qw.like("sex", businessman.getSex());
        }
        IPage<Businessman> data = iBusinessmanService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Businessman>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Businessman> saveOrUpdate(Businessman businessman){

        if(iBusinessmanService.saveOrUpdate(businessman)){
            return new ResultUtil<Businessman>().setData(businessman);
        }
        return new ResultUtil<Businessman>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iBusinessmanService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
