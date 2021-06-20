package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.DianPuShangHao;
import cn.exrick.xboot.modules.crm.entity.DianPuTongXin;
import cn.exrick.xboot.modules.crm.service.IDianPuTongXinService;
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
@Api(description = "通信位置管理接口")
@RequestMapping("/xboot/dianPuTongXin")
@Transactional
public class DianPuTongXinController {

    @Autowired
    private IDianPuTongXinService iDianPuTongXinService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DianPuTongXin> get(@PathVariable Long id){

        DianPuTongXin dianPuTongXin = iDianPuTongXinService.getById(id);
        return new ResultUtil<DianPuTongXin>().setData(dianPuTongXin);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DianPuTongXin>> getAll(){

        List<DianPuTongXin> list = iDianPuTongXinService.list();
        return new ResultUtil<List<DianPuTongXin>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DianPuTongXin>> getByPage(@ModelAttribute DianPuTongXin dianPuTongXin, @ModelAttribute PageVo page){
        QueryWrapper<DianPuTongXin> qw = new QueryWrapper<DianPuTongXin>();
        if(StrUtil.isNotBlank(dianPuTongXin.getBan())) {
            qw.like("ban", dianPuTongXin.getBan());
        }
        if(StrUtil.isNotBlank(dianPuTongXin.getNumber())) {
            qw.like("number", dianPuTongXin.getNumber());
        }
        if(StrUtil.isNotBlank(dianPuTongXin.getRemark())) {
            qw.like("remark", dianPuTongXin.getRemark());
        }
        if(StrUtil.isNotBlank(dianPuTongXin.getTitle())) {
            qw.like("title", dianPuTongXin.getTitle());
        }
        IPage<DianPuTongXin> data = iDianPuTongXinService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<DianPuTongXin>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DianPuTongXin> saveOrUpdate(DianPuTongXin dianPuTongXin){

        if(iDianPuTongXinService.saveOrUpdate(dianPuTongXin)){
            return new ResultUtil<DianPuTongXin>().setData(dianPuTongXin);
        }
        return new ResultUtil<DianPuTongXin>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iDianPuTongXinService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
