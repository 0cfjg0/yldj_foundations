package com.jzo2o.foundations.service;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author author
 * @since 2024-07-22
 */
public interface IServeService extends IService<Serve> {

    PageResult<ServeResDTO> getPages(ServePageQueryReqDTO dto);

    void batchAdd(List<ServeUpsertReqDTO> dto);

    void updatePrice(Long id, BigDecimal price);

    void updateStatus(Long id,Short type);

    void deleteServe(Long id);

    void setHot(Long id,Integer isHot);
}
