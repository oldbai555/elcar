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
@TableName("el_equipment")
@ApiModel(value = "Equipment对象", description = "")
public class Equipment extends ModelEntity {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备地点")
    private String place;

    @ApiModelProperty(value = "设备名称")
    private String eqName;

    @ApiModelProperty(value = "设备状态（0可用，1已用，2禁用）")
    private Integer state;

    @ApiModelProperty(value = "关联车辆类型表")
    private Integer carTypeId;

    @ApiModelProperty(value = "关联收费表，表示要收多少钱。")
    private Integer payId;


}
