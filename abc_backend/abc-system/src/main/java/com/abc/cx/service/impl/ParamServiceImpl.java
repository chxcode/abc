package com.abc.cx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.abc.cx.domain.Param;
import com.abc.cx.domain.User;
import com.abc.cx.domain.repository.ParamRepository;
import com.abc.cx.service.IParamService;
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
public class ParamServiceImpl implements IParamService {

    private UserServiceImpl userService;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    private ParamRepository paramRepository;

    @Autowired
    public void setParamRepository(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    /**
     * 获取参数分页列表
     *
     * @param name     名称
     * @param key      键值
     * @param page     页码
     * @param pageSize 页数
     * @return 参数对象列表结果
     */
    @Override
    public Ret pageParams(String name, String key, Integer page, Integer pageSize) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "createTime");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order);
        Sort sort = Sort.by(orders);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        Specification<Param> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("isDelete"), 0));
            if (StrUtil.isNotEmpty(name)) {
                predicateList.add(builder.isNotNull(root.get("name")));
                predicateList.add(builder.like(root.get("name"), "%" + name + "%"));
            }
            if (StrUtil.isNotEmpty(key)) {
                predicateList.add(builder.isNotNull(root.get("key")));
                predicateList.add(builder.like(root.get("key"), "%" + key + "%"));
            }
            Predicate[] arrayType = new Predicate[predicateList.size()];
            return builder.and(predicateList.toArray(arrayType));
        };
        Page<Param> list = paramRepository.findAll(specification, pageRequest);
        return Ret.ok("list", list);
    }

    /**
     * 获取参数详情
     *
     * @return ret
     */
    @Override
    public Ret getParam(Long id) {
        Param param = paramRepository.findTopById(id);
        return Ret.ok("param", param);
    }

    /**
     * 增加参数
     *
     * @param param 参数对象
     * @return 增加结果
     */
    @Override
    public Ret insertParam(Param param) {
        User user = userService.getCurrentLoginUser();
        param.setCreateUser(user);
        paramRepository.save(param);
        return Ret.ok("message", "增加参数成功");
    }

    /**
     * 修改参数
     *
     * @param param 参数对象
     * @return 修改结果
     */
    @Override
    public Ret updateParam(Param param) {
        User user = userService.getCurrentLoginUser();
        Param oldParam = paramRepository.findTopById(param.getId());
        BeanUtil.copyProperties(param, oldParam, CopyOptions.create().setIgnoreNullValue(true));
        oldParam.setUpdateUser(user);
        paramRepository.save(oldParam);
        return Ret.ok("message", "保存成功");
    }

    /**
     * 删除参数
     *
     * @param id 参数对象id
     * @return 删除结果
     */
    @Override
    public Ret deleteParam(Long id) {
        User user = userService.getCurrentLoginUser();
        Param param = paramRepository.findTopById(id);
        if (param == null) {
            return Ret.failMsg("找不到要删除的参数");
        }
        param.setIsDelete(1);
        param.setUpdateUser(user);
        paramRepository.save(param);
        return Ret.ok("message", "删除参数成功");
    }

    /**
     * 根据key获取参数
     * @param key
     * @return
     */
    @Override
    public Param findParamByKey(String key) {
        return paramRepository.findTopByKeyAndIsDelete(key, 0);
    }
}
