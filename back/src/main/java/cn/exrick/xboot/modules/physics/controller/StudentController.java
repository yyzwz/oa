package cn.exrick.xboot.modules.physics.controller;

import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.physics.entity.ClassInformation;
import cn.exrick.xboot.modules.physics.entity.Student;
import cn.exrick.xboot.modules.physics.service.IStudentService;
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
@Api(description = "学生管理接口")
@RequestMapping("/xboot/student")
@Transactional
public class StudentController {

    @Autowired
    private IStudentService iStudentService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Student> get(@PathVariable String id){

        Student student = iStudentService.getById(id);
        return new ResultUtil<Student>().setData(student);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Student>> getAll(){

        List<Student> list = iStudentService.list();
        return new ResultUtil<List<Student>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Student>> getByPage(@ModelAttribute Student student, @ModelAttribute PageVo page){
        QueryWrapper<Student> qw = new QueryWrapper<Student>();
        if(StrUtil.isNotBlank(student.getName())) {
            qw.like("name", student.getName());
        }
        if(StrUtil.isNotBlank(student.getMobile())) {
            qw.like("mobile", student.getMobile());
        }
        if(StrUtil.isNotBlank(student.getNumber())) {
            qw.like("number", student.getNumber());
        }
        IPage<Student> data = iStudentService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Student>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Student> saveOrUpdate(Student student){

        if(iStudentService.saveOrUpdate(student)){
            return new ResultUtil<Student>().setData(student);
        }
        return new ResultUtil<Student>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            iStudentService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
