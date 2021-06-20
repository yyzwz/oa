package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.DormitoryLease;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宿舍租借接口
 * @author 郑为中
 */
public interface IDormitoryLeaseService extends IService<DormitoryLease> {

    List<DormitoryLease> findByHouseId(String houseId);
}