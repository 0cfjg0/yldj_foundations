package com.jzo2o.foundations.controller.consumer;

import com.jzo2o.foundations.model.domain.ServeType;
import com.jzo2o.foundations.model.dto.request.ServeTypePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.service.IFirstPageServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController("consumerServeController")
@RequestMapping("/customer/serve")
@Api(tags = "用户端 - 首页服务查询接口")
public class FirstPageServeController {

    @Resource
    IFirstPageServeService firstPageServeService;


    /**
     * 查询对应区域的服务列表
     * @param regionId
     * @return
     */
    @GetMapping("/firstPageServeList")
    @ApiOperation("首页服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeCategoryResDTO> serveCategory(@RequestParam("regionId") Long regionId) {
        return firstPageServeService.getServeList(regionId);
    }

    /**
     * 服务类型列表
     */
    @GetMapping("/serveTypeList")
    @ApiOperation("服务类型列表")
    public List<ServeAggregationTypeSimpleResDTO> getServeTypeList(@RequestParam("regionId")Long regionId){
        return firstPageServeService.getServeTypeList(regionId);
    }

    /**
     * 首页热门服务列表
     */
    @GetMapping("/hotServeList")
    @ApiOperation("热门服务列表")
    public List<ServeAggregationSimpleResDTO> getHotServeList(@RequestParam("regionId")Long regionId){
        return firstPageServeService.getHotServeList(regionId);
    }

    /**
     * 根据id查询服务
     */
    @GetMapping("/{id}")
    public ServeAggregationSimpleResDTO getServeDetail(@RequestParam("id")Long serveId){
        return firstPageServeService.getServeDetail(serveId);
    }
}