package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.service.mybatis.IUserService;
import cn.exrick.xboot.modules.your.entity.PostLevel;
import cn.exrick.xboot.modules.your.entity.Welfare;
import cn.exrick.xboot.modules.your.service.IPostLevelService;
import cn.exrick.xboot.modules.your.service.IUserAndPostLevelService;
import cn.exrick.xboot.modules.your.service.IWelfareService;
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
@Api(description = "岗级管理档案管理接口")
@RequestMapping("/xboot/postLevel")
@Transactional
public class PostLevelController {

    @Autowired
    private IPostLevelService iPostLevelService;

    @Autowired
    private IWelfareService iWelfareService;

    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "/getNotImportUserData", method = RequestMethod.GET)
    @ApiOperation(value = "查询不是该岗级的用户")
    public Result<List<User>> getNotImportUserData(String postId){
        if(StrUtil.isNotBlank(postId)) {
            List<User> users = iUserService.findNotImportUserData(postId);

            return new ResultUtil<List<User>>().setData(users);
        }
        return new ResultUtil<List<User>>().setData(null);
    }

    @RequestMapping(value = "/importUserByPostId", method = RequestMethod.POST)
    @ApiOperation(value = "岗级批量导入用户")
    public Result<Object> importUserByPostId(@RequestParam String[] ids,@RequestParam String postId){
        PostLevel level = iPostLevelService.findByIdZwz(postId);
        if(StrUtil.isNotBlank(postId) && level != null) {
            for(String id : ids){
                User user = iUserService.getById(id);
                user.setPost(postId);
                user.setPostName(level.getTitle());
                iUserService.saveOrUpdate(user);
            }
            return ResultUtil.success("导入成功");
        }
        return ResultUtil.error("导入失败");
    }


    @RequestMapping(value = "/getByAllSubInTreeByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取所有子节点并展开")
    public Result<List<PostLevel>> getByAllSubInTreeByParentId(@PathVariable String parentId,
                                                               @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){
        List<PostLevel> list = new ArrayList<>();
        list = getByParentIdInTree(parentId, openDataFilter);
        Result<List<PostLevel>> result=new ResultUtil<List<PostLevel>>().setData(list);
        return result;
    }
    public List<PostLevel> getByParentIdInTree(String parentId,Boolean openDataFilter) {
        List<PostLevel> list = new ArrayList<>();
        QueryWrapper<PostLevel> qw = new QueryWrapper<PostLevel>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iPostLevelService.list(qw);
        // 根据排序值排序
        Collections.sort(list, new Comparator<PostLevel>() {
            @Override
            public int compare(PostLevel p1, PostLevel p2) {
                return p1.getSortOrder().compareTo(p2.getSortOrder());
            }
        });
        // list = iElseJueSeService.findByParentIdOrderBySortOrder(parentId,openDataFilter);
        list = setInfo(list);
        for(int i=0;i<list.size();i++){
            PostLevel postLevel=list.get(i);
            postLevel.setChildren(getByParentIdInTree(postLevel.getId(),true));
        }
        return list;
    }
    public List<PostLevel> setInfo(List<PostLevel> list){
        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){

                PostLevel parent = null; // = iElseJueSeService.get(item.getParentId());
                QueryWrapper<PostLevel> qw = new QueryWrapper<PostLevel>();
                if(StrUtil.isNotBlank(item.getParentId())) {
                    qw.like("id", item.getParentId());
                }
                List<PostLevel> list1 = iPostLevelService.list(qw);
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
    public Result<List<PostLevel>> getByParentId(@PathVariable String parentId,
                                                 @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<PostLevel> list = new ArrayList<>();
        QueryWrapper<PostLevel> qw = new QueryWrapper<PostLevel>();
        if(StrUtil.isNotBlank(parentId)) {
            qw.eq("parent_id", parentId);
        }
        list = iPostLevelService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<PostLevel>>().setData(list);
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(PostLevel postLevel){
        // 创建福利表
        Welfare welfare = new Welfare();
        welfare.setBirthdayBonus("0");
        welfare.setCommunicationSubsidy("0");
        welfare.setMalAllowance("0");
        welfare.setTransportationSubsidy("0");
        iWelfareService.saveOrUpdate(welfare);
        postLevel.setIsParent(false);
        postLevel.setWelfare(welfare.getId());
        iPostLevelService.save(postLevel);
        // 不是一级菜单
        if(!CommonConstant.PARENT_ID.equals(postLevel.getParentId())) {
            PostLevel result = StrUtil.isNotBlank(postLevel.getParentId())?iPostLevelService.findByIdZwz(postLevel.getParentId()):null;
            PostLevel parent = result == null ? null :result;
            if(parent.getIsParent()==null||!parent.getIsParent()){
                parent.setIsParent(true);
                iPostLevelService.saveOrUpdate(parent);
            }
        }
        return ResultUtil.success("添加成功");
    }
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(PostLevel postLevel,
                               @RequestParam(required = false) String[] mainHeader,
                               @RequestParam(required = false) String[] viceHeader){

        QueryWrapper<PostLevel> qw = new QueryWrapper<PostLevel>();
        if(StrUtil.isNotBlank(postLevel.getId())) {
            qw.eq("id", postLevel.getId());
        }
        List<PostLevel> list = iPostLevelService.list(qw);
        if(!list.isEmpty()){
            PostLevel old = list.get(0);
            iPostLevelService.saveOrUpdate(postLevel);
            return ResultUtil.success("编辑成功");
        }
        return ResultUtil.success("原先数据不存在");
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "岗位名模糊搜索")
    public Result<List<PostLevel>> searchByTitle(@RequestParam String title,
                                                 @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        QueryWrapper<PostLevel> qw = new QueryWrapper<PostLevel>();
        if(StrUtil.isNotBlank(title)) {
            qw.like("title", "%"+title+"%");
        }
        List<PostLevel> list = iPostLevelService.list(qw);
        list = setInfo(list);
        return new ResultUtil<List<PostLevel>>().setData(list);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<PostLevel> get(@PathVariable String id){

        PostLevel postLevel = iPostLevelService.getById(id);
        return new ResultUtil<PostLevel>().setData(postLevel);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<PostLevel>> getAll(){

        List<PostLevel> list = iPostLevelService.list();
        return new ResultUtil<List<PostLevel>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<PostLevel>> getByPage(PageVo page){

        IPage<PostLevel> data = iPostLevelService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<PostLevel>>().setData(data);
    }

    @SystemLog(description = "新增/修改岗级记录", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<PostLevel> saveOrUpdate(PostLevel postLevel){

        if(iPostLevelService.saveOrUpdate(postLevel)){
            return new ResultUtil<PostLevel>().setData(postLevel);
        }
        return new ResultUtil<PostLevel>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除岗级", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){
        for(String id : ids){
            PostLevel result = iPostLevelService.findByIdZwz(id);
            List<PostLevel> res = iPostLevelService.findListByParentIdZwz(id);
            if(res != null && res.size() > 0){
                return ResultUtil.error("请先删除子部门 " + res.get(0).getTitle() + "重试");
            }
            // 删除关联福利表
            iWelfareService.removeById(result.getWelfare());
            iPostLevelService.removeById(id);
        }
        return ResultUtil.success("删除成功");
    }
}
