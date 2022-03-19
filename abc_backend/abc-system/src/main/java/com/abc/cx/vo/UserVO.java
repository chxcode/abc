package com.abc.cx.vo;

import com.abc.cx.domain.Dept;
import com.abc.cx.domain.Menu;
import com.abc.cx.domain.Post;
import com.abc.cx.domain.Role;
import com.abc.cx.enums.GenderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.groups.Default;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 23:37 2021/11/6
 **/
@Data
@Builder
public class UserVO {

    /**
     * 更新时校验组
     */
    public interface Update {
    }

    /**
     * 添加时校验组
     */
    public interface Valid extends Default {
    }

    public @interface Register{}

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 照片
     */
    @ApiModelProperty(value = "照片")
    private String photo;

    /**
     * 用户名
     */
    @Length(min = 4, max = 30, message = "用户名长度为4-30个字符", groups = {Register.class})
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String realName;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式不正确", groups = {Register.class})
    private String email;

    /**
     * 密码（加密后）
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String telephone;

    /**
     * 性别 （0女 1男）
     */
    @ApiModelProperty(value = "性别，接收导入数据")
    private GenderTypeEnum gender;

    /**
     * 岗位
     */
    @ApiModelProperty(value = "岗位")
    private String post;

    /**
     * 用状态（0启用 1禁用）
     */
    @ApiModelProperty(value = "用状态（0启用 1禁用）")
    private String status;

    /**
     * 是否已删除，0：未删除；1：已删除
     */
    @ApiModelProperty(value = "是否已删除，0：未删除；1：已删除")
    private Integer isDelete;

    /**
     * 用户部门
     */
    @ApiModelProperty(value = "用户部门")
    private Dept dept;

    /**
     * 用户角色
     */
    @ApiModelProperty(value = "用户角色")
    private Role role;

    /**
     * 岗位列表
     */
    @ApiModelProperty(value = "岗位列表")
    private List<Post> postList;

    /**
     * 菜单列表
     */
    @ApiModelProperty(value = "菜单列表")
    private List<Menu> menuList;


    private String wechat;

}
