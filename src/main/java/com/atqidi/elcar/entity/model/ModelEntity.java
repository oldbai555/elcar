package com.atqidi.elcar.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 模板
 *
 * @author 老白
 */
public class ModelEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "版本号，乐观锁")
    @Version
    private Integer version;

    @ApiModelProperty(value = "插入时间")
    @TableField(fill = FieldFill.INSERT)
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除（0为不删除，1为删除）")
    private Integer isdelete;


    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
