package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.DianPuShangHao;
import cn.exrick.xboot.modules.crm.entity.MianJiDangAn;
import cn.exrick.xboot.modules.crm.service.IDianPuShangHaoService;
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
@Api(description = "商号名称管理接口")
@RequestMapping("/xboot/dianPuShangHao")
@Transactional
public class DianPuShangHaoController {

    @Autowired
    private IDianPuShangHaoService iDianPuShangHaoService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DianPuShangHao> get(@PathVariable Long id){

        DianPuShangHao dianPuShangHao = iDianPuShangHaoService.getById(id);
        return new ResultUtil<DianPuShangHao>().setData(dianPuShangHao);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DianPuShangHao>> getAll(){

        List<DianPuShangHao> list = iDianPuShangHaoService.list();
        return new ResultUtil<List<DianPuShangHao>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DianPuShangHao>> getByPage(@ModelAttribute DianPuShangHao dianPuShangHao, @ModelAttribute PageVo page){
        QueryWrapper<DianPuShangHao> qw = new QueryWrapper<DianPuShangHao>();
        if(StrUtil.isNotBlank(dianPuShangHao.getBan())) {
            qw.like("ban", dianPuShangHao.getBan());
        }
        if(StrUtil.isNotBlank(dianPuShangHao.getNumber())) {
            qw.like("number", dianPuShangHao.getNumber());
        }
        if(StrUtil.isNotBlank(dianPuShangHao.getRemark())) {
            qw.like("remark", dianPuShangHao.getRemark());
        }
        if(StrUtil.isNotBlank(dianPuShangHao.getTitle())) {
            qw.like("title", dianPuShangHao.getTitle());
        }
        IPage<DianPuShangHao> data = iDianPuShangHaoService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<DianPuShangHao>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DianPuShangHao> saveOrUpdate(DianPuShangHao dianPuShangHao){

        if(iDianPuShangHaoService.saveOrUpdate(dianPuShangHao)){
            return new ResultUtil<DianPuShangHao>().setData(dianPuShangHao);
        }
        return new ResultUtil<DianPuShangHao>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iDianPuShangHaoService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
