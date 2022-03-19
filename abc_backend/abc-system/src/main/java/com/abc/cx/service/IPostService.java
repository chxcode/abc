package com.abc.cx.service;

import com.abc.cx.domain.Post;
import com.abc.cx.vo.Ret;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 10:25 2020/5/8
 **/
public interface IPostService {

    /**
     * 获取职位列表
     *
     * @return ret
     */
    Ret listAll();

    /**
     * 获取职位分页列表
     *
     * @param name     名称
     * @param page     页码
     * @param pageSize 页数
     * @return 职位对象列表结果
     */
    Ret pagePosts(String name, Integer page, Integer pageSize);

    /**
     * 获取职位详细信息
     *
     * @param id 职位对象id
     * @return 职位对象详情结果
     */
    Ret getPost(Long id);

    /**
     * 新增职位
     *
     * @param post 职位对象
     * @return 增加结果
     */
    Ret insertPost(Post post);

    /**
     * 修改职位
     *
     * @param post 职位对象
     * @return 修改结果
     */
    Ret updatePost(Post post);

    /**
     * 删除职位
     *
     * @param id 职位对象id
     * @return 删除结果
     */
    Ret deletePost(Long id);

}
