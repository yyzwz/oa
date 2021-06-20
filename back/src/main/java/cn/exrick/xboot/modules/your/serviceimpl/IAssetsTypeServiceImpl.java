package cn.exrick.xboot.modules.your.serviceimpl;

import cn.exrick.xboot.modules.your.mapper.AssetsTypeMapper;
import cn.exrick.xboot.modules.your.entity.AssetsType;
import cn.exrick.xboot.modules.your.service.IAssetsTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 资产种类接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IAssetsTypeServiceImpl extends ServiceImpl<AssetsTypeMapper, AssetsType> implements IAssetsTypeService {

    @Autowired
    private AssetsTypeMapper assetsTypeMapper;

    @Override
    public AssetsType findByIdZwz(String id) {
        return assetsTypeMapper.findByIdZwz(id);
    }
}