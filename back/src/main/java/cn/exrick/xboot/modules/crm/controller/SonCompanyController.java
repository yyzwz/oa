package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import cn.exrick.xboot.modules.crm.entity.SonCompany;
import cn.exrick.xboot.modules.crm.service.ISonCompanyService;
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
@Api(description = "分公司定义管理接口")
@RequestMapping("/xboot/sonCompany")
@Transactional
public class SonCompanyController {

    @Autowired
    private ISonCompanyService iSonCompanyService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<SonCompany> get(@PathVariable Long id){

        SonCompany sonCompany = iSonCompanyService.getById(id);
        return new ResultUtil<SonCompany>().setData(sonCompany);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<SonCompany>> getAll(){

        List<SonCompany> list = iSonCompanyService.list();
        return new ResultUtil<List<SonCompany>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<SonCompany>> getByPage(@ModelAttribute SonCompany sonCompany, @ModelAttribute PageVo page){
        QueryWrapper<SonCompany> qw = new QueryWrapper<SonCompany>();
        if(StrUtil.isNotBlank(sonCompany.getBan())) {
            qw.eq("ban", sonCompany.getBan());
        }
        if(StrUtil.isNotBlank(sonCompany.getNumber())) {
            qw.like("number", sonCompany.getNumber());
        }
        if(StrUtil.isNotBlank(sonCompany.getRemark())) {
            qw.like("remark", sonCompany.getRemark());
        }
        if(StrUtil.isNotBlank(sonCompany.getDingId())) {
            qw.like("ding_id", sonCompany.getDingId());
        }
        if(StrUtil.isNotBlank(sonCompany.getTitle())) {
            qw.like("title", sonCompany.getTitle());
        }
        IPage<SonCompany> data = iSonCompanyService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<SonCompany>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<SonCompany> saveOrUpdate(SonCompany sonCompany){

        if(iSonCompanyService.saveOrUpdate(sonCompany)){
            return new ResultUtil<SonCompany>().setData(sonCompany);
        }
        return new ResultUtil<SonCompany>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iSonCompanyService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
