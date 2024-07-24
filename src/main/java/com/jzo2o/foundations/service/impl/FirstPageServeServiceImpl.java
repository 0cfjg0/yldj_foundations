package com.jzo2o.foundations.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.FirstPageServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.domain.ServeType;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.model.dto.response.ServeSimpleResDTO;
import com.jzo2o.foundations.service.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FirstPageServeServiceImpl implements IFirstPageServeService {

    @Resource
    FirstPageServeMapper firstPageServeMapper;

    @Resource
    IRegionService regionService;

    @Resource
    IServeTypeService serveTypeService;

    @Resource
    IServeService serveService;

    @Resource
    IServeItemService serveItemService;


    //只有unless可以校验result,缓存空值以免缓存穿透
    @Override
    @Caching(
            cacheable = {
                    @Cacheable(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    @Cacheable(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public List<ServeCategoryResDTO> getServeList(Long regionId) {
        //校验参数
        LambdaQueryWrapper<Region> wrapper = Wrappers.<Region>lambdaQuery().eq(Region::getId, regionId).eq(Region::getActiveStatus, FoundationStatusEnum.ENABLE.getStatus());
        Region region = regionService.getOne(wrapper);
        if(ObjectUtil.isEmpty(region)){
            throw new ForbiddenOperationException("区域不存在或已禁用");
        }

        //查询服务
        List<ServeCategoryResDTO> res = firstPageServeMapper.getServeList(regionId);


        //截取两个服务
        List<ServeCategoryResDTO> resList = res.stream().limit(2).collect(Collectors.toList());
        //截取四个具体服务
        resList.stream().forEach(item -> {
            List<ServeSimpleResDTO> tmpSimpleList = item.getServeResDTOList().stream().limit(4).collect(Collectors.toList());
            item.setServeResDTOList(tmpSimpleList);
        });

        if(ObjectUtil.isEmpty(resList)){
            return Collections.emptyList();
        }

        return resList;
    }

    @Override
    @Caching(
            cacheable = {
                    @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public List<ServeAggregationTypeSimpleResDTO> getServeTypeList(Long regionId) {
        //校验参数
        LambdaQueryWrapper<Region> wrapper = Wrappers.<Region>lambdaQuery().eq(Region::getId, regionId).eq(Region::getActiveStatus, FoundationStatusEnum.ENABLE.getStatus());
        Region region = regionService.getOne(wrapper);
        if(ObjectUtil.isEmpty(region)){
            throw new ForbiddenOperationException("区域不存在或已禁用");
        }

        //查询服务
        List<ServeCategoryResDTO> res = firstPageServeMapper.getServeList(regionId);

        //得到服务类型列表
        List<ServeAggregationTypeSimpleResDTO> resList = res.stream().map(item -> {
                    ServeAggregationTypeSimpleResDTO tmp = BeanUtil.toBean(item, ServeAggregationTypeSimpleResDTO.class);
                    tmp.setServeTypeImg(item.getServeTypeIcon());
                    return tmp;
                }
        ).collect(Collectors.toList());

        if(ObjectUtil.isEmpty(resList)){
            return Collections.emptyList();
        }

        return resList;
    }

    @Override
    @Caching(
            cacheable = {
                    @Cacheable(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    @Cacheable(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public List<ServeAggregationSimpleResDTO> getHotServeList(Long regionId) {
        //校验参数
        LambdaQueryWrapper<Region> wrapper = Wrappers.<Region>lambdaQuery().eq(Region::getId, regionId).eq(Region::getActiveStatus, FoundationStatusEnum.ENABLE.getStatus());
        Region region = regionService.getOne(wrapper);
        if(ObjectUtil.isEmpty(region)){
            throw new ForbiddenOperationException("区域不存在或已禁用");
        }

        //查询热门服务
        List<ServeAggregationSimpleResDTO> resList = firstPageServeMapper.getHotServe(regionId);

        if(ObjectUtil.isEmpty(resList)){
            return Collections.emptyList();
        }

        return resList;
    }

    @Override
    public ServeAggregationSimpleResDTO getServeDetail(Long serveId) {
        //校验参数
        if(ObjectUtil.isNull(serveId)){
            throw new ForbiddenOperationException("参数异常");
        }

        //查询服务
        Serve serve = serveService.getServeById(serveId);
        ServeItem serveItem = serveItemService.getServeItemById(serve.getServeItemId());
        if(!ObjectUtil.isAllNotEmpty(serve,serveItem)){
            throw new ForbiddenOperationException("查询错误");
        }

        //封装
        return ServeAggregationSimpleResDTO.builder()
                .id(serve.getId())
                .cityCode(serve.getCityCode())
                .serveItemId(serveItem.getId())
                .serveItemImg(serveItem.getImg())
                .serveItemName(serveItem.getName())
                .unit(serveItem.getUnit())
                .detailImg(serveItem.getDetailImg())
                .price(serve.getPrice())
                .build();
    }
}
