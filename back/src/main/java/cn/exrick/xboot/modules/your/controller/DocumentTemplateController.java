package cn.exrick.xboot.modules.your.controller;

import cn.exrick.xboot.common.annotation.SystemLog;
import cn.exrick.xboot.common.enums.LogType;
import cn.exrick.xboot.common.utils.PageUtil;
import cn.exrick.xboot.common.utils.ResultUtil;
import cn.exrick.xboot.common.vo.PageVo;
import cn.exrick.xboot.common.vo.Result;
import cn.exrick.xboot.modules.your.entity.DocumentTemplate;
import cn.exrick.xboot.modules.your.service.IDocumentTemplateService;
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
@Api(description = "文档模板管理接口")
@RequestMapping("/xboot/documentTemplate")
@Transactional
public class DocumentTemplateController {

    @Autowired
    private IDocumentTemplateService iDocumentTemplateService;


    @RequestMapping("/downloadFile/{id}")
    public ResponseEntity<byte[]> downloadImportedFile(@PathVariable String id) throws IOException {
        QueryWrapper<DocumentTemplate> qw = new QueryWrapper<DocumentTemplate>();
        if(StrUtil.isNotBlank(id)) {
            qw.eq("id", id);
        }
        List<DocumentTemplate> list = iDocumentTemplateService.list(qw);
        if(!list.isEmpty()){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "Template_" + id + "." + list.get(0).getHou());
            java.io.File filePath = new File("C:\\OAFiles\\Template\\" + id + "." + list.get(0).getHou());
            list.get(0).setTimes(Integer.parseInt(list.get(0).getTimes()) + 1 + "");
            iDocumentTemplateService.saveOrUpdate(list.get(0));
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(filePath), headers, HttpStatus.CREATED);
        }
        return null;
    }

    @RequestMapping(value = "/file/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传")
    public Result<Object> upload(@PathVariable String id,
                                 @RequestParam(required = false) MultipartFile file,
                                 @RequestParam(required = false) String base64,
                                 HttpServletRequest request) {
        if(file == null){
            return ResultUtil.error("上传文件为空");
        }
        QueryWrapper<DocumentTemplate> qw = new QueryWrapper<DocumentTemplate>();
        if(StrUtil.isNotBlank(id)) {
            qw.eq("id", id);
            List<DocumentTemplate> list = iDocumentTemplateService.list(qw);
            if(list.isEmpty()){
                return ResultUtil.error("模板记录不存在");
            }
            list.get(0).setIsHave("1");
            list.get(0).setHou(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            iDocumentTemplateService.saveOrUpdate(list.get(0));
        }
        String realPaths = "C:\\OAFiles\\Template\\";
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

    @RequestMapping(value = "/findAllHou", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<String>> findAllHou(){
        List<String> list = iDocumentTemplateService.findAllHou();
        return new ResultUtil<List<String>>().setData(list);
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DocumentTemplate> get(@PathVariable String id){

        DocumentTemplate documentTemplate = iDocumentTemplateService.getById(id);
        return new ResultUtil<DocumentTemplate>().setData(documentTemplate);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DocumentTemplate>> getAll(){

        List<DocumentTemplate> list = iDocumentTemplateService.list();
        return new ResultUtil<List<DocumentTemplate>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DocumentTemplate>> getByPage(@ModelAttribute DocumentTemplate documentTemplate,@ModelAttribute PageVo page){

        QueryWrapper<DocumentTemplate> qw = new QueryWrapper<DocumentTemplate>();
        if(StrUtil.isNotBlank(documentTemplate.getHou())) {
            qw.like("hou", documentTemplate.getHou());
        }
        IPage<DocumentTemplate> data = iDocumentTemplateService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<DocumentTemplate>>().setData(data);
    }

    @SystemLog(description = "新增/修改文档模板", type = LogType.INSERT)
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DocumentTemplate> saveOrUpdate(DocumentTemplate documentTemplate){
        documentTemplate.setTimes("0");
        if(iDocumentTemplateService.saveOrUpdate(documentTemplate)){
            return new ResultUtil<DocumentTemplate>().setData(documentTemplate);
        }
        return new ResultUtil<DocumentTemplate>().setErrorMsg("操作失败");
    }

    @SystemLog(description = "删除文档模板", type = LogType.DELETE)
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){

        for(String id : ids){
            DocumentTemplate dt = iDocumentTemplateService.findByIdZwz(id);
            File file = new File("C:\\OAFiles\\Template\\" + id + "." + dt.getHou());
            if(file != null){
                file.delete();
            }
            iDocumentTemplateService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
