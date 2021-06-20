package cn.exrick.xboot.modules.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.crm.entity.Shi;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 城市定义接口
 * @author 郑为中
 */
public interface IShiService extends IService<Shi> {
    List<Shi> findShiBySheng(String sheng);
}