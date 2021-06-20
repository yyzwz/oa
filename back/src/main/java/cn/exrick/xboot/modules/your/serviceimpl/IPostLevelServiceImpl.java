package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.PostLevelMapper;
import cn.exrick.xboot.modules.your.entity.PostLevel;
import cn.exrick.xboot.modules.your.service.IPostLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 岗级管理档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IPostLevelServiceImpl extends ServiceImpl<PostLevelMapper, PostLevel> implements IPostLevelService {

    @Autowired
    private PostLevelMapper postLevelMapper;

    @Override
    public PostLevel findByIdZwz(String id) {
        return postLevelMapper.findByIdZwz(id);
    }

    @Override
    public List<PostLevel> findListByParentIdZwz(String parentId) {
        return postLevelMapper.findListByParentIdZwz(parentId);
    }
}