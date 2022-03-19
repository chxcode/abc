package com.abc.cx.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @Author: ChangXuan
 * @Decription: 用户岗位
 * @Date: 22:51 2021/11/6
 **/
@Entity
@Data
@Table(name = "sys_user_post")
@ApiModel(value = "UserPost", description = "用户岗位")
@org.hibernate.annotations.Table(appliesTo = "sys_user_post", comment = "用户岗位表")
public class UserPost {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键自增列'")
    protected Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @Column(name = "user_id", columnDefinition = "bigint COMMENT '用户ID'")
    private Long userId;

    /**
     * 岗位ID
     */
    @ApiModelProperty(value = "岗位ID")
    @Column(name = "post_id", columnDefinition = "bigint COMMENT '岗位ID'")
    private Long postId;

}
