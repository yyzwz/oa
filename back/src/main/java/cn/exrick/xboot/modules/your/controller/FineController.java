package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.Fine;
import cn.exrick.xboot.modules.your.entity.WaterCost;
import cn.exrick.xboot.modules.your.service.IFineService;
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
@Api(description = "罚款登记管理接口")
@RequestMapping("/xboot/fine")
@Transactional
public class FineController {

    @Autowired
    private IFineService iFineService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Fine> get(@PathVariable String id){

        Fine fine = iFineService.getById(id);
        return new ResultUtil<Fine>().setData(fine);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Fine>> getAll(){

        List<Fine> list = iFineService.list();
        return new ResultUtil<List<Fine>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Fine>> getByPage(@ModelAttribute Fine fine,@ModelAttribute PageVo page){
        QueryWrapper<Fine> qw = new QueryWrapper<Fine>();
        if(StrUtil.isNotBlank(fine.getPerson())) {
            qw.like("person", fine.getPerson());
        }
        if(StrUtil.isNotBlank(fine.getReason())) {
            qw.like("reason", fine.getReason());
        }
        IPage<Fine> data = iFineService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Fine>>().setData(data);
    }

    @SystemLog(description = "新增/修改罚款记录", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Fine> saveOrUpdate(Fine fine){

        if(iFineService.saveOrUpdate(fine)){
            return new ResultUtil<Fine>().setData(fine);
        }
        return new ResultUtil<Fine>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除罚款记录", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iFineService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
