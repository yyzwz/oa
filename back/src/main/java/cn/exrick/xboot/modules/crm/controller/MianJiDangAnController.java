package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.MianJiDangAn;
import cn.exrick.xboot.modules.crm.entity.ShiChangArea;
import cn.exrick.xboot.modules.crm.service.IMianJiDangAnService;
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
@Api(description = "总面积档案管理接口")
@RequestMapping("/xboot/mianJiDangAn")
@Transactional
public class MianJiDangAnController {

    @Autowired
    private IMianJiDangAnService iMianJiDangAnService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<MianJiDangAn> get(@PathVariable Long id){

        MianJiDangAn mianJiDangAn = iMianJiDangAnService.getById(id);
        return new ResultUtil<MianJiDangAn>().setData(mianJiDangAn);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<MianJiDangAn>> getAll(){

        List<MianJiDangAn> list = iMianJiDangAnService.list();
        return new ResultUtil<List<MianJiDangAn>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<MianJiDangAn>> getByPage(@ModelAttribute MianJiDangAn mianJiDangAn, @ModelAttribute PageVo page){
        QueryWrapper<MianJiDangAn> qw = new QueryWrapper<MianJiDangAn>();
        if(StrUtil.isNotBlank(mianJiDangAn.getBan())) {
            qw.like("ban", mianJiDangAn.getBan());
        }
        if(StrUtil.isNotBlank(mianJiDangAn.getNumber())) {
            qw.like("number", mianJiDangAn.getNumber());
        }
        if(StrUtil.isNotBlank(mianJiDangAn.getRemark())) {
            qw.like("remark", mianJiDangAn.getRemark());
        }
        if(StrUtil.isNotBlank(mianJiDangAn.getTitle())) {
            qw.like("title", mianJiDangAn.getTitle());
        }
        IPage<MianJiDangAn> data = iMianJiDangAnService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<MianJiDangAn>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<MianJiDangAn> saveOrUpdate(MianJiDangAn mianJiDangAn){

        if(iMianJiDangAnService.saveOrUpdate(mianJiDangAn)){
            return new ResultUtil<MianJiDangAn>().setData(mianJiDangAn);
        }
        return new ResultUtil<MianJiDangAn>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iMianJiDangAnService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
