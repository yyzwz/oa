package cn.exrick.xboot.modules.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.crm.entity.AreaArchives;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 区域档案接口
 * @author 郑为中
 */
public interface IAreaArchivesService extends IService<AreaArchives> {
    List<String> findAllArea();
    List<AreaArchives> findAreaByBigArea(String area, String company);
}