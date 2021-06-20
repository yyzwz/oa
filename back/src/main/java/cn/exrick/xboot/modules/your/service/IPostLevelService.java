package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.PostLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗级管理档案接口
 * @author 郑为中
 */
public interface IPostLevelService extends IService<PostLevel> {
    PostLevel findByIdZwz(String id);

    List<PostLevel> findListByParentIdZwz(String parentId);
}