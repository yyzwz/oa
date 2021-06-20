package cn.exrick.xboot.modules.your.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.exrick.xboot.modules.your.entity.AssetsType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资产种类接口
 * @author 郑为中
 */
public interface IAssetsTypeService extends IService<AssetsType> {
    AssetsType findByIdZwz(String id);
}