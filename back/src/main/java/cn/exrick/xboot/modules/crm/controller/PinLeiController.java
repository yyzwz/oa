package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.PinLei;
import cn.exrick.xboot.modules.crm.service.IPinLeiService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "品类基础档案管理接口")
@RequestMapping("/xboot/pinLei")
@Transactional
public class PinLeiController {

    @Autowired
    private IPinLeiService iPinLeiService;

    @RequestMapping(value = "/getByAllSubInTreeByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取所有子节点并展开")
    public Result<List<PinLei>> getByAllSubInTreeByParentId(@PathVariable String parentId,
                                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){
        List<PinLei> list = new ArrayList<>();
        list = getByParentIdInTree(parentId, openDataFilter);
        Result<List<PinLei>> result=new ResultUtil<List<PinLei>>().setData(list);
        return result;
    }

    public List<PinLei> getByParentIdInTree(String parentId,Boolean openDataFilter) {
        List<PinLei> list = new ArrayList<>();
        QueryWrapper<PinLei> qw = new QueryWrapper<PinLei>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iPinLeiService.list(qw);
        Collections.sort(list, new Comparator<PinLei>() {
            @Override

            public int compare(PinLei o1, PinLei o2) {
                return o1.getSortOrder().compareTo(o2.getSortOrder());
            }
        });
        // list = iPinLeiService.findByParentIdOrderBySortOrder(parentId,openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            PinLei PinLei=list.get(i);
            PinLei.setChildren(getByParentIdInTree(PinLei.getId(),true));
        }
        return list;
    }
    public List<PinLei> setInfo(List<PinLei> list){

        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){

                PinLei parent = null; // = iPinLeiService.get(item.getParentId());
                QueryWrapper<PinLei> qw = new QueryWrapper<PinLei>();
                if(StrUtil.isNotBlank(item.getParentId())) {
                    qw.like("id", item.getParentId());
                }
                List<PinLei> list1 = iPinLeiService.list(qw);
                if(!list1.isEmpty()){
                    parent = list1.get(0);
                }
                item.setParentTitle(parent.getTitle());
            }else{
                item.setParentTitle("一级角色组");
            }

        });
        return list;
    }

    @RequestMapping(value = "/getByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取")
    public Result<List<PinLei>> getByParentId(@PathVariable String parentId,
                                                    @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<PinLei> list = new ArrayList<>();
        QueryWrapper<PinLei> qw = new QueryWrapper<PinLei>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iPinLeiService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<PinLei>>().setData(list);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(PinLei PinLei){

        iPinLeiService.save(PinLei);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if(!CommonConstant.PARENT_ID.equals(PinLei.getParentId())){
            QueryWrapper<PinLei> qw = new QueryWrapper<PinLei>();
            if(StrUtil.isNotBlank(PinLei.getParentId())) {
                qw.eq("parent_id", PinLei.getParentId());
            }
            List<PinLei> list = iPinLeiService.list(qw);
            PinLei parent = null;
            if(!list.isEmpty()){
                parent = list.get(0);
            }
            if(parent.getIsParent()==null||!parent.getIsParent()){
                parent.setIsParent(true);
                iPinLeiService.saveOrUpdate(parent);
            }
        }
        return ResultUtil.success("添加成功");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(PinLei PinLei,
                               @RequestParam(required = false) String[] mainHeader,
                               @RequestParam(required = false) String[] viceHeader){

        QueryWrapper<PinLei> qw = new QueryWrapper<PinLei>();
        if(StrUtil.isNotBlank(PinLei.getId())) {
            qw.eq("id", PinLei.getId());
        }
        List<PinLei> list = iPinLeiService.list(qw);
        if(!list.isEmpty()){
            PinLei old = list.get(0);
            iPinLeiService.saveOrUpdate(PinLei);
            return ResultUtil.success("编辑成功");
        }
        return ResultUtil.success("原先数据不存在");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@RequestParam String[] ids){

        for (String id : ids) {
            iPinLeiService.removeById(Long.parseLong(id));
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "岗位名模糊搜索")
    public Result<List<PinLei>> searchByTitle(@RequestParam String title,
                                                    @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        QueryWrapper<PinLei> qw = new QueryWrapper<PinLei>();
        if(StrUtil.isNotBlank(title)) {
            qw.like("title", "%"+title+"%");
        }
        List<PinLei> list = iPinLeiService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<PinLei>>().setData(list);
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<PinLei> get(@PathVariable Long id){

        PinLei PinLei = iPinLeiService.getById(id);
        return new ResultUtil<PinLei>().setData(PinLei);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<PinLei>> getAll(){

        List<PinLei> list = iPinLeiService.list();
        return new ResultUtil<List<PinLei>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<PinLei>> getByPage(PageVo page){

        IPage<PinLei> data = iPinLeiService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<PinLei>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<PinLei> saveOrUpdate(PinLei PinLei){

        if(iPinLeiService.saveOrUpdate(PinLei)){
            return new ResultUtil<PinLei>().setData(PinLei);
        }
        return new ResultUtil<PinLei>().setErrorMsg("操作失败");
    }
}
