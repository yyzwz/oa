package cn.exrick.xboot.modules.crm.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.crm.entity.JingLi;
import cn.exrick.xboot.modules.crm.entity.Businessman;
import cn.exrick.xboot.modules.crm.entity.Framework;
import cn.exrick.xboot.modules.crm.service.IBusinessmanService;
import cn.exrick.xboot.modules.crm.service.IFrameworkService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "组织架构档案管理接口")
@RequestMapping("/xboot/framework")
@Transactional
public class FrameworkController {

    @Autowired
    private IFrameworkService iFrameworkService;

    @Autowired
    private IBusinessmanService iBusinessmanService;


    @RequestMapping(value = "/getNotParent", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部子客商类型")
    public Result<List<Framework>> getNotParent(){
        QueryWrapper<Framework> qw = new QueryWrapper<Framework>();
        qw.eq("is_parent", 0l);
        List<Framework> list = iFrameworkService.list(qw);
        return new ResultUtil<List<Framework>>().setData(list);
    }

    @SystemLog(description = "查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByAllSubInTreeByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取所有子节点并展开")
    public Result<List<Framework>> getByAllSubInTreeByParentId(@PathVariable String parentId,
                                                            @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){
        List<Framework> list = new ArrayList<>();
        list = getByParentIdInTree(parentId, openDataFilter);
        Result<List<Framework>> result=new ResultUtil<List<Framework>>().setData(list);
        return result;
    }

    public List<Framework> getByParentIdInTree(String parentId,Boolean openDataFilter) {
        List<Framework> list = new ArrayList<>();
        QueryWrapper<Framework> qw = new QueryWrapper<Framework>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iFrameworkService.list(qw);
        Collections.sort(list, new Comparator<Framework>() {
            @Override

            public int compare(Framework o1, Framework o2) {
                return o1.getSortOrder().compareTo(o2.getSortOrder());
            }
        });
        // list = iFrameworkService.findByParentIdOrderBySortOrder(parentId,openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            Framework Framework=list.get(i);
            Framework.setChildren(getByParentIdInTree(Framework.getId(),true));
        }
        return list;
    }
    public List<Framework> setInfo(List<Framework> list){

        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){

                Framework parent = null; // = iFrameworkService.get(item.getParentId());
                QueryWrapper<Framework> qw = new QueryWrapper<Framework>();
                if(StrUtil.isNotBlank(item.getParentId())) {
                    qw.like("id", item.getParentId());
                }
                List<Framework> list1 = iFrameworkService.list(qw);
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
    public Result<List<Framework>> getByParentId(@PathVariable String parentId,
                                              @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<Framework> list = new ArrayList<>();
        QueryWrapper<Framework> qw = new QueryWrapper<Framework>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iFrameworkService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<Framework>>().setData(list);
    }

    @SystemLog(description = "新增角色", type = LogType.OPERATION)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(Framework Framework){

        iFrameworkService.save(Framework);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if(!CommonConstant.PARENT_ID.equals(Framework.getParentId())){
            QueryWrapper<Framework> qw = new QueryWrapper<Framework>();
            if(StrUtil.isNotBlank(Framework.getParentId())) {
                qw.eq("parent_id", Framework.getParentId());
            }
            List<Framework> list = iFrameworkService.list(qw);
            Framework parent = null;
            if(!list.isEmpty()){
                parent = list.get(0);
            }
            if(parent.getIsParent()==null||!parent.getIsParent()){
                parent.setIsParent(true);
                iFrameworkService.saveOrUpdate(parent);
            }
        }
        return ResultUtil.success("添加成功");
    }

    @SystemLog(description = "编辑角色", type = LogType.OPERATION)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(Framework Framework,
                               @RequestParam(required = false) String[] mainHeader,
                               @RequestParam(required = false) String[] viceHeader){

        QueryWrapper<Framework> qw = new QueryWrapper<Framework>();
        if(StrUtil.isNotBlank(Framework.getId())) {
            qw.eq("id", Framework.getId());
        }
        List<Framework> list = iFrameworkService.list(qw);
        if(!list.isEmpty()){
            Framework old = list.get(0);
            iFrameworkService.saveOrUpdate(Framework);
            return ResultUtil.success("编辑成功");
        }
        return ResultUtil.success("原先数据不存在");
    }

    @SystemLog(description = "删除角色", type = LogType.OPERATION)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@RequestParam String[] ids){

        for (String id : ids) {
            iFrameworkService.removeById(Long.parseLong(id));
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @SystemLog(description = "模糊搜索角色", type = LogType.OPERATION)
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "岗位名模糊搜索")
    public Result<List<Framework>> searchByTitle(@RequestParam String title,
                                              @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        QueryWrapper<Framework> qw = new QueryWrapper<Framework>();
        if(StrUtil.isNotBlank(title)) {
            qw.like("title", "%"+title+"%");
        }
        List<Framework> list = iFrameworkService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<Framework>>().setData(list);
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Framework> get(@PathVariable Long id){

        Framework Framework = iFrameworkService.getById(id);
        return new ResultUtil<Framework>().setData(Framework);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Framework>> getAll(){

        List<Framework> list = iFrameworkService.list();
        return new ResultUtil<List<Framework>>().setData(list);
    }

    @SystemLog(description = "分页查询角色", type = LogType.OPERATION)
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Framework>> getByPage(PageVo page){

        IPage<Framework> data = iFrameworkService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<Framework>>().setData(data);
    }

    @SystemLog(description = "新增角色", type = LogType.OPERATION)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Framework> saveOrUpdate(Framework Framework){

        if(iFrameworkService.saveOrUpdate(Framework)){
            return new ResultUtil<Framework>().setData(Framework);
        }
        return new ResultUtil<Framework>().setErrorMsg("操作失败");
    }
}
