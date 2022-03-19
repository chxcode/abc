package com.abc.cx.domain.repository;

import com.abc.cx.domain.Post;
import com.abc.cx.enums.EnableStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 职位
 * @Date: 22:29 2021/11/6
 **/
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 获取相应id的职位
     *
     * @param id 职位id
     * @return 职位对象
     */
    Post findTopById(Long id);

    /**
     * 取相应条件的职位列表
     *
     * @param isDelete 是否删除
     * @param status   职位状态
     * @return 职位对象列表
     */
    List<Post> findAllByIsDeleteAndStatusOrderByOrderNumAscCreateTimeDesc(Integer isDelete, EnableStatusEnum status);

    /**
     * 获取相应条件的职位分页列表
     *
     * @param specification 条件
     * @param pageable      分页
     * @return 职位对象分页列表
     */
    Page<Post> findAll(Specification specification, Pageable pageable);

}
