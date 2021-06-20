package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.RoleArchivesMapper;
import cn.exrick.xboot.modules.your.entity.RoleArchives;
import cn.exrick.xboot.modules.your.service.IRoleArchivesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IRoleArchivesServiceImpl extends ServiceImpl<RoleArchivesMapper, RoleArchives> implements IRoleArchivesService {

    @Autowired
    private RoleArchivesMapper roleArchivesMapper;
}