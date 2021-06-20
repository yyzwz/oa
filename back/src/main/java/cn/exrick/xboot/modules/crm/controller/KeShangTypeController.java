package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.crm.entity.KeHuSellType;
import cn.exrick.xboot.modules.crm.entity.KeShangType;
import cn.exrick.xboot.modules.crm.service.IKeShangTypeService;
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

import java.util.*;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "客商类型管理接口")
@RequestMapping("/xboot/keShangType")
@Transactional
public class KeShangTypeController {

    @Autowired
    private IKeShangTypeService iKeShangTypeService;

    @RequestMapping(value = "/getNotParent", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部子客商类型")
    public Result<List<KeShangType>> getNotParent(){
        QueryWrapper<KeShangType> qw = new QueryWrapper<KeShangType>();
        qw.eq("is_parent", 0l);
        List<KeShangType> list = iKeShangTypeService.list(qw);
        return new ResultUtil<List<KeShangType>>().setData(list);
    }

    @SystemLog(description = "查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByAllSubInTreeByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取所有子节点并展开")
    public Result<List<KeShangType>> getByAllSubInTreeByParentId(@PathVariable String parentId,
                                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){
        List<KeShangType> list = new ArrayList<>();
        list = getByParentIdInTree(parentId, openDataFilter);
        Result<List<KeShangType>> result=new ResultUtil<List<KeShangType>>().setData(list);
        return result;
    }

    public List<KeShangType> getByParentIdInTree(String parentId,Boolean openDataFilter) {
        List<KeShangType> list = new ArrayList<>();
        QueryWrapper<KeShangType> qw = new QueryWrapper<KeShangType>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iKeShangTypeService.list(qw);
        Collections.sort(list, new Comparator<KeShangType>() {
            @Override

            public int compare(KeShangType o1, KeShangType o2) {
                return o1.getSortOrder().compareTo(o2.getSortOrder());
            }
        });
        // list = iKeShangTypeService.findByParentIdOrderBySortOrder(parentId,openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            KeShangType KeShangType=list.get(i);
            KeShangType.setChildren(getByParentIdInTree(KeShangType.getId(),true));
        }
        return list;
    }
    public List<KeShangType> setInfo(List<KeShangType> list){

        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){

                KeShangType parent = null; // = iKeShangTypeService.get(item.getParentId());
                QueryWrapper<KeShangType> qw = new QueryWrapper<KeShangType>();
                if(StrUtil.isNotBlank(item.getParentId())) {
                    qw.like("id", item.getParentId());
                }
                List<KeShangType> list1 = iKeShangTypeService.list(qw);
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

    @SystemLog(description = "查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取")
    public Result<List<KeShangType>> getByParentId(@PathVariable String parentId,
                                                    @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<KeShangType> list = new ArrayList<>();
        QueryWrapper<KeShangType> qw = new QueryWrapper<KeShangType>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iKeShangTypeService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<KeShangType>>().setData(list);
    }

    @SystemLog(description = "新增角色", type = LogType.OPERATION)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(KeShangType KeShangType){

        iKeShangTypeService.save(KeShangType);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if(!CommonConstant.PARENT_ID.equals(KeShangType.getParentId())){
            QueryWrapper<KeShangType> qw = new QueryWrapper<KeShangType>();
            if(StrUtil.isNotBlank(KeShangType.getParentId())) {
                qw.eq("parent_id", KeShangType.getParentId());
            }
            List<KeShangType> list = iKeShangTypeService.list(qw);
            KeShangType parent = null;
            if(!list.isEmpty()){
                parent = list.get(0);
            }
            if(parent.getIsParent()==null||!parent.getIsParent()){
                parent.setIsParent(true);
                iKeShangTypeService.saveOrUpdate(parent);
            }
        }
        return ResultUtil.success("添加成功");
    }

    @SystemLog(description = "编辑角色", type = LogType.OPERATION)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(KeShangType KeShangType,
                               @RequestParam(required = false) String[] mainHeader,
                               @RequestParam(required = false) String[] viceHeader){

        QueryWrapper<KeShangType> qw = new QueryWrapper<KeShangType>();
        if(StrUtil.isNotBlank(KeShangType.getId())) {
            qw.eq("id", KeShangType.getId());
        }
        List<KeShangType> list = iKeShangTypeService.list(qw);
        if(!list.isEmpty()){
            KeShangType old = list.get(0);
            iKeShangTypeService.saveOrUpdate(KeShangType);
            return ResultUtil.success("编辑成功");
        }
        return ResultUtil.success("原先数据不存在");
    }

    @SystemLog(description = "删除角色", type = LogType.OPERATION)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@RequestParam String[] ids){

        for (String id : ids) {
            iKeShangTypeService.removeById(Long.parseLong(id));
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @SystemLog(description = "模糊搜索角色", type = LogType.OPERATION)
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "岗位名模糊搜索")
    public Result<List<KeShangType>> searchByTitle(@RequestParam String title,
                                                    @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        QueryWrapper<KeShangType> qw = new QueryWrapper<KeShangType>();
        if(StrUtil.isNotBlank(title)) {
            qw.like("title", "%"+title+"%");
        }
        List<KeShangType> list = iKeShangTypeService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<KeShangType>>().setData(list);
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<KeShangType> get(@PathVariable Long id){

        KeShangType KeShangType = iKeShangTypeService.getById(id);
        return new ResultUtil<KeShangType>().setData(KeShangType);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<KeShangType>> getAll(){

        List<KeShangType> list = iKeShangTypeService.list();
        return new ResultUtil<List<KeShangType>>().setData(list);
    }

    @SystemLog(description = "分页查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<KeShangType>> getByPage(PageVo page){

        IPage<KeShangType> data = iKeShangTypeService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<KeShangType>>().setData(data);
    }

    @SystemLog(description = "新增角色", type = LogType.OPERATION)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<KeShangType> saveOrUpdate(KeShangType KeShangType){

        if(iKeShangTypeService.saveOrUpdate(KeShangType)){
            return new ResultUtil<KeShangType>().setData(KeShangType);
        }
        return new ResultUtil<KeShangType>().setErrorMsg("操作失败");
    }
}
