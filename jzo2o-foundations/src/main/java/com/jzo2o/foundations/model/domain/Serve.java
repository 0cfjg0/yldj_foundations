package com.jzo2o.foundations.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务表
 * </p>
 *
 * @author cfjg
 * @since 2024-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serve")
@ApiModel(value="Serve对象", description="服务表")
public class Serve implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "服务项id")
    private Long serveItemId;

    @ApiModelProperty(value = "区域id")
    private Long regionId;

    @ApiModelProperty(value = "城市编码")
    private String cityCode;

    @ApiModelProperty(value = "售卖状态，0：草稿，1下架，2上架")
    private Integer saleStatus;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "是否为热门，0非热门，1热门")
    private Integer isHot;

    @ApiModelProperty(value = "更新为热门的时间戳")
    private Long hotTimeStamp;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;


}
