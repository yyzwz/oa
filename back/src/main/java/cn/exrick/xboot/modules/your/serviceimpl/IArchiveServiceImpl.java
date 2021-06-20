package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.common.vo.SearchVo;
import cn.exrick.xboot.modules.your.mapper.ArchiveMapper;
import cn.exrick.xboot.modules.your.entity.Archive;
import cn.exrick.xboot.modules.your.service.IArchiveService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IArchiveServiceImpl extends ServiceImpl<ArchiveMapper, Archive> implements IArchiveService {

    @Autowired
    private ArchiveMapper archiveMapper;

    @Override
    public Archive findById(String id) {
        return archiveMapper.findById(id);
    }
}