package com.jzo2o.foundations.job;

import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.service.IFirstPageServeService;
import com.jzo2o.foundations.service.IRegionService;
import com.jzo2o.foundations.service.IServeItemService;
import com.jzo2o.foundations.service.IServeService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class JobHandler {

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Resource
    IRegionService regionService;

    @Resource
    IFirstPageServeService firstPageServeService;

    @Resource
    IServeItemService serveItemService;

    @Resource
    IServeService serveService;

    @XxlJob("firstpagecacheInit")
    public void cacheInit(){
        //清除缓存
        redisTemplate.delete(RedisConstants.CacheName.JZ_CACHE+"ACTIVE_REGIONS");

        //查询数据库,载入缓存
        //区域缓存
        List<RegionSimpleResDTO> region = regionService.queryActiveRegionList();
        for (RegionSimpleResDTO item : region) {
            redisTemplate.delete(RedisConstants.CacheName.HOT_SERVE+item.getId());
            redisTemplate.delete(RedisConstants.CacheName.SERVE_ICON+item.getId());
            redisTemplate.delete(RedisConstants.CacheName.SERVE_TYPE+item.getId());
            //服务类型缓存
            firstPageServeService.getServeTypeList(item.getId());
            //首页服务缓存
            firstPageServeService.getServeList(item.getId());
            //热门缓存
            for (ServeAggregationSimpleResDTO serveEvent : firstPageServeService.getHotServeList(item.getId())) {
                redisTemplate.delete(RedisConstants.CacheName.SERVE+serveEvent.getId());
                redisTemplate.delete(RedisConstants.CacheName.SERVE_ITEM+serveEvent.getServeItemId());
                serveService.getServeById(serveEvent.getId());
                serveItemService.getServeItemById(serveEvent.getServeItemId());
            }
        }
    }

    @XxlJob("activeRegionCacheSync")
    public void cacheRefresh(){
        //清除缓存
        redisTemplate.delete(RedisConstants.CacheName.JZ_CACHE+"ACTIVE_REGIONS");

        //查询数据库,载入缓存
        //区域缓存
        List<RegionSimpleResDTO> region = regionService.queryActiveRegionList();

    }

}
