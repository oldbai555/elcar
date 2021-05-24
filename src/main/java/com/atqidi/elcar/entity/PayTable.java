package com.atqidi.elcar.entity;

import com.atqidi.elcar.entity.model.ModelEntity;
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
@TableName("el_pay_table")
@ApiModel(value = "PayTable对象", description = "")
public class PayTable extends ModelEntity {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联车辆类型表，根据什么类型收什么样的费")
    private Integer carTypeId;

    @ApiModelProperty(value = "时段名称")
    private String payTimeName;

    @ApiModelProperty(value = "需要的金额（服务费）")
    private Double payMoney;

    @ApiModelProperty(value = "收费开始时间")
    private Integer chargeStart;

    @ApiModelProperty(value = "收费结束时间")
    private Integer chargeEnd;

    @ApiModelProperty(value = "电费(多少钱一度电或者多少钱一个小时)")
    private Double payFee;

    @ApiModelProperty(value = "服务费+电费的合计")
    private Double payCount;
}
