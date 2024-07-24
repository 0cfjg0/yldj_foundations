package com.jzo2o.foundations.mapper;

import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FirstPageServeMapper {

    List<ServeCategoryResDTO> getServeList(Long regionId);

    List<ServeAggregationSimpleResDTO> getHotServe(Long regionId);
}
