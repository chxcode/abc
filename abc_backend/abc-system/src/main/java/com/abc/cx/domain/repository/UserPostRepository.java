package com.abc.cx.domain.repository;

import com.abc.cx.domain.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 用户职位
 * @Date: 22:31 2021/11/6
 **/
public interface UserPostRepository extends JpaRepository<UserPost, Long> {

    /**
     * 删除用户所有岗位
     *
     * @param userId 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByUserId(Long userId);


    /**
     * 获得用户所有岗位列表
     *
     * @param userId 用户id
     * @return 对象
     */
    List<UserPost> findAllByUserId(Long userId);

}
