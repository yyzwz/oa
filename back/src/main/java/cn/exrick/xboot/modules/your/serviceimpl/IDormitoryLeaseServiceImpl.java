package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.DormitoryLeaseMapper;
import cn.exrick.xboot.modules.your.entity.DormitoryLease;
import cn.exrick.xboot.modules.your.service.IDormitoryLeaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 宿舍租借接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IDormitoryLeaseServiceImpl extends ServiceImpl<DormitoryLeaseMapper, DormitoryLease> implements IDormitoryLeaseService {

    @Autowired
    private DormitoryLeaseMapper dormitoryLeaseMapper;

    @Override
    public List<DormitoryLease> findByHouseId(String houseId) {
        return dormitoryLeaseMapper.findByHouseId(houseId);
    }
}