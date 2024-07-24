package com.jzo2o.foundations.service;

import com.jzo2o.foundations.model.domain.ServeType;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;

import java.util.List;

public interface IFirstPageServeService{

    List<ServeCategoryResDTO> getServeList(Long regionId);

    List<ServeAggregationTypeSimpleResDTO> getServeTypeList(Long regionId);

    List<ServeAggregationSimpleResDTO> getHotServeList(Long regionId);

    ServeAggregationSimpleResDTO getServeDetail(Long serveId);
}
