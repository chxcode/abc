package com.abc.cx.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @Author: ChangXuan
 * @Decription: 角色部门
 * @Date: 22:49 2021/11/6
 **/
@Entity
@Data
@ApiModel(value = "RoleDept", description = "角色部门")
@Table(name = "sys_role_dept")
@org.hibernate.annotations.Table(appliesTo = "sys_role_dept", comment = "角色部门表")
public class RoleDept {

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
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    @Column(name = "dept_id", columnDefinition = "bigint COMMENT '部门ID'")
    private Long deptId;

}
