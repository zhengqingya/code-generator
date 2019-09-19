package com.zhengqing.modules.common.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类的基类
 */
public class BaseEntity_bak implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private Date createdTime;

    private String creater;

    private Date modifiedTime;

    private String modifier;

    private Boolean isDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 修改时间
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    /**
     * 是否删除
     * false：未删除
     * true：删除
     */
    public Boolean isDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    /**
     * 设置ModifiedTime的值
     */
    public void update(){
        this.setModifiedTime(new Date());
    }

    /**
     * 设置CreatedTime
     */
    public void add(){ }

    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreater() {
        return creater;
    }

    /**
     * 设置创建者
     * @param creater
     */
    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModifier() {
        return modifier;
    }

    /**
     * 设置修改者
     * @param modifier
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

}
