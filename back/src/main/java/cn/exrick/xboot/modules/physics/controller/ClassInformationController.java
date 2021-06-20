package cn.exrick.xboot.modules.physics.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.physics.entity.ClassInformation;
import cn.exrick.xboot.modules.physics.service.IClassInformationService;
import cn.exrick.xboot.modules.your.entity.WarehouseOut;
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
@Api(description = "班级信息管理接口")
@RequestMapping("/xboot/classInformation")
@Transactional
public class ClassInformationController {

    @Autowired
    private IClassInformationService iClassInformationService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ClassInformation> get(@PathVariable String id){

        ClassInformation classInformation = iClassInformationService.getById(id);
        return new ResultUtil<ClassInformation>().setData(classInformation);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ClassInformation>> getAll(){

        List<ClassInformation> list = iClassInformationService.list();
        return new ResultUtil<List<ClassInformation>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ClassInformation>> getByPage(@ModelAttribute ClassInformation classInformation, @ModelAttribute PageVo page){
        QueryWrapper<ClassInformation> qw = new QueryWrapper<ClassInformation>();
        if(StrUtil.isNotBlank(classInformation.getClassSimpleName())) {
            qw.like("class_simple_name", classInformation.getClassSimpleName());
        }
        if(StrUtil.isNotBlank(classInformation.getClassAllName())) {
            qw.like("class_all_name", classInformation.getClassAllName());
        }
        IPage<ClassInformation> data = iClassInformationService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ClassInformation>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ClassInformation> saveOrUpdate(ClassInformation classInformation){

        if(iClassInformationService.saveOrUpdate(classInformation)){
            return new ResultUtil<ClassInformation>().setData(classInformation);
        }
        return new ResultUtil<ClassInformation>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iClassInformationService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
