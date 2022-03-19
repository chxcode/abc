package com.abc.cx.domain;

import com.abc.cx.enums.EnableStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 角色
 * @Date: 22:41 2021/11/6
 **/
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Role", description = "角色")
@Table(name = "sys_role")
@org.hibernate.annotations.Table(appliesTo = "sys_role", comment = "角色表")
public class Role extends BaseEntity {

    private static final long serialVersionUID = -8577525906401273141L;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    @Column(name = "name", columnDefinition = "varchar(255) COMMENT '角色名称'")
    private String name;

    /**
     * 角色权限
     */
    @ApiModelProperty(value = "角色权限")
    @Column(name = "role_key", columnDefinition = "varchar(255) COMMENT '角色权限'")
    private String key;

    /**
     * 角色排序
     */
    @ApiModelProperty(value = "角色排序")
    @Column(name = "order_num", columnDefinition = "int COMMENT '角色排序'")
    private Integer orderNum;

    /**
     * 角色状态（0正常 1停用）
     */
    @ApiModelProperty(value = "角色状态（0正常 1停用）")
    @Column(name = "status", columnDefinition = "int COMMENT '角色状态（0正常 1停用）'")
    private EnableStatusEnum status;

    /**
     * 菜单列表
     */
    @Transient
    @ApiModelProperty(value = "菜单列表")
    private List<Menu> menuList;

    /**
     * 部门列表
     */
    @Transient
    @ApiModelProperty(value = "部门列表")
    private List<Dept> deptList;

    /**
     * 菜单key列表
     */
    @Transient
    @ApiModelProperty(value = "菜单key列表")
    private List<String> menuKeyList;

    /**
     * 部门key列表
     */
    @Transient
    @ApiModelProperty(value = "部门key列表")
    private List<String> deptKeyList;

}
