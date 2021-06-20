package cn.exrick.xboot.modules.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.crm.entity.BigArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 大区定义接口
 * @author 郑为中
 */
public interface IBigAreaService extends IService<BigArea> {
    List<BigArea> findBigAreaBySon(String company);
}