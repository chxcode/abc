package com.abc.cx.domain;

import com.abc.cx.enums.EnableStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: ChangXuan
 * @Decription: 职位
 * @Date: 22:48 2021/11/6
 **/
@Entity
@Data
@ApiModel(value = "Post", description = "职位")
@Table(name = "sys_post")
@org.hibernate.annotations.Table(appliesTo = "sys_post", comment = "职位表")
public class Post extends BaseEntity {

    private static final long serialVersionUID = 3322773939527793338L;

    /**
     * 职位编码
     */
    @ApiModelProperty(value = "职位编码")
    @Column(name = "code", columnDefinition = "varchar(255) COMMENT '职位编码'")
    private String code;

    /**
     * 职位名称
     */
    @ApiModelProperty(value = "职位名称")
    @Column(name = "name", columnDefinition = "varchar(255) COMMENT '岗位名称'")
    private String name;

    /**
     * 职位排序
     */
    @ApiModelProperty(value = "职位排序")
    @Column(name = "order_num", columnDefinition = "int COMMENT '岗位排序'")
    private Integer orderNum;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）")
    @Column(name = "status", columnDefinition = "int COMMENT '状态（0正常 1停用）'")
    private EnableStatusEnum status;

}

