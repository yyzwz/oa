package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.KeHuRoster;
import cn.exrick.xboot.modules.crm.entity.KeHuSellStatus;
import cn.exrick.xboot.modules.crm.service.IKeHuRosterService;
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
@Api(description = "客户档案管理接口")
@RequestMapping("/xboot/keHuRoster")
@Transactional
public class KeHuRosterController {

    @Autowired
    private IKeHuRosterService iKeHuRosterService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<KeHuRoster> get(@PathVariable Long id){

        KeHuRoster keHuRoster = iKeHuRosterService.getById(id);
        return new ResultUtil<KeHuRoster>().setData(keHuRoster);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<KeHuRoster>> getAll(){

        List<KeHuRoster> list = iKeHuRosterService.list();
        return new ResultUtil<List<KeHuRoster>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<KeHuRoster>> getByPage(@ModelAttribute KeHuRoster keHuRoster, @ModelAttribute PageVo page){
        QueryWrapper<KeHuRoster> qw = new QueryWrapper<KeHuRoster>();
        if(StrUtil.isNotBlank(keHuRoster.getBan())) {
            qw.eq("ban", keHuRoster.getBan());
        }
        if(StrUtil.isNotBlank(keHuRoster.getName())) {
            qw.like("name", keHuRoster.getName());
        }
        if(StrUtil.isNotBlank(keHuRoster.getRemark())) {
            qw.like("remark", keHuRoster.getRemark());
        }
        if(StrUtil.isNotBlank(keHuRoster.getAreaManger())) {
            qw.like("area_manger", keHuRoster.getAreaManger());
        }
        if(StrUtil.isNotBlank(keHuRoster.getAreaNumber())) {
            qw.like("area_number", keHuRoster.getAreaNumber());
        }
        if(StrUtil.isNotBlank(keHuRoster.getBigQu())) {
            qw.like("big_qu", keHuRoster.getBigQu());
        }
        if(StrUtil.isNotBlank(keHuRoster.getCallPeople())) {
            qw.like("call_people", keHuRoster.getCallPeople());
        }
        if(StrUtil.isNotBlank(keHuRoster.getCityLevel())) {
            qw.like("city_level", keHuRoster.getCityLevel());
        }
        if(StrUtil.isNotBlank(keHuRoster.getCityType())) {
            qw.like("city_type", keHuRoster.getCityType());
        }
        if(StrUtil.isNotBlank(keHuRoster.getDianMi())) {
            qw.like("dian_mi", keHuRoster.getDianMi());
        }
        if(StrUtil.isNotBlank(keHuRoster.getFenSi())) {
            qw.like("fen_si", keHuRoster.getFenSi());
        }
        if(StrUtil.isNotBlank(keHuRoster.getHuForm())) {
            qw.like("hu_form", keHuRoster.getHuForm());
        }
        if(StrUtil.isNotBlank(keHuRoster.getHuLevel())) {
            qw.like("hu_level", keHuRoster.getHuLevel());
        }
        if(StrUtil.isNotBlank(keHuRoster.getHuLiang())) {
            qw.like("hu_liang", keHuRoster.getHuLiang());
        }
        if(StrUtil.isNotBlank(keHuRoster.getHuType())) {
            qw.like("hu_type", keHuRoster.getHuType());
        }
        if(StrUtil.isNotBlank(keHuRoster.getJianCai())) {
            qw.like("jian_cai", keHuRoster.getJianCai());
        }
        if(StrUtil.isNotBlank(keHuRoster.getJieDao())) {
            qw.like("jie_dao", keHuRoster.getJieDao());
        }
        if(StrUtil.isNotBlank(keHuRoster.getKaiHang())) {
            qw.like("kai_hang", keHuRoster.getKaiHang());
        }
        if(StrUtil.isNotBlank(keHuRoster.getKeFu())) {
            qw.like("ke_fu", keHuRoster.getKeFu());
        }
        if(StrUtil.isNotBlank(keHuRoster.getPeopleForm())) {
            qw.like("people_form", keHuRoster.getPeopleForm());
        }
        if(StrUtil.isNotBlank(keHuRoster.getSellNumber())) {
            qw.like("sell_number", keHuRoster.getSellNumber());
        }
        if(StrUtil.isNotBlank(keHuRoster.getShangForm())) {
            qw.like("shang_form", keHuRoster.getShangForm());
        }
        if(StrUtil.isNotBlank(keHuRoster.getShangName())) {
            qw.like("shang_name", keHuRoster.getShangName());
        }
        if(StrUtil.isNotBlank(keHuRoster.getShangNumber())) {
            qw.like("shang_number", keHuRoster.getShangNumber());
        }
        if(StrUtil.isNotBlank(keHuRoster.getSheng())) {
            qw.like("sheng", keHuRoster.getSheng());
        }
        if(StrUtil.isNotBlank(keHuRoster.getShi())) {
            qw.like("shi", keHuRoster.getShi());
        }
        if(StrUtil.isNotBlank(keHuRoster.getXianType())) {
            qw.like("xian_type", keHuRoster.getXianType());
        }
        if(StrUtil.isNotBlank(keHuRoster.getXinMoney())) {
            qw.like("xin_money", keHuRoster.getXinMoney());
        }
        if(StrUtil.isNotBlank(keHuRoster.getYeWu())) {
            qw.like("ye_wu", keHuRoster.getYeWu());
        }
        if(StrUtil.isNotBlank(keHuRoster.getYinHang())) {
            qw.like("yin_hang", keHuRoster.getYinHang());
        }
        IPage<KeHuRoster> data = iKeHuRosterService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<KeHuRoster>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<KeHuRoster> saveOrUpdate(KeHuRoster keHuRoster){

        if(iKeHuRosterService.saveOrUpdate(keHuRoster)){
            return new ResultUtil<KeHuRoster>().setData(keHuRoster);
        }
        return new ResultUtil<KeHuRoster>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/insertOrUpdateUniapp", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<KeHuRoster> insertOrUpdateUniapp(@RequestParam String cityType,@RequestParam String cityLevel,@RequestParam String xianType,
                                                   @RequestParam String dianMi,@RequestParam String sellNumber,@RequestParam String ban,
                                                   @RequestParam String shangForm,@RequestParam String huType,@RequestParam String shangNumber,
                                                   @RequestParam String shangName,@RequestParam String huForm,@RequestParam String xinMoney,
                                                   @RequestParam String huLevel,@RequestParam String huLiang,@RequestParam String areaManger,
                                                   @RequestParam String yeWu,@RequestParam String keFu,@RequestParam String callPeople,
                                                   @RequestParam String jieDao,@RequestParam String jianCai,@RequestParam String remark,
                                                   @RequestParam String kaiHang,@RequestParam String yinHang,@RequestParam String id,
                                                   @RequestParam String sheng,@RequestParam String shi,@RequestParam String xian,
                                                   @RequestParam String bigQu,@RequestParam String fenSi,@RequestParam String areaNumber){
        KeHuRoster k = new KeHuRoster();
        if(StrUtil.isNotBlank(id) && !id.equals("undefined")) {
            k.setId(id);
        }
        k.setAreaNumber(areaNumber);
        k.setSheng(sheng);
        k.setShi(shi);
        k.setPeopleForm(xian);
        k.setBigQu(bigQu);
        k.setFenSi(fenSi);
        k.setCityType(cityType);
        k.setCityLevel(cityLevel);
        k.setXianType(xianType);
        k.setDianMi(dianMi);
        k.setSellNumber(sellNumber);
        k.setBan(ban);
        k.setShangForm(shangForm);
        k.setHuType(huType);
        k.setShangNumber(shangNumber);
        k.setShangName(shangName);
        k.setHuForm(huForm);
        k.setXinMoney(xinMoney);
        k.setHuLevel(huLevel);
        k.setHuLiang(huLiang);
        k.setAreaManger(areaManger);
        k.setYeWu(yeWu);
        k.setKeFu(keFu);
        k.setCallPeople(callPeople);
        k.setJieDao(jieDao);
        k.setJianCai(jianCai);
        k.setRemark(remark);
        k.setKaiHang(kaiHang);
        k.setYinHang(yinHang);
        if(iKeHuRosterService.saveOrUpdate(k)){
            return new ResultUtil<KeHuRoster>().setData(k);
        }
        return new ResultUtil<KeHuRoster>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iKeHuRosterService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
