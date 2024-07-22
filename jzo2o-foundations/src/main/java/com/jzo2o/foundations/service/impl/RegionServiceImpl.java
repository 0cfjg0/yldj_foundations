package com.jzo2o.foundations.service.impl;

import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.service.IRegionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 区域表 服务实现类
 * </p>
 *
 * @author cfjg
 * @since 2024-07-22
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

}
