package cn.exrick.xboot.modules.physics.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.physics.entity.LaboratoryOrder;
import cn.exrick.xboot.modules.physics.service.ILaboratoryOrderService;
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
@Api(description = "实验室预约管理接口")
@RequestMapping("/xboot/laboratoryOrder")
@Transactional
public class LaboratoryOrderController {

    @Autowired
    private ILaboratoryOrderService iLaboratoryOrderService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<LaboratoryOrder> get(@PathVariable String id){

        LaboratoryOrder laboratoryOrder = iLaboratoryOrderService.getById(id);
        return new ResultUtil<LaboratoryOrder>().setData(laboratoryOrder);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<LaboratoryOrder>> getAll(){

        List<LaboratoryOrder> list = iLaboratoryOrderService.list();
        return new ResultUtil<List<LaboratoryOrder>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<LaboratoryOrder>> getByPage(PageVo page){

        IPage<LaboratoryOrder> data = iLaboratoryOrderService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<LaboratoryOrder>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<LaboratoryOrder> saveOrUpdate(LaboratoryOrder laboratoryOrder){

        if(iLaboratoryOrderService.saveOrUpdate(laboratoryOrder)){
            return new ResultUtil<LaboratoryOrder>().setData(laboratoryOrder);
        }
        return new ResultUtil<LaboratoryOrder>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iLaboratoryOrderService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
