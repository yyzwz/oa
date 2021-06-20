package cn.exrick.xboot.modules.base.service;


import cn.exrick.xboot.base.XbootBaseService;
import cn.exrick.xboot.common.vo.SearchVo;
import cn.exrick.xboot.modules.base.entity.User;
import cn.exrick.xboot.modules.base.entity.UserDepartment;
import cn.exrick.xboot.modules.your.entity.Roster;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 用户接口
 * @author 郑为中
 */
@CacheConfig(cacheNames = "user")
public interface UserService extends XbootBaseService<User,String> {

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    @Cacheable(key = "#username")
    User findByUsername(String username);

    /**
     * 通过手机获取用户
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 通过邮件和状态获取用户
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 多条件分页获取用户
     * @param user
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<User> findByCondition(User user, SearchVo searchVo, Pageable pageable);

    /**
     * 通过部门id获取
     * @param departmentId
     * @return
     */
    List<User> findByDepartmentId(String departmentId);

    /**
     * 通过用户名模糊搜索
     * @param username
     * @param status
     * @return
     */
    List<User> findByUsernameLikeAndStatus(String username, Integer status);

    /**
     * 更新部门名称
     * @param departmentId
     * @param departmentTitle
     */
    void updateDepartmentTitle(String departmentId, String departmentTitle);

    /**
     * 查询未导入的花名册数据
     * @return
     */
    List<Roster> findNotImportUserList();

    /**
     * 模糊查询  未导入的花名册数据
     * @param name
     * @return
     */
    List<Roster> findNotImportUserListByName(String name);

    /**
     * 根据花名册ID 查找用户ID
     * @param id
     * @return
     */
    User findByRosterId(String id);

    /**
     * 模糊查询某部门下的人
     * @param name
     * @param departmentId
     * @return
     */
    List<User> findByConditionZwz(String name,String departmentId);

    User findByIdZwz(String id);

    List<User> findAllZwz();

    List<User> findByNameZwz(String name);

    List<User> getUserByRoles(String roleId);

    User findByMobileZwz(String mobile);

    List<User> findByDepartmentZwz(String departmentId);

    List<User> findByDepartmentAndNameZwz(String name,String departmentId);

    List<User> findNotImportUserByDepartment(String departmentId);

    List<User> findNotImportUserByDepartmentAndName(String departmentId,String name);

    List<User> findByPostZwz(String post);

    List<User> findByRolesId(String roleId);
}
