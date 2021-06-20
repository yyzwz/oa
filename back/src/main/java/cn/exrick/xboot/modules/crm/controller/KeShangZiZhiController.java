package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.KeHuSellType;
import cn.exrick.xboot.modules.crm.entity.KeShangZiZhi;
import cn.exrick.xboot.modules.crm.service.IKeShangZiZhiService;
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
@Api(description = "客商资质管理接口")
@RequestMapping("/xboot/keShangZiZhi")
@Transactional
public class KeShangZiZhiController {

    @Autowired
    private IKeShangZiZhiService iKeShangZiZhiService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<KeShangZiZhi> get(@PathVariable Long id){

        KeShangZiZhi keShangZiZhi = iKeShangZiZhiService.getById(id);
        return new ResultUtil<KeShangZiZhi>().setData(keShangZiZhi);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<KeShangZiZhi>> getAll(){

        List<KeShangZiZhi> list = iKeShangZiZhiService.list();
        return new ResultUtil<List<KeShangZiZhi>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<KeShangZiZhi>> getByPage(@ModelAttribute KeShangZiZhi keShangZiZhi, @ModelAttribute PageVo page){
        QueryWrapper<KeShangZiZhi> qw = new QueryWrapper<KeShangZiZhi>();
        if(StrUtil.isNotBlank(keShangZiZhi.getBan())) {
            qw.eq("ban", keShangZiZhi.getBan());
        }
        if(StrUtil.isNotBlank(keShangZiZhi.getRemark())) {
            qw.like("remark", keShangZiZhi.getRemark());
        }
        if(StrUtil.isNotBlank(keShangZiZhi.getZiNumber())) {
            qw.like("zi_number", keShangZiZhi.getZiNumber());
        }
        if(StrUtil.isNotBlank(keShangZiZhi.getZiZhi())) {
            qw.like("zi_zhi", keShangZiZhi.getZiZhi());
        }
        IPage<KeShangZiZhi> data = iKeShangZiZhiService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<KeShangZiZhi>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<KeShangZiZhi> saveOrUpdate(KeShangZiZhi keShangZiZhi){

        if(iKeShangZiZhiService.saveOrUpdate(keShangZiZhi)){
            return new ResultUtil<KeShangZiZhi>().setData(keShangZiZhi);
        }
        return new ResultUtil<KeShangZiZhi>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iKeShangZiZhiService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
