package com.abc.cx.domain;

import com.abc.cx.enums.EnableStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 部门
 * @Date: 22:40 2021/11/6
 **/
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Dept", description = "部门")
@Table(name = "sys_dept")
@org.hibernate.annotations.Table(appliesTo = "sys_dept", comment = "部门表")
public class Dept extends BaseEntity {

    private static final long serialVersionUID = 6084725952921715644L;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @Column(name = "name", columnDefinition = "varchar(255) COMMENT '部门名称'")
    private String name;

    /**
     * 父部门
     */
    @ManyToOne
    @ApiModelProperty(value = "父部门")
    @JoinColumn(name = "parent_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT), columnDefinition = "bigint COMMENT '父部门ID'")
    private Dept parentDept;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    @Column(name = "order_num", columnDefinition = "int COMMENT '显示顺序'")
    private Integer orderNum;

    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    @Column(name = "leader", columnDefinition = "varchar(255) COMMENT '负责人'")
    private String leader;

    /**
     * 部门联系电话
     */
    @ApiModelProperty(value = "联系电话")
    @Column(name = "phone", columnDefinition = "varchar(255) COMMENT '联系电话'")
    private String phone;

    /**
     * 部门邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Column(name = "email", columnDefinition = "varchar(255) COMMENT '邮箱'")
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    @ApiModelProperty(value = "部门状态:0正常,1停用")
    @Column(name = "status", columnDefinition = "int COMMENT '部门状态:0正常,1停用'")
    private EnableStatusEnum status;

    /**
     * 子部门列表
     */
    @Transient
    @ApiModelProperty(value = "子部门列表")
    private List<Dept> children;

}
