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
 * @Decription: 字典类型
 * @Date: 22:43 2021/11/6
 **/
@Entity
@Data
@ApiModel(value = "DictType", description = "字典类型")
@Table(name = "sys_dict_type")
@org.hibernate.annotations.Table(appliesTo = "sys_dict_type", comment = "字典类型表")
public class DictType extends BaseEntity {

    private static final long serialVersionUID = 4823582577478643170L;

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    @Column(name = "name", columnDefinition = "varchar(255) COMMENT '字典名称'")
    private String name;

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型")
    @Column(name = "type", columnDefinition = "varchar(255) COMMENT '字典类型'")
    private String type;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）")
    @Column(name = "status", columnDefinition = "int COMMENT '状态（0正常 1停用）'")
    private EnableStatusEnum status;

}
