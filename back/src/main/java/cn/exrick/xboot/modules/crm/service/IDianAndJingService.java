package cn.exrick.xboot.modules.crm.service;

import cn.exrick.xboot.modules.crm.entity.DianMian;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.crm.entity.DianAndJing;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店面经理关系表接口
 * @author 郑为中
 */
public interface IDianAndJingService extends IService<DianAndJing> {
    List<DianMian> findByLi(String liId);

    int deleteJAD(String liId,String dianId);

    List<DianMian> findNotImport(String name,String liId);
}