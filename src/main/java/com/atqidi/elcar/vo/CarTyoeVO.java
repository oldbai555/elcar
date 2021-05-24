package com.atqidi.elcar.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CarTyoeVO {
    @ApiModelProperty(value = "车辆类型名称")
    private String typeName;
}
