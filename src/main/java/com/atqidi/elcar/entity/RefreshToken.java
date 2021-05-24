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
 * @since 2021-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("el_refresh_token")
@ApiModel(value = "RefreshToken对象", description = "")
public class RefreshToken extends ModelEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是哪个用户的token信息")
    private Integer userId;

    @ApiModelProperty(value = "具体的token信息")
    private String refreshToken;

    @ApiModelProperty(value = "token_key可以找到token")
    private String tokenKey;

    @ApiModelProperty(value = "逻辑删除（0为不删除，1为删除）")
    @TableField(exist = false)
    private Integer isdelete;


}
