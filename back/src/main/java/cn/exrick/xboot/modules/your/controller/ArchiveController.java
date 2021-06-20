package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.base.entity.Department;
import cn.exrick.xboot.modules.base.service.DepartmentService;
import cn.exrick.xboot.modules.your.entity.Archive;
import cn.exrick.xboot.modules.your.service.IArchiveService;
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
@Api(description = "部门档案管理接口")
@RequestMapping("/xboot/archive")
@Transactional
public class ArchiveController {

    @Autowired
    private IArchiveService iArchiveService;

    @Autowired
    private DepartmentService departmentService;

    /**
     * 根据ID获取部门档案
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Archive> get(@PathVariable String id){

        Archive archive = iArchiveService.getById(id);
        return new ResultUtil<Archive>().setData(archive);
    }

    /**
     * 获取所有部门档案
     * @return
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有部门档案")
    public Result<List<Archive>> getAll(){

        List<Archive> list = iArchiveService.list();
        return new ResultUtil<List<Archive>>().setData(list);
    }

    /**
     * 分页查询部门档案
     * @param page
     * @return
     */
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页查询部门档案")
    public Result<IPage<Archive>> getByPage(@ModelAttribute Archive archive,@ModelAttribute PageVo page){
        QueryWrapper<Archive> qw = new QueryWrapper<Archive>();
        if(StrUtil.isNotBlank(archive.getTitle())) {
            qw.like("title", archive.getTitle());
        }
        if(StrUtil.isNotBlank(archive.getDuty())) {
            qw.like("duty", archive.getDuty());
        }
        IPage<Archive> data = iArchiveService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Archive>>().setData(data);
    }

    /**
     * 新增或者修改部门档案
     * @param archive
     * @return
     */
    @SystemLog(description = "新增/修改部门档案", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "新增或者修改部门档案")
    public Result<Archive> saveOrUpdate(Archive archive){
        if(archive.getId()!=null){
            Department de = departmentService.findByArchiveId(archive.getId());
            if(de != null && de.getTitle() != null){
                de.setTitle(archive.getTitle());
                de.setDuty(archive.getDuty());
                departmentService.update(de);
            }
        }
        if(iArchiveService.saveOrUpdate(archive)){
            return new ResultUtil<Archive>().setData(archive);
        }
        return new ResultUtil<Archive>().setErrorMsg("操作失败");
    }

    /**
     * 删除部门档案
     * @param ids
     * @return
     */
    @SystemLog(description = "删除部门档案", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除部门档案")
    public Result<Object> delAllByIds(@RequestParam String[] ids){
        for(String id : ids){
            // 未挂靠部门,禁止删除
            if(id.equals("1294437819837714432")) continue;
            Archive a = iArchiveService.findById(id);
            if(departmentService.findByArchiveId(a.getId())!=null){
                departmentService.delete(departmentService.findByArchiveId(a.getId()).getId());
            }
            iArchiveService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    /**
     *  从部门档案导入到组织架构
     * @param archives
     * @return
     */
    @RequestMapping(value = "/importData", method = RequestMethod.POST)
    @ApiOperation(value = "从部门档案导入到组织架构")
    public Result<Object> importData(@RequestBody List<Archive> archives){
        int count = 0;
        List<Integer> errors = new ArrayList<>();
        List<String> reasons = new ArrayList<>();
        for (Archive archive : archives) {
            count ++;
            if(StrUtil.isBlank(archive.getTitle())){
                errors.add(count);
                reasons.add("部门名称为空");
                continue;
            }
            if(StrUtil.isBlank(archive.getDuty())){
                errors.add(count);
                reasons.add("部门职责为空");
                continue;
            }
            if(archive.getStatus()==null){
                archive.setStatus(CommonConstant.USER_STATUS_NORMAL);
            }
            iArchiveService.saveOrUpdate(archive);
        }
        int successCount = archives.size() - errors.size();
        String successMessage = "全部导入成功，共计 " + successCount + " 条数据";
        String failMessage = "导入成功 " + successCount + " 条，失败 " + errors.size() + " 条数据。<br>" +
                "第 " + errors.toString() + " 行数据导入出错，错误原因分别为：<br>" + reasons.toString();
        String message = "";
        if(errors.size()==0){
            message = successMessage;
        }else{
            message = failMessage;
        }
       return ResultUtil.success(message);
    }
}
