package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.RosterEnclosure;
import cn.exrick.xboot.modules.your.service.IRosterEnclosureService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(description = "花名册附件管理接口")
@RequestMapping("/xboot/rosterEnclosure")
@Transactional
public class RosterEnclosureController {

    @Autowired
    private IRosterEnclosureService iRosterEnclosureService;

    @SystemLog(description = "下载花名册附件", type = LogType.OPERATION)
    @RequestMapping("/downloadFile/{id}")
    public ResponseEntity<byte[]> downloadImportedFile(@PathVariable String id) throws IOException {
        RosterEnclosure result = iRosterEnclosureService.findById(id);
        if(result != null){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("enclosure", "zwz" + id + "." + result.getSuffixName());
            java.io.File filePath = new File("C:\\OAFiles\\" + id + "." + result.getSuffixName());
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(filePath), headers, HttpStatus.CREATED);
        }
        return null;
    }

    @SystemLog(description = "上传花名册附件", type = LogType.OPERATION)
    @RequestMapping(value = "/file/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传")
    public Result<Object> upload(@PathVariable String id,
                                 @RequestParam(required = false) MultipartFile file,
                                 @RequestParam(required = false) String base64,
                                 HttpServletRequest request) {
        if(file == null){
            return ResultUtil.error("文件不存在");
        }
        RosterEnclosure result = iRosterEnclosureService.findById(id);
        if(result != null){
            result.setIsUpload("1");
            result.setSuffixName(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            iRosterEnclosureService.saveOrUpdate(result);
        }
        String realPaths = "C:\\OAFiles\\";
        try {
            java.io.File dir = new File(realPaths);
            if (!dir.exists()) {
                dir.mkdir();
            }
            java.io.File files  =  new File(realPaths,  id + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            file.transferTo(files);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success("附件上传成功");
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<RosterEnclosure> get(@PathVariable String id){

        RosterEnclosure rosterEnclosure = iRosterEnclosureService.getById(id);
        return new ResultUtil<RosterEnclosure>().setData(rosterEnclosure);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<RosterEnclosure>> getAll(){

        List<RosterEnclosure> list = iRosterEnclosureService.list();
        return new ResultUtil<List<RosterEnclosure>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<RosterEnclosure>> getByPage(@ModelAttribute RosterEnclosure rosterEnclosure,@ModelAttribute PageVo page){
        QueryWrapper<RosterEnclosure> qw = new QueryWrapper<RosterEnclosure>();
        if(StrUtil.isNotBlank(rosterEnclosure.getRosterId())) {
            qw.eq("roster_id", rosterEnclosure.getRosterId());
        }
        IPage<RosterEnclosure> data = iRosterEnclosureService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<RosterEnclosure>>().setData(data);
    }

    @SystemLog(description = "新增/修改花名册附件", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<RosterEnclosure> saveOrUpdate(RosterEnclosure rosterEnclosure){

        if(iRosterEnclosureService.saveOrUpdate(rosterEnclosure)){
            return new ResultUtil<RosterEnclosure>().setData(rosterEnclosure);
        }
        return new ResultUtil<RosterEnclosure>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除花名册附件", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "花名册附件删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            RosterEnclosure result = iRosterEnclosureService.findById("" + id);
            if(result != null){
                File file = new File("C:\\OAFiles\\" + id + "." + result.getSuffixName());
                if(file != null){
                    file.delete();
                }
            }
            iRosterEnclosureService.removeById(id);
        }
        return ResultUtil.success("花名册附件删除成功");
    }
}
