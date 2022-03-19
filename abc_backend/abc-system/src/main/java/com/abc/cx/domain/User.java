package com.abc.cx.domain;

import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.enums.GenderTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.groups.Default;
import java.util.*;

/**
 * @Author: ChangXuan
 * @Decription: 用户
 * @Date: 22:34 2021/11/6
 **/
@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "User", description = "用户")
@Table(name = "sys_user")
@org.hibernate.annotations.Table(appliesTo = "sys_user", comment = "用户表")
public class User implements UserDetails {

    private static final long serialVersionUID = 103301379401198743L;

    /**
     * 修改本人信息校验组
     */
    public interface UpdateOwn {
    }

    /**
     * 默认校验组
     */
    public interface Valid extends Default {
    }

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    @Column(name = "id", unique = true, nullable = false, length = 19, columnDefinition = "bigint COMMENT '主键自增列'")
    private Long id;

    /**
     * 照片
     */
    @ApiModelProperty(value = "照片")
    @Column(name = "photo", columnDefinition = "varchar(255) COMMENT '照片'")
    private String photo;

    /**
     * 用户名
     */
    @Length(min = 4, max = 30, message = "用户名长度为4-30个字符")
    @ApiModelProperty(value = "用户名")
    @Column(name = "user_name", columnDefinition = "varchar(255) COMMENT '用户名'")
    private String username;

    /**
     * 密码（加密后）
     */
    @ApiModelProperty(value = "密码")
    @Column(name = "password", columnDefinition = "varchar(255) COMMENT '密码'")
    private String password;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    @Column(name = "real_name", columnDefinition = "varchar(255) COMMENT '用户姓名'")
    private String realName;

    /**
     * 性别 （0女 1男）
     */
    @ApiModelProperty(value = "性别 （0女 1男）")
    @Column(name = "gender", columnDefinition = "int COMMENT '性别 （0女 1男）'")
    private GenderTypeEnum gender;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Column(name = "email", columnDefinition = "varchar(255) COMMENT '邮箱'")
    private String email;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    @Column(name = "telephone", columnDefinition = "varchar(20) COMMENT '电话'")
    private String telephone;

    /**
     * QQ
     */
    @ApiModelProperty(value = "QQ")
    @Column(name = "qq", columnDefinition = "varchar(20) COMMENT 'QQ'")
    private String qq;

    /**
     * 微信
     */
    @ApiModelProperty(value = "微信")
    @Column(name = "wechat", columnDefinition = "varchar(50) COMMENT '微信'")
    private String wechat;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    @Column(name = "nickname", columnDefinition = "varchar(50) COMMENT '昵称'")
    private String nickname;

    /**
     * 电话国际代码
     */
    @ApiModelProperty(value = "电话国际代码")
    @Column(name = "telephone_code", columnDefinition = "varchar(8) COMMENT '电话国际代码'")
    private String telephoneCode;

    /**
     * 岗位
     */
    @ApiModelProperty(value = "岗位")
    @Column(name = "post", columnDefinition = "varchar(255) COMMENT '岗位'")
    private String post;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "注册时间")
    @Column(name = "create_time", updatable = false, columnDefinition = "datetime COMMENT '创建时间'")
    @CreationTimestamp
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time", columnDefinition = "datetime COMMENT '更新时间'")
    @UpdateTimestamp
    private Date updateTime;

    /**
     * 启用状态（0启用 1禁用）
     */
    @ApiModelProperty(value = "启用状态（0启用 1禁用）")
    @Column(name = "status", columnDefinition = "int COMMENT '启用状态（0启用 1禁用）'")
    private EnableStatusEnum status;

    /**
     * 是否已删除，0：未删除；1：已删除
     */
    @ApiModelProperty(value = "是否已删除，0：未删除；1：已删除")
    @Column(name = "is_delete", columnDefinition = "int COMMENT '是否已删除，0：未删除；1：已删除'")
    private Integer isDelete = 0;


    /**
     * 用户部门
     */
    @ManyToOne
    @ApiModelProperty(value = "用户部门")
    @JoinColumn(name = "dept_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT), columnDefinition = "bigint COMMENT '用户部门ID'")
    private Dept dept;

    /**
     * 用户角色
     */
    @ManyToOne
    @ApiModelProperty(value = "用户角色")
    @JoinColumn(name = "role_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT), columnDefinition = "bigint COMMENT '用户角色ID'")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (this.role != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getKey()));
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}