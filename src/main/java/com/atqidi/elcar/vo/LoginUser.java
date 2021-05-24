package com.atqidi.elcar.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 老白
 */
@Data
public class LoginUser {
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;
}
