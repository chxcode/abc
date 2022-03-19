package com.abc.cx.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: ChangXuan
 * @Decription: 参数
 * @Date: 22:47 2021/11/6
 **/
@Entity
@Data
@ApiModel(value = "Param", description = "参数")
@Table(name = "sys_param")
@org.hibernate.annotations.Table(appliesTo = "sys_param", comment = "参数表")
public class Param extends BaseEntity {

    private static final long serialVersionUID = 242952284557488828L;

    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称")
    @Column(name = "name", columnDefinition = "varchar(255) COMMENT '参数名称'")
    private String name;

    /**
     * 参数键名
     */
    @ApiModelProperty(value = "参数键名")
    @Column(name = "param_key", columnDefinition = "varchar(255) COMMENT '参数键名'")
    private String key;

    /**
     * 参数键值
     */
    @ApiModelProperty(value = "参数键值")
    @Column(name = "value", columnDefinition = "longtext COMMENT '参数键值'")
    private String value;

    /**
     * 系统内置（0是 1否）
     */
    @ApiModelProperty(value = "系统内置（0是 1否）")
    @Column(name = "type", columnDefinition = "int COMMENT '系统内置（0是 1否）'")
    private Integer type;

}
