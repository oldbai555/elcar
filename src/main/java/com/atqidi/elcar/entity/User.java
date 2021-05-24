package com.atqidi.elcar.entity;

import com.atqidi.elcar.entity.model.ModelEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

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
@TableName("el_user")
@ApiModel(value = "User对象", description = "")
public class User extends ModelEntity {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "教职工、学生、普通人的身份卡。比如身份证，学生证，教师证。")
    private String userIdCard;

    @ApiModelProperty(value = "角色ID，关联角色表，逻辑关联。")
    private Integer roleId;

    @TableField(exist = false)
    private List<Car> carList;

    @TableField(exist = false)
    private String rose;


}
