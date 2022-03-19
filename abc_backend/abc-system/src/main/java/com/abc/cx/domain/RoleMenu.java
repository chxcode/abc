package com.abc.cx.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @Author: ChangXuan
 * @Decription: 角色菜单
 * @Date: 22:49 2021/11/6
 **/
@Entity
@Data
@ApiModel(value = "RoleMenu", description = "角色菜单")
@Table(name = "sys_role_menu")
@org.hibernate.annotations.Table(appliesTo = "sys_role_menu", comment = "角色菜单表")
public class RoleMenu {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键自增列'")
    protected Long id;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    @Column(name = "role_id", columnDefinition = "bigint COMMENT '角色ID'")
    private Long roleId;

    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID")
    @Column(name = "menu_id", columnDefinition = "bigint COMMENT '菜单ID'")
    private Long menuId;

}
