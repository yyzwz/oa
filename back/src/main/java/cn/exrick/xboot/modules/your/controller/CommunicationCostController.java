package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.CommunicationCost;
import cn.exrick.xboot.modules.your.entity.Consumables;
import cn.exrick.xboot.modules.your.service.ICommunicationCostService;
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
@Api(description = "通讯费管理接口")
@RequestMapping("/xboot/communicationCost")
@Transactional
public class CommunicationCostController {

    @Autowired
    private ICommunicationCostService iCommunicationCostService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<CommunicationCost> get(@PathVariable String id){

        CommunicationCost communicationCost = iCommunicationCostService.getById(id);
        return new ResultUtil<CommunicationCost>().setData(communicationCost);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<CommunicationCost>> getAll(){

        List<CommunicationCost> list = iCommunicationCostService.list();
        return new ResultUtil<List<CommunicationCost>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<CommunicationCost>> getByPage(@ModelAttribute CommunicationCost communicationCost, @ModelAttribute PageVo page){
        QueryWrapper<CommunicationCost> qw = new QueryWrapper<CommunicationCost>();
        if(StrUtil.isNotBlank(communicationCost.getYear())) {
            qw.eq("year", communicationCost.getYear());
        }
        if(StrUtil.isNotBlank(communicationCost.getMouth())) {
            qw.eq("mouth", communicationCost.getMouth());
        }
        IPage<CommunicationCost> data = iCommunicationCostService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<CommunicationCost>>().setData(data);
    }

    @SystemLog(description = "新增/修改通讯费记录", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<CommunicationCost> saveOrUpdate(CommunicationCost communicationCost){

        if(iCommunicationCostService.saveOrUpdate(communicationCost)){
            return new ResultUtil<CommunicationCost>().setData(communicationCost);
        }
        return new ResultUtil<CommunicationCost>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除通讯费记录", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iCommunicationCostService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
