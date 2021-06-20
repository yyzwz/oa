package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.ShiChangManage;
import cn.exrick.xboot.modules.crm.entity.ShiChangPinPai;
import cn.exrick.xboot.modules.crm.service.IShiChangPinPaiService;
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
@Api(description = "市场品牌档案管理接口")
@RequestMapping("/xboot/shiChangPinPai")
@Transactional
public class ShiChangPinPaiController {

    @Autowired
    private IShiChangPinPaiService iShiChangPinPaiService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ShiChangPinPai> get(@PathVariable Long id){

        ShiChangPinPai shiChangPinPai = iShiChangPinPaiService.getById(id);
        return new ResultUtil<ShiChangPinPai>().setData(shiChangPinPai);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ShiChangPinPai>> getAll(){

        List<ShiChangPinPai> list = iShiChangPinPaiService.list();
        return new ResultUtil<List<ShiChangPinPai>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ShiChangPinPai>> getByPage(@ModelAttribute ShiChangPinPai shiChangPinPai, @ModelAttribute PageVo page){
        QueryWrapper<ShiChangPinPai> qw = new QueryWrapper<ShiChangPinPai>();
        if(StrUtil.isNotBlank(shiChangPinPai.getBan())) {
            qw.like("ban", shiChangPinPai.getBan());
        }
        if(StrUtil.isNotBlank(shiChangPinPai.getNumber())) {
            qw.like("number", shiChangPinPai.getNumber());
        }
        if(StrUtil.isNotBlank(shiChangPinPai.getRemark())) {
            qw.like("remark", shiChangPinPai.getRemark());
        }
        if(StrUtil.isNotBlank(shiChangPinPai.getTitle())) {
            qw.like("title", shiChangPinPai.getTitle());
        }
        IPage<ShiChangPinPai> data = iShiChangPinPaiService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ShiChangPinPai>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ShiChangPinPai> saveOrUpdate(ShiChangPinPai shiChangPinPai){

        if(iShiChangPinPaiService.saveOrUpdate(shiChangPinPai)){
            return new ResultUtil<ShiChangPinPai>().setData(shiChangPinPai);
        }
        return new ResultUtil<ShiChangPinPai>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShiChangPinPaiService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
