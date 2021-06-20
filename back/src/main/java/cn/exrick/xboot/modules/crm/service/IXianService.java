package cn.exrick.xboot.modules.crm.service;

import cn.exrick.xboot.modules.crm.entity.Shi;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.crm.entity.Xian;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 县区定义接口
 * @author 郑为中
 */
public interface IXianService extends IService<Xian> {
    List<Xian> findShiByShengShi(String sheng,String city);
}