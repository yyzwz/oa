package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.PaySlip;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工资条接口
 * @author 郑为中
 */
public interface IPaySlipService extends IService<PaySlip> {
    PaySlip findByIdZwz(String id);
    int deleteByYearAndMouth(String year,String mouth);
}