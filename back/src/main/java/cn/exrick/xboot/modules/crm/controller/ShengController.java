package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.entity.Sheng;
import cn.exrick.xboot.modules.crm.service.IShengService;
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
@Api(description = "省份定义管理接口")
@RequestMapping("/xboot/sheng")
@Transactional
public class ShengController {

    @Autowired
    private IShengService iShengService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Sheng> get(@PathVariable Long id){

        Sheng sheng = iShengService.getById(id);
        return new ResultUtil<Sheng>().setData(sheng);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Sheng>> getAll(){

        List<Sheng> list = iShengService.list();
        return new ResultUtil<List<Sheng>>().setData(list);
    }

    @RequestMapping(value = "/findAllSheng", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有省份")
    public Result<List<String>> findAllSheng(){
        List<String> list = iShengService.findAllSheng();
        return new ResultUtil<List<String>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Sheng>> getByPage(@ModelAttribute Sheng sheng, @ModelAttribute PageVo page){
        QueryWrapper<Sheng> qw = new QueryWrapper<Sheng>();
        if(StrUtil.isNotBlank(sheng.getBan())) {
            qw.eq("ban", sheng.getBan());
        }
        if(StrUtil.isNotBlank(sheng.getNumber())) {
            qw.like("number", sheng.getNumber());
        }
        if(StrUtil.isNotBlank(sheng.getRemark())) {
            qw.like("remark", sheng.getRemark());
        }
        if(StrUtil.isNotBlank(sheng.getTitle())) {
            qw.like("title", sheng.getTitle());
        }
        if(StrUtil.isNotBlank(sheng.getSheng())) {
            qw.like("sheng", sheng.getSheng());
        }
        IPage<Sheng> data = iShengService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Sheng>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Sheng> saveOrUpdate(Sheng sheng){

        if(iShengService.saveOrUpdate(sheng)){
            return new ResultUtil<Sheng>().setData(sheng);
        }
        return new ResultUtil<Sheng>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShengService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
