package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.DianPuWuLiao;
import cn.exrick.xboot.modules.crm.entity.DianPuXingXiang;
import cn.exrick.xboot.modules.crm.service.IDianPuXingXiangService;
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
@Api(description = "店面形象管理管理接口")
@RequestMapping("/xboot/dianPuXingXiang")
@Transactional
public class DianPuXingXiangController {

    @Autowired
    private IDianPuXingXiangService iDianPuXingXiangService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DianPuXingXiang> get(@PathVariable Long id){

        DianPuXingXiang dianPuXingXiang = iDianPuXingXiangService.getById(id);
        return new ResultUtil<DianPuXingXiang>().setData(dianPuXingXiang);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DianPuXingXiang>> getAll(){

        List<DianPuXingXiang> list = iDianPuXingXiangService.list();
        return new ResultUtil<List<DianPuXingXiang>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DianPuXingXiang>> getByPage(@ModelAttribute DianPuXingXiang dianPuXingXiang, @ModelAttribute PageVo page){
        QueryWrapper<DianPuXingXiang> qw = new QueryWrapper<DianPuXingXiang>();
        if(StrUtil.isNotBlank(dianPuXingXiang.getBan())) {
            qw.like("ban", dianPuXingXiang.getBan());
        }
        if(StrUtil.isNotBlank(dianPuXingXiang.getNumber())) {
            qw.like("number", dianPuXingXiang.getNumber());
        }
        if(StrUtil.isNotBlank(dianPuXingXiang.getRemark())) {
            qw.like("remark", dianPuXingXiang.getRemark());
        }
        if(StrUtil.isNotBlank(dianPuXingXiang.getTitle())) {
            qw.like("title", dianPuXingXiang.getTitle());
        }
        IPage<DianPuXingXiang> data = iDianPuXingXiangService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<DianPuXingXiang>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DianPuXingXiang> saveOrUpdate(DianPuXingXiang dianPuXingXiang){

        if(iDianPuXingXiangService.saveOrUpdate(dianPuXingXiang)){
            return new ResultUtil<DianPuXingXiang>().setData(dianPuXingXiang);
        }
        return new ResultUtil<DianPuXingXiang>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iDianPuXingXiangService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
