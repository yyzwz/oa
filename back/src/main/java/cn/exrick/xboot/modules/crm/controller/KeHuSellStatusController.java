package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.KeHuSellStatus;
import cn.exrick.xboot.modules.crm.entity.KeHuSellType;
import cn.exrick.xboot.modules.crm.service.IKeHuSellStatusService;
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
@Api(description = "客户销售状态管理接口")
@RequestMapping("/xboot/keHuSellStatus")
@Transactional
public class KeHuSellStatusController {

    @Autowired
    private IKeHuSellStatusService iKeHuSellStatusService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<KeHuSellStatus> get(@PathVariable Long id){

        KeHuSellStatus keHuSellStatus = iKeHuSellStatusService.getById(id);
        return new ResultUtil<KeHuSellStatus>().setData(keHuSellStatus);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<KeHuSellStatus>> getAll(){

        List<KeHuSellStatus> list = iKeHuSellStatusService.list();
        return new ResultUtil<List<KeHuSellStatus>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<KeHuSellStatus>> getByPage(@ModelAttribute KeHuSellStatus keHuSellStatus, @ModelAttribute PageVo page){
        QueryWrapper<KeHuSellStatus> qw = new QueryWrapper<KeHuSellStatus>();
        if(StrUtil.isNotBlank(keHuSellStatus.getStatusNumber())) {
            qw.like("status_number", keHuSellStatus.getStatusNumber());
        }
        if(StrUtil.isNotBlank(keHuSellStatus.getBan())) {
            qw.eq("ban", keHuSellStatus.getBan());
        }
        if(StrUtil.isNotBlank(keHuSellStatus.getRemark())) {
            qw.like("remark", keHuSellStatus.getRemark());
        }
        if(StrUtil.isNotBlank(keHuSellStatus.getSellStatus())) {
            qw.like("sell_status", keHuSellStatus.getSellStatus());
        }
        if(StrUtil.isNotBlank(keHuSellStatus.getStatusType())) {
            qw.like("status_type", keHuSellStatus.getStatusType());
        }
        IPage<KeHuSellStatus> data = iKeHuSellStatusService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<KeHuSellStatus>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<KeHuSellStatus> saveOrUpdate(KeHuSellStatus keHuSellStatus){

        if(iKeHuSellStatusService.saveOrUpdate(keHuSellStatus)){
            return new ResultUtil<KeHuSellStatus>().setData(keHuSellStatus);
        }
        return new ResultUtil<KeHuSellStatus>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iKeHuSellStatusService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
