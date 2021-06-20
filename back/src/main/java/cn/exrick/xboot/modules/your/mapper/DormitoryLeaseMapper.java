package cn.exrick.xboot.modules.your.mapper;

import cn.exrick.xboot.modules.your.entity.AssetsType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.exrick.xboot.modules.your.entity.DormitoryLease;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宿舍租借数据处理层
 * @author 郑为中
 */
public interface DormitoryLeaseMapper extends BaseMapper<DormitoryLease> {
    List<DormitoryLease> findByHouseId(@Param("houseId") String houseId);
}