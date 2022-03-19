package com.abc.cx.domain;

import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.enums.OperateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: ChangXuan
 * @Decription: 操作日志
 * @Date: 22:46 2021/11/6
 **/
@Entity
@Data
@ApiModel(value = "OperateLog", description = "操作日志")
@Table(name = "sys_operate_log")
@org.hibernate.annotations.Table(appliesTo = "sys_operate_log", comment = "操作日志表")
public class OperateLog extends BaseEntity {

    private static final long serialVersionUID = -5624142528998100502L;

    /**
     * 操作模块
     */
    @ApiModelProperty(value = "操作模块")
    @Column(name = "title", columnDefinition = "varchar(255) COMMENT '操作模块'")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退 8生成代码 9清空数据）
     */
    @ApiModelProperty(value = "业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退 8生成代码 9清空数据）")
    @Column(name = "operate_type", columnDefinition = "int COMMENT '业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退 8生成代码 9清空数据）'")
    private OperateTypeEnum type;

    /**
     * 请求方法
     */
    @ApiModelProperty(value = "请求方法")
    @Column(name = "method", columnDefinition = "varchar(255) COMMENT '请求方法'")
    private String method;

    /**
     * 操作人员
     */
    @ApiModelProperty(value = "操作人员")
    @Column(name = "operator", columnDefinition = "varchar(255) COMMENT '操作人员'")
    private String operator;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @Column(name = "dept_name", columnDefinition = "varchar(255) COMMENT '部门名称'")
    private String deptName;

    /**
     * 请求url
     */
    @ApiModelProperty(value = "请求url")
    @Column(name = "url", columnDefinition = "varchar(255) COMMENT '请求url'")
    private String url;

    /**
     * 操作地址
     */
    @ApiModelProperty(value = "操作地址")
    @Column(name = "ip", columnDefinition = "varchar(255) COMMENT '操作地址'")
    private String ip;

    /**
     * 操作地点
     */
    @ApiModelProperty(value = "操作地点")
    @Column(name = "location", columnDefinition = "varchar(255) COMMENT '部门名称'")
    private String location;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    @Column(name = "param", columnDefinition = "longtext COMMENT '请求参数'")
    private String param;

    /**
     * 操作状态（0正常 1异常）
     */
    @ApiModelProperty(value = "操作状态（0正常 1异常）")
    @Column(name = "status", columnDefinition = "int COMMENT '操作状态（0正常 1异常）'")
    private OperateStatusEnum status;

    /**
     * 错误消息
     */
    @ApiModelProperty(value = "错误消息")
    @Column(name = "error_msg", columnDefinition = "varchar(255) COMMENT '错误消息'")
    private String errorMsg;

    /**
     * 是否已删除 0：未删除 1：已删除
     */
    @ApiModelProperty(value = "是否已删除 0：未删除 1：已删除")
    @Column(name = "is_delete", columnDefinition = "int COMMENT '是否已删除 0：未删除 1：已删除'")
    private Integer isDelete = 0;

}
