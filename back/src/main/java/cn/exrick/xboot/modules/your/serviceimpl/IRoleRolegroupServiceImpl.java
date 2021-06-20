package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.RoleRolegroupMapper;
import cn.exrick.xboot.modules.your.entity.RoleRolegroup;
import cn.exrick.xboot.modules.your.service.IRoleRolegroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色角色组关系表接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IRoleRolegroupServiceImpl extends ServiceImpl<RoleRolegroupMapper, RoleRolegroup> implements IRoleRolegroupService {

}