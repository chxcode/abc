package com.abc.cx.domain;

import com.abc.cx.enums.OperateStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: ChangXuan
 * @Decription: 登录日志
 * @Date: 22:43 2021/11/6
 **/
@Entity
@Data
@ApiModel(value = "LoginInfo", description = "登录日志")
@Table(name = "sys_login_info")
@org.hibernate.annotations.Table(appliesTo = "sys_login_info", comment = "登录日志表")
public class LoginInfo {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键自增列'")
    protected Long id;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    @Column(name = "user_name", columnDefinition = "varchar(255) COMMENT '用户账号'")
    private String username;

    /**
     * 登录状态 0成功 1失败
     */
    @ApiModelProperty(value = "登录状态 0成功 1失败")
    @Column(name = "status", columnDefinition = "int COMMENT '登录状态 0成功 1失败'")
    private OperateStatusEnum status;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value = "登录IP地址")
    @Column(name = "ip", columnDefinition = "varchar(255) COMMENT '登录IP地址'")
    private String ip;

    /**
     * 登录地点
     */
    @ApiModelProperty(value = "登录地点")
    @Column(name = "location", columnDefinition = "varchar(255) COMMENT '登录地点'")
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
     * 提示消息
     */
    @ApiModelProperty(value = "提示消息")
    @Column(name = "msg", columnDefinition = "varchar(255) COMMENT '提示消息'")
    private String msg;

    /**
     * 访问时间
     */
    @ApiModelProperty(value = "访问时间")
    @Column(name = "time", columnDefinition = "datetime COMMENT '访问时间'")
    private Date time;

}
