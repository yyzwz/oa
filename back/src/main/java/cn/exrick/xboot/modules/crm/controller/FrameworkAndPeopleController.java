package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.DianAndJing;
import cn.exrick.xboot.modules.crm.entity.DianMian;
import cn.exrick.xboot.modules.crm.entity.Businessman;
import cn.exrick.xboot.modules.crm.entity.FrameworkAndPeople;
import cn.exrick.xboot.modules.crm.service.IFrameworkAndPeopleService;
import cn.exrick.xboot.modules.crm.service.IFrameworkService;
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
@Api(description = "经销商组织人员关系表管理接口")
@RequestMapping("/xboot/frameworkAndPeople")
@Transactional
public class FrameworkAndPeopleController {

    @Autowired
    private IFrameworkAndPeopleService iFrameworkAndPeopleService;

    @Autowired
    private IFrameworkService iFrameworkService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<FrameworkAndPeople> get(@PathVariable Long id){

        FrameworkAndPeople frameworkAndPeople = iFrameworkAndPeopleService.getById(id);
        return new ResultUtil<FrameworkAndPeople>().setData(frameworkAndPeople);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<FrameworkAndPeople>> getAll(){

        List<FrameworkAndPeople> list = iFrameworkAndPeopleService.list();
        return new ResultUtil<List<FrameworkAndPeople>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<FrameworkAndPeople>> getByPage(PageVo page){

        IPage<FrameworkAndPeople> data = iFrameworkAndPeopleService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<FrameworkAndPeople>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<FrameworkAndPeople> saveOrUpdate(FrameworkAndPeople frameworkAndPeople){

        if(iFrameworkAndPeopleService.saveOrUpdate(frameworkAndPeople)){
            return new ResultUtil<FrameworkAndPeople>().setData(frameworkAndPeople);
        }
        return new ResultUtil<FrameworkAndPeople>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iFrameworkAndPeopleService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/getByNotImport", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Businessman>> getByNotImport(@ModelAttribute Businessman businessman, @ModelAttribute PageVo page){
        if(StrUtil.isNotBlank(businessman.getPid())) {
            List<Businessman> dianMians = iFrameworkAndPeopleService.findNotImport(businessman.getName(), businessman.getPid());
            IPage<Businessman> data = iFrameworkAndPeopleService.page(PageUtil.initMpPage(page));
            List<Businessman> ans = new ArrayList<>();
            for(int i = (page.getPageNumber() - 1)*page.getPageSize() ; i < dianMians.size() && i < page.getPageSize()*page.getPageNumber(); i ++){
                ans.add(dianMians.get(i));
            }
            data.setRecords(ans);
            data.setTotal(dianMians.size());
            data.setPages(page.getPageNumber());
            data.setSize(page.getPageSize());
            return new ResultUtil<IPage<Businessman>>().setData(data);
        }
        return new ResultUtil<IPage<Businessman>>().setData(null);
    }

    @RequestMapping(value = "/importMore", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> importMore(@RequestParam String[] ids,@RequestParam String pid){

        for (String id : ids) {
            FrameworkAndPeople d = new FrameworkAndPeople();
            d.setPeopleId(id);
            d.setFrameworkId(pid);
            iFrameworkAndPeopleService.save(d);
        }
        return ResultUtil.success("导入成功");
    }

    @RequestMapping(value = "/getByLi", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Businessman>> getByLi(@RequestParam String id){
        List<Businessman> list = iFrameworkAndPeopleService.findByLi(id);
        return new ResultUtil<List<Businessman>>().setData(list);
    }

    @RequestMapping(value = "/deleteJAD", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> deleteJAD(@RequestParam String pid,@RequestParam String did){
        iFrameworkAndPeopleService.deleteJAD(pid,did);
        return ResultUtil.success("删除成功");
    }
}
