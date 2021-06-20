package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.DutyPeople;
import cn.exrick.xboot.modules.crm.entity.ShiChangRent;
import cn.exrick.xboot.modules.crm.service.IDutyPeopleService;
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
@Api(description = "负责人档案管理接口")
@RequestMapping("/xboot/dutyPeople")
@Transactional
public class DutyPeopleController {

    @Autowired
    private IDutyPeopleService iDutyPeopleService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DutyPeople> get(@PathVariable Long id){

        DutyPeople dutyPeople = iDutyPeopleService.getById(id);
        return new ResultUtil<DutyPeople>().setData(dutyPeople);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DutyPeople>> getAll(){

        List<DutyPeople> list = iDutyPeopleService.list();
        return new ResultUtil<List<DutyPeople>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DutyPeople>> getByPage(@ModelAttribute DutyPeople dutyPeople, @ModelAttribute PageVo page){
        QueryWrapper<DutyPeople> qw = new QueryWrapper<DutyPeople>();
        if(StrUtil.isNotBlank(dutyPeople.getBan())) {
            qw.like("ban", dutyPeople.getBan());
        }
        if(StrUtil.isNotBlank(dutyPeople.getNumber())) {
            qw.like("number", dutyPeople.getNumber());
        }
        if(StrUtil.isNotBlank(dutyPeople.getRemark())) {
            qw.like("remark", dutyPeople.getRemark());
        }
        if(StrUtil.isNotBlank(dutyPeople.getName())) {
            qw.like("name", dutyPeople.getName());
        }
        if(StrUtil.isNotBlank(dutyPeople.getMobile())) {
            qw.like("mobile", dutyPeople.getMobile());
        }
        if(StrUtil.isNotBlank(dutyPeople.getSex())) {
            qw.like("sex", dutyPeople.getSex());
        }
        if(StrUtil.isNotBlank(dutyPeople.getBirthday())) {
            qw.like("birthday", dutyPeople.getBirthday());
        }
        if(StrUtil.isNotBlank(dutyPeople.getMajor())) {
            qw.like("major", dutyPeople.getMajor());
        }
        if(StrUtil.isNotBlank(dutyPeople.getSchool())) {
            qw.like("school", dutyPeople.getSchool());
        }
        if(StrUtil.isNotBlank(dutyPeople.getSchoolLevel())) {
            qw.like("school_level", dutyPeople.getSchoolLevel());
        }
        IPage<DutyPeople> data = iDutyPeopleService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<DutyPeople>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DutyPeople> saveOrUpdate(DutyPeople dutyPeople){

        if(iDutyPeopleService.saveOrUpdate(dutyPeople)){
            return new ResultUtil<DutyPeople>().setData(dutyPeople);
        }
        return new ResultUtil<DutyPeople>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iDutyPeopleService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
