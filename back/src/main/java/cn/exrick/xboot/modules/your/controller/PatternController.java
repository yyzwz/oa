package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.Insurance;
import cn.exrick.xboot.modules.your.entity.Pattern;
import cn.exrick.xboot.modules.your.service.IPatternService;
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
@Api(description = "薪资模式管理接口")
@RequestMapping("/xboot/pattern")
@Transactional
public class PatternController {

    @Autowired
    private IPatternService iPatternService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Pattern> get(@PathVariable String id){

        Pattern pattern = iPatternService.getById(id);
        return new ResultUtil<Pattern>().setData(pattern);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Pattern>> getAll(){

        List<Pattern> list = iPatternService.list();
        return new ResultUtil<List<Pattern>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Pattern>> getByPage(@ModelAttribute Pattern pattern,@ModelAttribute PageVo page){
        QueryWrapper<Pattern> qw = new QueryWrapper<Pattern>();
        if(StrUtil.isNotBlank(pattern.getPatternName())) {
            qw.like("pattern_name", pattern.getPatternName());
        }
        IPage<Pattern> data = iPatternService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Pattern>>().setData(data);
    }

    @SystemLog(description = "新增/修改薪资模式", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Pattern> saveOrUpdate(Pattern pattern){

        if(iPatternService.saveOrUpdate(pattern)){
            return new ResultUtil<Pattern>().setData(pattern);
        }
        return new ResultUtil<Pattern>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除薪资模式", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iPatternService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
