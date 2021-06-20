package cn.exrick.xboot.modules.your.service;

import cn.exrick.xboot.common.vo.SearchVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.Archive;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 部门档案接口
 * @author 郑为中
 */
public interface IArchiveService extends IService<Archive> {
    Archive findById(String id);
}