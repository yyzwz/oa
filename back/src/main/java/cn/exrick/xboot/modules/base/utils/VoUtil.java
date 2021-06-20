package cn.exrick.xboot.modules.base.utils;

import cn.exrick.xboot.modules.base.entity.Permission;
import cn.exrick.xboot.modules.base.vo.MenuVo;
import cn.hutool.core.bean.BeanUtil;

/**
 * @author 郑为中
 */
public class VoUtil {

    public static MenuVo permissionToMenuVo(Permission p){

        MenuVo menuVo = new MenuVo();
        BeanUtil.copyProperties(p, menuVo);
        return menuVo;
    }
}
