package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.DianPuTongXin;
import cn.exrick.xboot.modules.crm.entity.DianPuWuLiao;
import cn.exrick.xboot.modules.crm.service.IDianPuWuLiaoService;
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
@Api(description = "店铺物料位置管理接口")
@RequestMapping("/xboot/dianPuWuLiao")
@Transactional
public class DianPuWuLiaoController {

    @Autowired
    private IDianPuWuLiaoService iDianPuWuLiaoService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DianPuWuLiao> get(@PathVariable Long id){

        DianPuWuLiao dianPuWuLiao = iDianPuWuLiaoService.getById(id);
        return new ResultUtil<DianPuWuLiao>().setData(dianPuWuLiao);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DianPuWuLiao>> getAll(){

        List<DianPuWuLiao> list = iDianPuWuLiaoService.list();
        return new ResultUtil<List<DianPuWuLiao>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DianPuWuLiao>> getByPage(@ModelAttribute DianPuWuLiao dianPuWuLiao, @ModelAttribute PageVo page){
        QueryWrapper<DianPuWuLiao> qw = new QueryWrapper<DianPuWuLiao>();
        if(StrUtil.isNotBlank(dianPuWuLiao.getBan())) {
            qw.like("ban", dianPuWuLiao.getBan());
        }
        if(StrUtil.isNotBlank(dianPuWuLiao.getNumber())) {
            qw.like("number", dianPuWuLiao.getNumber());
        }
        if(StrUtil.isNotBlank(dianPuWuLiao.getRemark())) {
            qw.like("remark", dianPuWuLiao.getRemark());
        }
        if(StrUtil.isNotBlank(dianPuWuLiao.getTitle())) {
            qw.like("title", dianPuWuLiao.getTitle());
        }
        IPage<DianPuWuLiao> data = iDianPuWuLiaoService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<DianPuWuLiao>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DianPuWuLiao> saveOrUpdate(DianPuWuLiao dianPuWuLiao){

        if(iDianPuWuLiaoService.saveOrUpdate(dianPuWuLiao)){
            return new ResultUtil<DianPuWuLiao>().setData(dianPuWuLiao);
        }
        return new ResultUtil<DianPuWuLiao>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iDianPuWuLiaoService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
