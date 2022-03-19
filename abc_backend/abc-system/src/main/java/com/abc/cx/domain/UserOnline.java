package com.abc.cx.domain;

import com.abc.cx.enums.OperateStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: ChangXuan
 * @Decription: 在线用户
 * @Date: 22:50 2021/11/6
 **/
@Entity
@Data
@ApiModel(value = "UserOnline", description = "在线用户")
@Table(name = "sys_user_online")
@org.hibernate.annotations.Table(appliesTo = "sys_user_online", comment = "在线用户表")
public class UserOnline {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键自增列'")
    private Long id;

    /**
     * 用户会话id
     */
    @ApiModelProperty(value = "用户会话id")
    @Column(name = "session_id", columnDefinition = "varchar(255) COMMENT '用户会话id'")
    private String sessionId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @Column(name = "dept_name", columnDefinition = "varchar(255) COMMENT '部门名称'")
    private String deptName;

    /**
     * 登录名称
     */
    @ApiModelProperty(value = "登录名称")
    @Column(name = "username", columnDefinition = "varchar(255) COMMENT '部门名称'")
    private String username;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value = "登录IP地址")
    @Column(name = "ip", columnDefinition = "varchar(255) COMMENT '登录IP地址'")
    private String ip;

    /**
     * 登录地址
     */
    @ApiModelProperty(value = "登录地址")
    @Column(name = "location", columnDefinition = "varchar(255) COMMENT '登录地址'")
    private String location;

    /**
     * 浏览器类型
     */
    @ApiModelProperty(value = "浏览器类型")
    @Column(name = "browser", columnDefinition = "varchar(255) COMMENT '浏览器类型'")
    private String browser;

    /**
     * 操作系统
     */
    @ApiModelProperty(value = "操作系统")
    @Column(name = "os", columnDefinition = "varchar(255) COMMENT '操作系统'")
    private String os;

    /**
     * session创建时间
     */
    @ApiModelProperty(value = "session创建时间")
    @Column(name = "session_create_time", columnDefinition = "datetime COMMENT 'session创建时间'")
    private Date sessionCreateTime;

    /**
     * session最后访问时间
     */
    @ApiModelProperty(value = "session最后访问时间")
    @Column(name = "last_access_time", columnDefinition = "datetime COMMENT 'session最后访问时间'")
    private Date lastAccessTime;

    /**
     * 超时时间，单位为分钟
     */
    @ApiModelProperty(value = "超时时间，单位为分钟")
    @Column(name = "expire_time", columnDefinition = "bigint COMMENT '超时时间，单位为分钟'")
    private Long expireTime;

    /**
     * 登录状态
     */
    @ApiModelProperty(value = "登录状态")
    @Column(name = "status", columnDefinition = "int COMMENT '登录状态'")
    private OperateStatusEnum status;

}
