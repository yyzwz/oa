package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.MianJiDangAn;
import cn.exrick.xboot.modules.crm.entity.OneMianJi;
import cn.exrick.xboot.modules.crm.service.IOneMianJiService;
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
@Api(description = "单品类面积管理接口")
@RequestMapping("/xboot/oneMianJi")
@Transactional
public class OneMianJiController {

    @Autowired
    private IOneMianJiService iOneMianJiService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<OneMianJi> get(@PathVariable Long id){

        OneMianJi oneMianJi = iOneMianJiService.getById(id);
        return new ResultUtil<OneMianJi>().setData(oneMianJi);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<OneMianJi>> getAll(){

        List<OneMianJi> list = iOneMianJiService.list();
        return new ResultUtil<List<OneMianJi>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<OneMianJi>> getByPage(@ModelAttribute OneMianJi oneMianJi,@ModelAttribute PageVo page){
        QueryWrapper<OneMianJi> qw = new QueryWrapper<OneMianJi>();
        if(StrUtil.isNotBlank(oneMianJi.getBan())) {
            qw.like("ban", oneMianJi.getBan());
        }
        if(StrUtil.isNotBlank(oneMianJi.getNumber())) {
            qw.like("number", oneMianJi.getNumber());
        }
        if(StrUtil.isNotBlank(oneMianJi.getRemark())) {
            qw.like("remark", oneMianJi.getRemark());
        }
        if(StrUtil.isNotBlank(oneMianJi.getTitle())) {
            qw.like("title", oneMianJi.getTitle());
        }
        IPage<OneMianJi> data = iOneMianJiService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<OneMianJi>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<OneMianJi> saveOrUpdate(OneMianJi oneMianJi){

        if(iOneMianJiService.saveOrUpdate(oneMianJi)){
            return new ResultUtil<OneMianJi>().setData(oneMianJi);
        }
        return new ResultUtil<OneMianJi>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iOneMianJiService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
