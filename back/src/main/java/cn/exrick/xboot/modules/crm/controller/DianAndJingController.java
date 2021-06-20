package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.DianAndJing;
import cn.exrick.xboot.modules.crm.entity.DianMian;
import cn.exrick.xboot.modules.crm.service.IDianAndJingService;
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
@Api(description = "店面经理关系表管理接口")
@RequestMapping("/xboot/dianAndJing")
@Transactional
public class DianAndJingController {

    @Autowired
    private IDianAndJingService iDianAndJingService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DianAndJing> get(@PathVariable Long id){

        DianAndJing dianAndJing = iDianAndJingService.getById(id);
        return new ResultUtil<DianAndJing>().setData(dianAndJing);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DianAndJing>> getAll(){

        List<DianAndJing> list = iDianAndJingService.list();
        return new ResultUtil<List<DianAndJing>>().setData(list);
    }

    @RequestMapping(value = "/getByLi", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DianMian>> getByLi(@RequestParam String id){
        List<DianMian> list = iDianAndJingService.findByLi(id);
        return new ResultUtil<List<DianMian>>().setData(list);
    }

    @RequestMapping(value = "/importDian", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> importDian(@RequestParam String[] ids,@RequestParam String pid){

        for(String id : ids){
            DianAndJing d = new DianAndJing();
            d.setDianId(id);
            d.setJingLi(pid);
            iDianAndJingService.save(d);
        }
        return ResultUtil.success("导入成功");
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DianAndJing>> getByPage(PageVo page){

        IPage<DianAndJing> data = iDianAndJingService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<DianAndJing>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DianAndJing> saveOrUpdate(DianAndJing dianAndJing){

        if(iDianAndJingService.saveOrUpdate(dianAndJing)){
            return new ResultUtil<DianAndJing>().setData(dianAndJing);
        }
        return new ResultUtil<DianAndJing>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam Long[] ids){

        for(Long id : ids){
            iDianAndJingService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/importMore", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> importMore(@RequestParam String[] ids,@RequestParam String pid){

        for (String id : ids) {
            DianAndJing d = new DianAndJing();
            d.setDianId(id);
            d.setJingLi(pid);
            iDianAndJingService.save(d);
        }
        return ResultUtil.success("导入成功");
    }


    @RequestMapping(value = "/deleteJAD", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> deleteJAD(@RequestParam String pid,@RequestParam String did){
        iDianAndJingService.deleteJAD(pid,did);
        return ResultUtil.success("删除成功");
    }

    @RequestMapping(value = "/getByNotImport", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DianMian>> getByNotImport(@ModelAttribute DianMian dianMian, @ModelAttribute PageVo page){
        if(StrUtil.isNotBlank(dianMian.getPid())) {
            List<DianMian> dianMians = iDianAndJingService.findNotImport(dianMian.getDianName(), dianMian.getPid());
            IPage<DianMian> data = iDianAndJingService.page(PageUtil.initMpPage(page));
            List<DianMian> ans = new ArrayList<>();
            for(int i = (page.getPageNumber() - 1)*page.getPageSize() ; i < dianMians.size() && i < page.getPageSize()*page.getPageNumber(); i ++){
                ans.add(dianMians.get(i));
            }
            data.setRecords(ans);
            data.setTotal(dianMians.size());
            data.setPages(page.getPageNumber());
            data.setSize(page.getPageSize());
            return new ResultUtil<IPage<DianMian>>().setData(data);
        }
        return new ResultUtil<IPage<DianMian>>().setData(null);
    }
}
