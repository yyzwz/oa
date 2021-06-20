package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.SellArea;
import cn.exrick.xboot.modules.crm.entity.ShangCall;
import cn.exrick.xboot.modules.crm.service.IShangCallService;
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
@Api(description = "客商联系人管理接口")
@RequestMapping("/xboot/shangCall")
@Transactional
public class ShangCallController {

    @Autowired
    private IShangCallService iShangCallService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ShangCall> get(@PathVariable Long id){

        ShangCall shangCall = iShangCallService.getById(id);
        return new ResultUtil<ShangCall>().setData(shangCall);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ShangCall>> getAll(){

        List<ShangCall> list = iShangCallService.list();
        return new ResultUtil<List<ShangCall>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ShangCall>> getByPage(@ModelAttribute ShangCall shangCall, @ModelAttribute PageVo page){
        QueryWrapper<ShangCall> qw = new QueryWrapper<ShangCall>();
        if(StrUtil.isNotBlank(shangCall.getBan())) {
            qw.like("ban", shangCall.getBan());
        }
        if(StrUtil.isNotBlank(shangCall.getAddress())) {
            qw.like("address", shangCall.getAddress());
        }
        if(StrUtil.isNotBlank(shangCall.getCanDuan())) {
            qw.like("can_duan", shangCall.getCanDuan());
        }
        if(StrUtil.isNotBlank(shangCall.getChuanZhen())) {
            qw.like("chuan_zhen", shangCall.getChuanZhen());
        }
        if(StrUtil.isNotBlank(shangCall.getMobile())) {
            qw.eq("mobile", shangCall.getMobile());
        }
        if(StrUtil.isNotBlank(shangCall.getName())) {
            qw.like("name", shangCall.getName());
        }
        if(StrUtil.isNotBlank(shangCall.getRemark())) {
            qw.like("remark", shangCall.getRemark());
        }
        if(StrUtil.isNotBlank(shangCall.getShangId())) {
            qw.eq("shang_id", shangCall.getShangId());
        }
        if(StrUtil.isNotBlank(shangCall.getShangType())) {
            qw.eq("shang_type", shangCall.getShangType());
        }
        IPage<ShangCall> data = iShangCallService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<ShangCall>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ShangCall> saveOrUpdate(ShangCall shangCall){

        if(iShangCallService.saveOrUpdate(shangCall)){
            return new ResultUtil<ShangCall>().setData(shangCall);
        }
        return new ResultUtil<ShangCall>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iShangCallService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
