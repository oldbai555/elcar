package com.atqidi.elcar.entity;

import com.atqidi.elcar.entity.model.ModelEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("el_order_table")
@ApiModel(value = "OrderTable对象", description = "")
public class OrderTable extends ModelEntity {


    @ApiModelProperty(value = "车辆Id")
    private Integer carId;

    @ApiModelProperty(value = "设备Id")
    private Integer eqId;

    @ApiModelProperty(value = "合计支付金额")
    private Double pay;

    @ApiModelProperty(value = "订单状态（0未开始，1正在进行，2结束，3未确认开始超时结束）")
    private Integer state;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "关联用户")
    private String userPhone;

    @ApiModelProperty(value = "设备名称")
    @TableField(exist = false)
    private String eqName;


}
