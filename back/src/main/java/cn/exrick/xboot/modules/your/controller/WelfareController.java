package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.Welfare;
import cn.exrick.xboot.modules.your.service.IWelfareService;
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
@Api(description = "岗级福利管理接口")
@RequestMapping("/xboot/welfare")
@Transactional
public class WelfareController {

    @Autowired
    private IWelfareService iWelfareService;

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Welfare> get(@RequestParam String id){

        Welfare welfare = iWelfareService.findByIdZwz(id);
        return new ResultUtil<Welfare>().setData(welfare);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Welfare>> getAll(){

        List<Welfare> list = iWelfareService.list();
        return new ResultUtil<List<Welfare>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Welfare>> getByPage(PageVo page){

        IPage<Welfare> data = iWelfareService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<Welfare>>().setData(data);
    }

    @SystemLog(description = "新增/修改岗级", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Welfare> saveOrUpdate(Welfare welfare){

        if(iWelfareService.saveOrUpdate(welfare)){
            return new ResultUtil<Welfare>().setData(welfare);
        }
        return new ResultUtil<Welfare>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除岗级", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iWelfareService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
