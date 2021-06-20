package cn.exrick.xboot.modules.crm.service;

import cn.exrick.xboot.modules.crm.entity.DianMian;
import cn.exrick.xboot.modules.crm.entity.Businessman;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.crm.entity.FrameworkAndPeople;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 经销商组织人员关系表接口
 * @author 郑为中
 */
public interface IFrameworkAndPeopleService extends IService<FrameworkAndPeople> {
    List<Businessman> findByLi(String frameworkId);

    int deleteJAD(String frameworkId, String peopleId);

    List<Businessman> findNotImport(String name,String frameworkId);
}