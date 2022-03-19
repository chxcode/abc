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
 * @Decription: 字典数据
 * @Date: 22:42 2021/11/6
 **/
@Entity
@Data
@ApiModel(value = "DictData", description = "字典数据")
@Table(name = "sys_dict_data")
@org.hibernate.annotations.Table(appliesTo = "sys_dict_data", comment = "字典数据表")
public class DictData extends BaseEntity {

    private static final long serialVersionUID = -5742770211660711507L;

    /**
     * 字典排序
     */
    @ApiModelProperty(value = "字典排序")
    @Column(name = "order_num", columnDefinition = "int COMMENT '字典排序'")
    private Integer orderNum;

    /**
     * 字典标签
     */
    @ApiModelProperty(value = "字典标签")
    @Column(name = "label", columnDefinition = "varchar(255) COMMENT '字典标签'")
    private String label;

    /**
     * 字典键值
     */
    @ApiModelProperty(value = "字典键值")
    @Column(name = "value", columnDefinition = "varchar(255) COMMENT '字典键值'")
    private String value;

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型")
    @Column(name = "type", columnDefinition = "varchar(255) COMMENT '字典类型'")
    private String type;

    /**
     * 是否默认（Y是 N否）
     */
    @ApiModelProperty(value = "是否默认（0是 1否）")
    @Column(name = "is_default", columnDefinition = "int COMMENT '是否默认（0是 1否）'")
    private Integer isDefault;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）")
    @Column(name = "status", columnDefinition = "int COMMENT '状态（0正常 1停用）'")
    private EnableStatusEnum status;

}
