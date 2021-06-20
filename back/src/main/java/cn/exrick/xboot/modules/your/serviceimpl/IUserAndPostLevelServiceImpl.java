package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.entity.PostLevel;
import cn.exrick.xboot.modules.your.mapper.UserAndPostLevelMapper;
import cn.exrick.xboot.modules.your.entity.UserAndPostLevel;
import cn.exrick.xboot.modules.your.service.IUserAndPostLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户岗级关系表接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IUserAndPostLevelServiceImpl extends ServiceImpl<UserAndPostLevelMapper, UserAndPostLevel> implements IUserAndPostLevelService {

    @Autowired
    private UserAndPostLevelMapper userAndPostLevelMapper;

    @Override
    public int deleteAllByUserId(String userId) {
        return userAndPostLevelMapper.deleteAllByUserId(userId);
    }

    @Override
    public List<PostLevel> selectByUserId(String userId) {
        return userAndPostLevelMapper.selectByUserId(userId);
    }
}