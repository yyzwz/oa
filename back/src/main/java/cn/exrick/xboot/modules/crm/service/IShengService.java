package cn.exrick.xboot.modules.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.crm.entity.Sheng;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 省份定义接口
 * @author 郑为中
 */
public interface IShengService extends IService<Sheng> {
    List<String> findAllSheng();

    List<Sheng> findShengByArea(String area);
}