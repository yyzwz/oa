package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.UserAndPostLevel;
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
@Api(description = "用户岗级关系表管理接口")
@RequestMapping("/xboot/userAndPostLevel")
@Transactional
public class UserAndPostLevelController {

    @Autowired
    private IUserAndPostLevelService iUserAndPostLevelService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<UserAndPostLevel> get(@PathVariable String id){

        UserAndPostLevel userAndPostLevel = iUserAndPostLevelService.getById(id);
        return new ResultUtil<UserAndPostLevel>().setData(userAndPostLevel);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<UserAndPostLevel>> getAll(){

        List<UserAndPostLevel> list = iUserAndPostLevelService.list();
        return new ResultUtil<List<UserAndPostLevel>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<UserAndPostLevel>> getByPage(PageVo page){

        IPage<UserAndPostLevel> data = iUserAndPostLevelService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<UserAndPostLevel>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<UserAndPostLevel> saveOrUpdate(UserAndPostLevel userAndPostLevel){

        if(iUserAndPostLevelService.saveOrUpdate(userAndPostLevel)){
            return new ResultUtil<UserAndPostLevel>().setData(userAndPostLevel);
        }
        return new ResultUtil<UserAndPostLevel>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iUserAndPostLevelService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
