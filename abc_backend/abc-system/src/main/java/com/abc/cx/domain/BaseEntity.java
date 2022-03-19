package com.abc.cx.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:29 2021/11/6
 **/
@MappedSuperclass
@Data
@ApiModel(value = "BaseEntity", description = "实体基类")
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 7229711996915758071L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键自增列")
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键自增列'")
    protected Long id;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time", columnDefinition = "datetime COMMENT '创建时间'")
    @CreationTimestamp
    private Date createTime;

    /**
     * 创建人
     */
    @JsonIgnore
    @ManyToOne
    @ApiModelProperty(value = "创建人")
    @JoinColumn(name = "create_user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT), columnDefinition = "bigint COMMENT '创建人ID'")
    private User createUser;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time", columnDefinition = "datetime COMMENT '更新时间'")
    @UpdateTimestamp
    private Date updateTime;

    /**
     * 更新人
     */
    @JsonIgnore
    @ManyToOne
    @ApiModelProperty(value = "更新人")
    @JoinColumn(name = "update_user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT), columnDefinition = "bigint COMMENT '更新人ID'")
    private User updateUser;

    /**
     * 是否已删除 0：未删除 1：已删除
     */
    @ApiModelProperty(value = "是否已删除 0：未删除 1：已删除")
    @Column(name = "is_delete", columnDefinition = "int COMMENT '是否已删除 0：未删除 1：已删除'")
    private Integer isDelete = 0;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Column(name = "remark", columnDefinition = "varchar(255) COMMENT '备注'")
    private String remark;

}

