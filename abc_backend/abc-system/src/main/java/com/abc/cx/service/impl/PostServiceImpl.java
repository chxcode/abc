package com.abc.cx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.abc.cx.domain.Post;
import com.abc.cx.domain.User;
import com.abc.cx.domain.repository.PostRepository;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.service.IPostService;
import com.abc.cx.vo.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:48 2021/11/6
 **/
@Service
public class PostServiceImpl implements IPostService {

    private UserServiceImpl userService;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    private PostRepository postRepository;

    @Autowired
    public void setPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * 获取职位列表
     *
     * @return ret
     */
    @Override
    public Ret listAll() {
        List<Post> list = postRepository.findAllByIsDeleteAndStatusOrderByOrderNumAscCreateTimeDesc(0, EnableStatusEnum.ENABLED);
        return Ret.ok("list", list);
    }

    /**
     * 获取职位分页列表
     *
     * @param name     名称
     * @param page     页码
     * @param pageSize 页数
     * @return 职位对象列表结果
     */
    @Override
    public Ret pagePosts(String name, Integer page, Integer pageSize) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "orderNum");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        Sort sort = Sort.by(orders);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        Specification<Post> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("isDelete"), 0));
            if (StrUtil.isNotEmpty(name)) {
                predicateList.add(builder.isNotNull(root.get("name")));
                predicateList.add(builder.like(root.get("name"), "%" + name + "%"));
            }
            Predicate[] arrayType = new Predicate[predicateList.size()];
            return builder.and(predicateList.toArray(arrayType));
        };
        Page<Post> list = postRepository.findAll(specification, pageRequest);
        return Ret.ok("list", list);
    }

    /**
     * 获取职位详细信息
     *
     * @return ret
     */
    @Override
    public Ret getPost(Long id) {
        Post post = postRepository.findTopById(id);
        return Ret.ok("post", post);
    }

    /**
     * 增加职位
     *
     * @param post 职位对象
     * @return 增加结果
     */
    @Override
    public Ret insertPost(Post post) {
        User user = userService.getCurrentLoginUser();
        post.setCreateUser(user);
        postRepository.save(post);
        return Ret.ok("message", "增加职位成功");
    }

    /**
     * 修改职位
     *
     * @param post 职位对象
     * @return 修改结果
     */
    @Override
    public Ret updatePost(Post post) {
        User user = userService.getCurrentLoginUser();
        Post oldPost = postRepository.findTopById(post.getId());
        BeanUtil.copyProperties(post, oldPost, CopyOptions.create().setIgnoreNullValue(true));
        oldPost.setUpdateUser(user);
        postRepository.save(oldPost);
        return Ret.ok("message", "修改职位成功");
    }

    /**
     * 删除职位
     *
     * @param id 职位对象id
     * @return 删除结果
     */
    @Override
    public Ret deletePost(Long id) {
        User user = userService.getCurrentLoginUser();
        Post post = postRepository.findTopById(id);
        post.setIsDelete(1);
        post.setUpdateUser(user);
        postRepository.save(post);
        return Ret.ok("message", "删除职位成功");
    }

}
