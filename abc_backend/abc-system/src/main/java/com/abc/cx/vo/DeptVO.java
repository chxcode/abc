package com.abc.cx.vo;

import com.abc.cx.domain.Dept;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 23:35 2021/11/6
 **/
@Data
@Builder
public class DeptVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String name;

    /**
     * 父部门
     */
    @ApiModelProperty(value = "父部门")
    private Dept parentDept;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    private String leader;

    /**
     * 部门联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 部门邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 部门状态
     */
    @ApiModelProperty(value = "部门状态")
    private String status;

}
