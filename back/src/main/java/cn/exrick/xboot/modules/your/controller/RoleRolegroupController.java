package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.RoleRolegroup;
import cn.exrick.xboot.modules.your.service.IRoleRolegroupService;
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
@Api(description = "角色角色组关系表管理接口")
@RequestMapping("/xboot/roleRolegroup")
@Transactional
public class RoleRolegroupController {

    @Autowired
    private IRoleRolegroupService iRoleRolegroupService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<RoleRolegroup> get(@PathVariable String id){

        RoleRolegroup roleRolegroup = iRoleRolegroupService.getById(id);
        return new ResultUtil<RoleRolegroup>().setData(roleRolegroup);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<RoleRolegroup>> getAll(){

        List<RoleRolegroup> list = iRoleRolegroupService.list();
        return new ResultUtil<List<RoleRolegroup>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<RoleRolegroup>> getByPage(PageVo page){

        IPage<RoleRolegroup> data = iRoleRolegroupService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<RoleRolegroup>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<RoleRolegroup> saveOrUpdate(RoleRolegroup roleRolegroup){

        if(iRoleRolegroupService.saveOrUpdate(roleRolegroup)){
            return new ResultUtil<RoleRolegroup>().setData(roleRolegroup);
        }
        return new ResultUtil<RoleRolegroup>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iRoleRolegroupService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
