package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.KeHuSellType;
import cn.exrick.xboot.modules.crm.entity.KeShangRoster;
import cn.exrick.xboot.modules.crm.service.IKeShangRosterService;
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
@Api(description = "客商档案管理接口")
@RequestMapping("/xboot/keShangRoster")
@Transactional
public class KeShangRosterController {

    @Autowired
    private IKeShangRosterService iKeShangRosterService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<KeShangRoster> get(@PathVariable Long id){

        KeShangRoster keShangRoster = iKeShangRosterService.getById(id);
        return new ResultUtil<KeShangRoster>().setData(keShangRoster);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<KeShangRoster>> getAll(){

        List<KeShangRoster> list = iKeShangRosterService.list();
        return new ResultUtil<List<KeShangRoster>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<KeShangRoster>> getByPage(@ModelAttribute KeShangRoster keShangRoster, @ModelAttribute PageVo page){
        QueryWrapper<KeShangRoster> qw = new QueryWrapper<KeShangRoster>();
        if(StrUtil.isNotBlank(keShangRoster.getAddress())) {
            qw.like("address", keShangRoster.getAddress());
        }
        if(StrUtil.isNotBlank(keShangRoster.getArea())) {
            qw.like("area", keShangRoster.getArea());
        }
        if(StrUtil.isNotBlank(keShangRoster.getBan())) {
            qw.like("ban", keShangRoster.getBan());
        }
        if(StrUtil.isNotBlank(keShangRoster.getCity())) {
            qw.like("city", keShangRoster.getCity());
        }
        if(StrUtil.isNotBlank(keShangRoster.getJieDao())) {
            qw.like("jie_dao", keShangRoster.getJieDao());
        }
        if(StrUtil.isNotBlank(keShangRoster.getRemark())) {
            qw.like("remark", keShangRoster.getRemark());
        }
        if(StrUtil.isNotBlank(keShangRoster.getShangName())) {
            qw.like("shang_name", keShangRoster.getShangName());
        }
        if(StrUtil.isNotBlank(keShangRoster.getShangNumber())) {
            qw.like("shang_number", keShangRoster.getShangNumber());
        }
        if(StrUtil.isNotBlank(keShangRoster.getShangName())) {
            qw.like("shang_name", keShangRoster.getShangName());
        }
        if(StrUtil.isNotBlank(keShangRoster.getSheng())) {
            qw.like("sheng", keShangRoster.getSheng());
        }
        if(StrUtil.isNotBlank(keShangRoster.getXinMoney())) {
            qw.like("xin_money", keShangRoster.getXinMoney());
        }
        if(StrUtil.isNotBlank(keShangRoster.getHangNumber())) {
            qw.like("hang_number", keShangRoster.getHangNumber());
        }
        if(StrUtil.isNotBlank(keShangRoster.getOpenHang())) {
            qw.like("open_hang", keShangRoster.getOpenHang());
        }
        if(StrUtil.isNotBlank(keShangRoster.getShangType())) {
            qw.like("shang_type", keShangRoster.getShangType());
        }
        if(StrUtil.isNotBlank(keShangRoster.getXian())) {
            qw.like("xian", keShangRoster.getXian());
        }
        IPage<KeShangRoster> data = iKeShangRosterService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<KeShangRoster>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<KeShangRoster> saveOrUpdate(KeShangRoster keShangRoster){

        if(iKeShangRosterService.saveOrUpdate(keShangRoster)){
            return new ResultUtil<KeShangRoster>().setData(keShangRoster);
        }
        return new ResultUtil<KeShangRoster>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iKeShangRosterService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
