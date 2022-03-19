package com.abc.cx.enums;

import lombok.Getter;

/**
 * @Author: ChangXuan
 * @Decription: 请求结果
 * @Date: 22:37 2021/11/6
 **/
@Getter
public enum ResultEnum implements CodeEnum {

    /**
     * SUCCESS
     */
    SUCCESS(0, "成功"),

    /**
     * PARAM_ERROR
     */
    PARAM_ERROR(1, "参数不正确"),

    /**
     * PARENT_IS_CHILD
     */
    PARENT_IS_CHILD(10, "父节点不能设置成该节点的子节点或本身"),

    /**
     * CHILD_NOT_DELETE
     */
    CHILD_NOT_DELETE(11, "存在未删除的子节点，请先删除子节点"),

    /**
     * ORG_NOT_FOUND
     */
    DEPT_NOT_FOUND(20, "部门已删除或不存在"),

    /**
     * DEPT_EXIT
     */
    DEPT_EXIT(21, "父级下已存在同名部门"),

    /**
     * DEPT_ROLE_USE
     */
    DEPT_ROLE_USE(22, "存在角色数据权限中设置为该部门"),

    /**
     * DEPT_USER_USE
     */
    DEPT_USER_USE(23, "存在用户部门设置为该部门"),

    /**
     * DEPT_CHILD_NOT_DISABLED
     */
    DEPT_CHILD_NOT_DISABLED(24, "存在未禁用的子部门，请先禁用子部门"),

    /**
     * DEPT_PARENT_DISABLED
     */
    DEPT_PARENT_DISABLED(25, "父部门禁用，请先取消禁用父部门"),

    /**
     * DEPT_IS_DEFAULT
     */
    DEPT_IS_DEFAULT(26, "无法禁用或删除默认部门"),

    /**
     * MENU__NOT_FOUND
     */
    MENU_NOT_FOUND(30, "菜单已删除或不存在"),

    /**
     * MENU_NAME_EXIT
     */
    MENU_NAME_EXIT(31, "父级下已存在同名菜单"),

    /**
     * AUTH_MENU_NONE
     */
    AUTH_MENU_NONE(32, "没有菜单权限"),

    /**
     * MENU_ROLE_USE
     */
    MENU_ROLE_USE(33, "菜单已在角色中使用，请先取消"),

    /**
     * MENU_CHILD_VISIBLE
     */
    MENU_CHILD_VISIBLE(34, "存在显示的子菜单，请先隐藏子菜单"),

    /**
     * MENU_PARENT_HIDE
     */
    MENU_PARENT_HIDE(35, "父菜单隐藏，请先取消隐藏父菜单"),

    /**
     * MENU_URL_EXIT
     */
    MENU_URL_EXIT(36, "已存在相同URL菜单"),

    /**
     * ROLE_NOT_FOUND
     */
    ROLE_NOT_FOUND(50, "角色已删除或不存在"),

    /**
     * ROLE_EXIT
     */
    ROLE_EXIT(51, "角色已存在"),

    /**
     * ROLE_USER_USE
     */
    ROLE_USER_USE(52, "存在用户角色设置为该角色"),

    /**
     * ROLE_IS_ADMIN
     */
    ROLE_IS_ADMIN(53, "管理员角色无法禁用或删除"),

    /**
     * ROLE_HAS_NO_DEPT
     */
    ROLE_HAS_NO_DEPT(54, "用户角色未设置数据权限"),

    /**
     * USER_NOT_FOUND
     */
    USER_NOT_FOUND(60, "用户不存在或已删除"),

    /**
     * USER_EXIST
     */
    USER_EXIST(61, "用户名已存在"),

    /**
     * USER_NOT_LOGIN
     */
    USER_NOT_LOGIN(62, "登录已超时，请重新登录"),

    /**
     * USER_IS_ADMIN
     */
    USER_IS_ADMIN(63, "管理员账号无法修改"),

    /**
     * USER_ADMIN_ROLE
     */
    USER_ADMIN_ROLE(64, "管理员账号无法更换非管理员角色"),

    /**
     * USER_DELETE_SELF
     */
    USER_DELETE_SELF(65, "无法删除当前登录账号"),

    /**
     * USER_EMAIL_EXIST
     */
    USER_EMAIL_EXIST(66, "此邮箱已注册"),

    /**
     * ERROR
     */
    SERVER_ERROR(500, "服务出现异常"),

    /**
     * FILE_EMPTY
     */
    FILE_EMPTY(70, "文件内容为空"),

    /**
     * FILE_FORMAT_DANGER
     */
    FILE_FORMAT_DANGER(71, "文件格式不允许上传"),

    /**
     * FILE_NOT_EXISTS
     */
    FILE_NOT_EXISTS(72, "文件不存在"),

    /**
     * TXT_TYPE_MUST
     */
    TXT_TYPE_MUST(140, "文件格式只能为txt"),

    /**
     * SERVER_SUCCESS
     */
    SERVER_SUCCESS(200, "成功"),

    /**
     * LOGIN_FAILED
     */
    LOGIN_FAILED(201, "登录失败，用户名或密码错误"),

    /**
     * AUTH_FAILD
     */
    AUTH_FAILD(403, "权限不足"),


    /**
     * AUTH_DATA_ERROR
     */
    AUTH_DATA_ERROR(407, "数据出现错误");

    private final Integer code;

    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
