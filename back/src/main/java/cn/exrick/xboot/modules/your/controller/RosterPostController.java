package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.service.UserService;
import cn.exrick.xboot.modules.base.service.mybatis.IUserService;
import cn.exrick.xboot.modules.your.entity.PostLevel;
import cn.exrick.xboot.modules.your.entity.RosterPost;
import cn.exrick.xboot.modules.your.entity.UserAndPostLevel;
import cn.exrick.xboot.modules.your.service.IPostLevelService;
import cn.exrick.xboot.modules.your.service.IRosterPostService;
import cn.exrick.xboot.modules.your.service.IUserAndPostLevelService;
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
@Api(description = "花名册岗位信息管理接口")
@RequestMapping("/xboot/rosterPost")
@Transactional
public class RosterPostController {

    @Autowired
    private IRosterPostService iRosterPostService;

    @Autowired
    private IPostLevelService iPostLevelServicel;

    @Autowired
    private IUserAndPostLevelService iUserAndPostLevelService;

    @Autowired
    private IUserService iUserService;


    @SystemLog(description = "查询单个花名册岗位", type = LogType.OPERATION)
    @RequestMapping(value = "/getOnePost", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<RosterPost> getOnePost(String id){
        RosterPost rosterPost = iRosterPostService.findByRosterId(id);
        User user = iUserService.findByRosterIdZwz(id);
        if(user != null){
            rosterPost.setPostLevelId(user.getPost());
            rosterPost.setPostLevelName(user.getPostName());
//            List<PostLevel> postLevels = iUserAndPostLevelService.selectByUserId(user.getId());
//            if(!postLevels.isEmpty()){
//                rosterPost.setPostLevelId(postLevels.get(0).getId());
//                rosterPost.setPostLevelName(postLevels.get(0).getTitle());
//            }
        }
        return new ResultUtil<RosterPost>().setData(rosterPost);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<RosterPost> get(@PathVariable String id){

        RosterPost rosterPost = iRosterPostService.getById(id);
        return new ResultUtil<RosterPost>().setData(rosterPost);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<RosterPost>> getAll(){

        List<RosterPost> list = iRosterPostService.list();
        return new ResultUtil<List<RosterPost>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<RosterPost>> getByPage(PageVo page){
        IPage<RosterPost> data = iRosterPostService.page(PageUtil.initMpPage(page));
        for (RosterPost rosterPost : data.getRecords()) {
            String rosterId = rosterPost.getRosterId();
            User user = iUserService.findByRosterIdZwz(rosterId);
            if(user != null){
                List<PostLevel> postLevels = iUserAndPostLevelService.selectByUserId(user.getId());
                if(!postLevels.isEmpty()){
                    rosterPost.setPostLevelId(postLevels.get(0).getId());
                    rosterPost.setPostLevelName(postLevels.get(0).getTitle());
                }
            }
        }
        return new ResultUtil<IPage<RosterPost>>().setData(data);
    }

    @SystemLog(description = "新增/修改花名册岗位记录", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<RosterPost> saveOrUpdate(RosterPost rosterPost){
        User user = iUserService.findByRosterIdZwz(rosterPost.getRosterId());
        if(user == null){
            return new ResultUtil<RosterPost>().setErrorMsg("还未导入到用户");
        }
        // 删除原有岗级
        iUserAndPostLevelService.deleteAllByUserId(user.getId());
        UserAndPostLevel uapl = new UserAndPostLevel();
        uapl.setUserId(user.getId());
        uapl.setLevelId(rosterPost.getPostLevelId());
        iUserAndPostLevelService.save(uapl);
        if(iRosterPostService.saveOrUpdate(rosterPost)){
            return new ResultUtil<RosterPost>().setData(rosterPost);
        }
        return new ResultUtil<RosterPost>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除花名册岗位记录", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iRosterPostService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
