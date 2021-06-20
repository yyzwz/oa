package cn.exrick.xboot.modules.physics.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.physics.entity.ClassInformation;
import cn.exrick.xboot.modules.physics.entity.LaboratoryInformation;
import cn.exrick.xboot.modules.physics.service.ILaboratoryInformationService;
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
@Api(description = "实验室信息管理接口")
@RequestMapping("/xboot/laboratoryInformation")
@Transactional
public class LaboratoryInformationController {

    @Autowired
    private ILaboratoryInformationService iLaboratoryInformationService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<LaboratoryInformation> get(@PathVariable String id){

        LaboratoryInformation laboratoryInformation = iLaboratoryInformationService.getById(id);
        return new ResultUtil<LaboratoryInformation>().setData(laboratoryInformation);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<LaboratoryInformation>> getAll(){

        List<LaboratoryInformation> list = iLaboratoryInformationService.list();
        return new ResultUtil<List<LaboratoryInformation>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<LaboratoryInformation>> getByPage(@ModelAttribute LaboratoryInformation laboratoryInformation, @ModelAttribute PageVo page){
        QueryWrapper<LaboratoryInformation> qw = new QueryWrapper<LaboratoryInformation>();
        if(StrUtil.isNotBlank(laboratoryInformation.getName())) {
            qw.like("name", laboratoryInformation.getName());
        }
        IPage<LaboratoryInformation> data = iLaboratoryInformationService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<LaboratoryInformation>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<LaboratoryInformation> saveOrUpdate(LaboratoryInformation laboratoryInformation){

        if(iLaboratoryInformationService.saveOrUpdate(laboratoryInformation)){
            return new ResultUtil<LaboratoryInformation>().setData(laboratoryInformation);
        }
        return new ResultUtil<LaboratoryInformation>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iLaboratoryInformationService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
