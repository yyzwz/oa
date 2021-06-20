package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.KeHuSellType;
import cn.exrick.xboot.modules.crm.service.IKeHuSellTypeService;
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
@Api(description = "客户销售类型管理接口")
@RequestMapping("/xboot/keHuSellType")
@Transactional
public class KeHuSellTypeController {

    @Autowired
    private IKeHuSellTypeService iKeHuSellTypeService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<KeHuSellType> get(@PathVariable Long id){

        KeHuSellType keHuSellType = iKeHuSellTypeService.getById(id);
        return new ResultUtil<KeHuSellType>().setData(keHuSellType);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<KeHuSellType>> getAll(){

        List<KeHuSellType> list = iKeHuSellTypeService.list();
        return new ResultUtil<List<KeHuSellType>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<KeHuSellType>> getByPage(@ModelAttribute KeHuSellType keHuSellType,@ModelAttribute PageVo page){
        QueryWrapper<KeHuSellType> qw = new QueryWrapper<KeHuSellType>();
        if(StrUtil.isNotBlank(keHuSellType.getTypeNumber())) {
            qw.like("type_number", keHuSellType.getTypeNumber());
        }
        if(StrUtil.isNotBlank(keHuSellType.getManType())) {
            qw.like("man_type", keHuSellType.getManType());
        }
        if(StrUtil.isNotBlank(keHuSellType.getBan())) {
            qw.eq("ban", keHuSellType.getBan());
        }
        if(StrUtil.isNotBlank(keHuSellType.getRemark())) {
            qw.like("remark", keHuSellType.getRemark());
        }
        IPage<KeHuSellType> data = iKeHuSellTypeService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<KeHuSellType>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<KeHuSellType> saveOrUpdate(KeHuSellType keHuSellType){

        if(iKeHuSellTypeService.saveOrUpdate(keHuSellType)){
            return new ResultUtil<KeHuSellType>().setData(keHuSellType);
        }
        return new ResultUtil<KeHuSellType>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iKeHuSellTypeService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
