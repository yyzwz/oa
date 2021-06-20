package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.*;
import cn.exrick.xboot.modules.crm.service.*;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "县区定义管理接口")
@RequestMapping("/xboot/xian")
@Transactional
public class XianController {

    @Autowired
    private IXianService iXianService;

    @Autowired
    private IShengService iShengService;

    @Autowired
    private IShiService iShiService;
    
    @Autowired
    private ISonCompanyService iSonCompanyService;
    
    @Autowired
    private IBigAreaService iBigAreaService;
    
    @Autowired
    private IAreaArchivesService iAreaArchivesService;

    @RequestMapping(value = "/getAllShengShiXian", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<JiLianCity>> getAllShengShiXian(){
        
        List<JiLianCity> ansFenGongSi = new ArrayList<>();// 分公司结果
        List<SonCompany> SonCompanys = iSonCompanyService.list(); //分公司列表
        for (SonCompany sonCompany : SonCompanys) {// 分公司
            JiLianCity j1 = new JiLianCity();
            j1.setValue(sonCompany.getTitle());
            j1.setLabel(sonCompany.getTitle());

            List<JiLianCity> ansDaQu = new ArrayList<>();// 大区结果
            List<BigArea> bigAreas = iBigAreaService.findBigAreaBySon(sonCompany.getTitle()); // 大区列表
            for (BigArea bigArea : bigAreas) { // 大区
                JiLianCity j2 = new JiLianCity();
                j2.setLabel(bigArea.getTitle());
                j2.setValue(bigArea.getTitle());

                List<JiLianCity> ansQuYu = new ArrayList<>(); // 区域结果
                List<AreaArchives> areas = iAreaArchivesService.findAreaByBigArea(bigArea.getTitle(), sonCompany.getTitle()); // 区域列表
                for (AreaArchives area : areas) { // 区域
                    JiLianCity j3 = new JiLianCity();
                    j3.setValue(area.getTitle());
                    j3.setLabel(area.getTitle());

                    List<JiLianCity> ansSheng = new ArrayList<>(); // 省结果
                    List<Sheng> shengs = iShengService.findShengByArea(area.getTitle()); // 省列表
                    for (Sheng sheng : shengs) { // 省
                        JiLianCity j4 = new JiLianCity();
                        j4.setLabel(sheng.getSheng());
                        j4.setValue(sheng.getSheng());

                        List<JiLianCity> ansShi = new ArrayList<>(); // 市结果
                        List<Shi> shis = iShiService.findShiBySheng(sheng.getSheng()); // 市列表
                        for (Shi shi : shis) { // 市
                            JiLianCity j5 = new JiLianCity();
                            j5.setValue(shi.getTitle());
                            j5.setLabel(shi.getTitle());

                            List<JiLianCity> ansXian = new ArrayList<>(); // 县结果
                            List<Xian> xians = iXianService.findShiByShengShi(sheng.getSheng(), shi.getTitle()); // 县列表
                            for (Xian xian : xians) { // 县
                                JiLianCity j6 = new JiLianCity();
                                j6.setLabel(xian.getTitle());
                                j6.setValue(xian.getTitle());
                                ansXian.add(j6);
                            }
                            j5.setChildren(ansXian); // 市 吃 县
                            ansShi.add(j5);
                        }
                        j4.setChildren(ansShi);
                        ansSheng.add(j4);
                    }
                    j3.setChildren(ansSheng);
                    ansQuYu.add(j3);
                }
                j2.setChildren(ansQuYu);
                ansDaQu.add(j2);
            }
            j1.setChildren(ansDaQu);
            ansFenGongSi.add(j1);
        }
        return new ResultUtil<List<JiLianCity>>().setData(ansFenGongSi);
    }
    
    @RequestMapping(value = "/getAllShengShiXian1", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<JiLianCity>> getAllShengShiXian1(){
        List<JiLianCity> ans = new ArrayList<>();

        List<Sheng> shengs = iShengService.list();
        for (Sheng sheng : shengs) {
            JiLianCity j1 = new JiLianCity();
            j1.setLabel(sheng.getSheng());
            j1.setValue(sheng.getSheng());
            List<Shi> shis = iShiService.findShiBySheng(sheng.getSheng());
            List<JiLianCity> ansShis = new ArrayList<>();
            for (Shi shi : shis) {
                JiLianCity j2 = new JiLianCity();
                j2.setValue(shi.getTitle());
                j2.setLabel(shi.getTitle());
                List<Xian> xians = iXianService.findShiByShengShi(sheng.getSheng(),shi.getTitle());
                List<JiLianCity> ansXians = new ArrayList<>();
                for (Xian xian : xians) {
                    JiLianCity j3 = new JiLianCity();
                    j3.setLabel(xian.getTitle());
                    j3.setValue(xian.getTitle());
                    ansXians.add(j3);
                }
                j2.setChildren(ansXians);
                ansShis.add(j2);
            }
            j1.setChildren(ansShis);
            ans.add(j1);
        }
        return new ResultUtil<List<JiLianCity>>().setData(ans);
    }

    

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Xian> get(@PathVariable Long id){

        Xian xian = iXianService.getById(id);
        return new ResultUtil<Xian>().setData(xian);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Xian>> getAll(){

        List<Xian> list = iXianService.list();
        return new ResultUtil<List<Xian>>().setData(list);
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Xian>> findByName(@RequestParam(required = false) String shi,@RequestParam(required = false) String qu){
        QueryWrapper<Xian> qw = new QueryWrapper<Xian>();
        if(StrUtil.isNotBlank(shi)) {
            qw.eq("city", shi);
        }
        if(StrUtil.isNotBlank(qu)) {
            qw.eq("title", qu);
        }
        List<Xian> list = iXianService.list(qw);
        return new ResultUtil<List<Xian>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Xian>> getByPage(@ModelAttribute Xian xian, @ModelAttribute PageVo page){
        QueryWrapper<Xian> qw = new QueryWrapper<Xian>();
        if(StrUtil.isNotBlank(xian.getBan())) {
            qw.eq("ban", xian.getBan());
        }
        if(StrUtil.isNotBlank(xian.getNumber())) {
            qw.like("number", xian.getNumber());
        }
        if(StrUtil.isNotBlank(xian.getRemark())) {
            qw.like("remark", xian.getRemark());
        }
        if(StrUtil.isNotBlank(xian.getTitle())) {
            qw.like("title", xian.getTitle());
        }
        if(StrUtil.isNotBlank(xian.getCity())) {
            qw.like("city", xian.getCity());
        }
        if(StrUtil.isNotBlank(xian.getSheng())) {
            qw.like("sheng", xian.getSheng());
        }
        if(StrUtil.isNotBlank(xian.getSellNumber())) {
            qw.like("sell_number", xian.getSellNumber());
        }
        if(StrUtil.isNotBlank(xian.getCountyType())) {
            qw.like("county_type", xian.getCountyType());
        }
        if(StrUtil.isNotBlank(xian.getMiDu())) {
            qw.like("mi_du", xian.getMiDu());
        }
        IPage<Xian> data = iXianService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Xian>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Xian> saveOrUpdate(Xian xian){

        if(iXianService.saveOrUpdate(xian)){
            return new ResultUtil<Xian>().setData(xian);
        }
        return new ResultUtil<Xian>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iXianService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/getDataByXianName", method = RequestMethod.POST)
    @ApiOperation(value = "getDataByXianName")
    public Result<KeHuRoster> getDataByXianName(@RequestParam String sheng,@RequestParam String shi,@RequestParam String xian){
        KeHuRoster keHuRoster = new KeHuRoster();
        QueryWrapper<Xian> qw = new QueryWrapper<Xian>();
        if(StrUtil.isNotBlank(sheng)) {
            qw.eq("sheng", sheng);
            List<Sheng> shengs = iShengService.list();
            for (Sheng sheng1 : shengs) {
                if(sheng1.getSheng().equals(sheng)){
                    keHuRoster.setAreaNumber(sheng1.getTitle()); // 区域
                    List<AreaArchives> archivesList = iAreaArchivesService.list();
                    for (AreaArchives archives : archivesList) {
                        if(archives.getTitle().equals(sheng1.getTitle())){
                            keHuRoster.setBigQu(archives.getBigArea());
                            List<BigArea> bigAreas = iBigAreaService.list();
                            for (BigArea bigArea : bigAreas) {
                                if(bigArea.getTitle().equals(archives.getBigArea())){
                                    keHuRoster.setFenSi(bigArea.getSonCompany());
                                }
                            }
//                            keHuRoster.setFenSi("");
                        }
                    }
                }
            }
        }
        if(StrUtil.isNotBlank(shi)) {
            qw.eq("city", shi);
        }
        if(StrUtil.isNotBlank(xian)) {
            qw.eq("title", xian);
        }
        List<Xian> list = iXianService.list(qw);
        if(list.size() == 1){
            keHuRoster.setXianType(list.get(0).getCountyType());
            keHuRoster.setDianMi(list.get(0).getMiDu());
            keHuRoster.setSellNumber(list.get(0).getSellNumber());
        }

        QueryWrapper<Shi> qww = new QueryWrapper<Shi>();
        if(StrUtil.isNotBlank(sheng)) {
            qww.eq("sheng", sheng);
        }
        if(StrUtil.isNotBlank(shi)) {
            qww.eq("title", shi);
        }
        List<Shi> shis = iShiService.list(qww);
        if(shis.size() == 1){
            keHuRoster.setCityLevel(shis.get(0).getCityLevel());
            keHuRoster.setCityType(shis.get(0).getCityType());
        }
        if(keHuRoster.getCityType()==null){
            keHuRoster.setCityType("不存在");
        }
        if(keHuRoster.getCityLevel() == null){
            keHuRoster.setCityLevel("不存在");
        }
        if(keHuRoster.getXianType() == null){
            keHuRoster.setXianType("不存在");
        }
        if(keHuRoster.getDianMi() == null){
            keHuRoster.setDianMi("不存在");
        }
        if(keHuRoster.getSellNumber() == null){
            keHuRoster.setSellNumber("不存在");
        }
        return new ResultUtil<KeHuRoster>().setData(keHuRoster);
    }
}
