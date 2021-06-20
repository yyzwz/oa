package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.utils.DayUtil;
import cn.exrick.xboot.modules.your.entity.DormitoryLease;
import cn.exrick.xboot.modules.your.entity.WaterCost;
import cn.exrick.xboot.modules.your.service.IDormitoryLeaseService;
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
@Api(description = "宿舍租借管理接口")
@RequestMapping("/xboot/dormitoryLease")
@Transactional
public class DormitoryLeaseController {

    @Autowired
    private IDormitoryLeaseService iDormitoryLeaseService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DormitoryLease> get(@PathVariable String id){

        DormitoryLease dormitoryLease = iDormitoryLeaseService.getById(id);
        return new ResultUtil<DormitoryLease>().setData(dormitoryLease);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DormitoryLease>> getAll(){

        List<DormitoryLease> list = iDormitoryLeaseService.list();
        return new ResultUtil<List<DormitoryLease>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DormitoryLease>> getByPage(@ModelAttribute DormitoryLease dormitoryLease, @ModelAttribute PageVo page){
        QueryWrapper<DormitoryLease> qw = new QueryWrapper<DormitoryLease>();
        if(StrUtil.isNotBlank(dormitoryLease.getHouseNumber())) {
            qw.like("house_number", dormitoryLease.getHouseNumber());
        }
        if(StrUtil.isNotBlank(dormitoryLease.getUserName())) {
            qw.like("user_name", dormitoryLease.getUserName());
        }
        if(StrUtil.isNotBlank(dormitoryLease.getSelectStartDate())) {
            qw.gt("create_time",dormitoryLease.getSelectStartDate());
            qw.lt("create_time", DayUtil.nextDay(dormitoryLease.getSelectEndDate()));
        }
        IPage<DormitoryLease> data = iDormitoryLeaseService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<DormitoryLease>>().setData(data);
    }

    @SystemLog(description = "新增/修改宿舍租借记录", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DormitoryLease> saveOrUpdate(DormitoryLease dormitoryLease){

        if(iDormitoryLeaseService.saveOrUpdate(dormitoryLease)){
            return new ResultUtil<DormitoryLease>().setData(dormitoryLease);
        }
        return new ResultUtil<DormitoryLease>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除宿舍租借记录", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iDormitoryLeaseService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
