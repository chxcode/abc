package com.abc.cx.domain;

import com.abc.cx.enums.VisibleTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 菜单
 * @Date: 22:42 2021/11/6
 **/
@Entity
@Data
@ApiModel(value = "Menu", description = "菜单")
@Table(name = "sys_menu")
@org.hibernate.annotations.Table(appliesTo = "sys_menu", comment = "菜单表")
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 4040122802374435777L;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    @Column(name = "name", columnDefinition = "varchar(255) COMMENT '菜单名称'")
    private String name;

    /**
     * 父菜单
     */
    @ManyToOne
    @ApiModelProperty(value = "父菜单")
    @JoinColumn(name = "parent_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT), columnDefinition = "bigint COMMENT '父菜单ID'")
    private Menu parentMenu;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    @Column(name = "order_num", columnDefinition = "int COMMENT '显示顺序'")
    private Integer orderNum;

    /**
     * URL
     */
    @ApiModelProperty(value = "URL")
    @Column(name = "url", columnDefinition = "varchar(255) COMMENT 'URL'")
    private String url;

    /**
     * 类型:M目录,C菜单,B按钮
     */
    @ApiModelProperty(value = "类型:M目录,C菜单,B按钮")
    @Column(name = "type", columnDefinition = "varchar(255) COMMENT '类型:M目录,C菜单,B按钮'")
    private String type;

    /**
     * 菜单状态:0显示,1隐藏
     */
    @ApiModelProperty(value = "菜单状态:0显示,1隐藏")
    @Column(name = "visible", columnDefinition = "int COMMENT '菜单状态:0显示,1隐藏'")
    private VisibleTypeEnum visible;

    /**
     * 权限字符串
     */
    @ApiModelProperty(value = "权限字符串")
    @Column(name = "perms", columnDefinition = "varchar(255) COMMENT '权限字符串'")
    private String perms;

    /**
     * 方法
     */
    @ApiModelProperty(value = "方法")
    @Column(name = "method", columnDefinition = "varchar(255) COMMENT '方法'")
    private String method;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    @Column(name = "ico", columnDefinition = "varchar(255) COMMENT '菜单图标'")
    private String ico;

    @Transient
    @ApiModelProperty(value = "子菜单列表")
    private List<Menu> children;

}
