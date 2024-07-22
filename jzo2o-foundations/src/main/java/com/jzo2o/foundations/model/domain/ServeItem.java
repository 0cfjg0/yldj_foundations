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
 * 服务项表
 * </p>
 *
 * @author cfjg
 * @since 2024-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serve_item")
@ApiModel(value="ServeItem对象", description="服务项表")
public class ServeItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务项id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "服务编码")
    private String code;

    @ApiModelProperty(value = "服务类型id")
    private Long serveTypeId;

    @ApiModelProperty(value = "服务名称")
    private String name;

    @ApiModelProperty(value = "服务图标")
    private String serveItemIcon;

    @ApiModelProperty(value = "服务图片")
    private String img;

    @ApiModelProperty(value = "服务数量单位")
    private Integer unit;

    @ApiModelProperty(value = "服务描述")
    private String description;

    @ApiModelProperty(value = "服务详图")
    private String detailImg;

    @ApiModelProperty(value = "参考价格")
    private BigDecimal referencePrice;

    @ApiModelProperty(value = "排序字段")
    private Integer sortNum;

    @ApiModelProperty(value = "活动状态，0：草稿，1禁用，2启用")
    private Integer activeStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;


}
