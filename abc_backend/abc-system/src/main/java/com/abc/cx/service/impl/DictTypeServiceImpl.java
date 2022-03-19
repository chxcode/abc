package com.abc.cx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.abc.cx.domain.DictType;
import com.abc.cx.domain.User;
import com.abc.cx.domain.repository.DictTypeRepository;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.service.IDictTypeService;
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
public class DictTypeServiceImpl implements IDictTypeService {

    private UserServiceImpl userService;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    private DictTypeRepository dictTypeRepository;

    @Autowired
    public void setDictTypeRepository(DictTypeRepository dictTypeRepository) {
        this.dictTypeRepository = dictTypeRepository;
    }

    /**
     * 获取字典类型列表
     *
     * @return 字典类型对象列表结果
     */
    @Override
    public Ret listDictTypes() {
        List<DictType> list = dictTypeRepository.findAllByIsDeleteAndStatusOrderByCreateTimeAsc(0, EnableStatusEnum.ENABLED);
        return Ret.ok("list", list);
    }

    /**
     * 获取字典类型分页列表
     *
     * @param name     名称
     * @param type     类型
     * @param page     页码
     * @param pageSize 页数
     * @return 字典类型对象列表结果
     */
    @Override
    public Ret pageDictTypes(String name, String type, Integer page, Integer pageSize) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order);
        Sort sort = Sort.by(orders);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        Specification<DictType> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("isDelete"), 0));
            if (StrUtil.isNotEmpty(name)) {
                predicateList.add(builder.isNotNull(root.get("name")));
                predicateList.add(builder.like(root.get("name"), "%" + name + "%"));
            }
            if (StrUtil.isNotEmpty(type)) {
                predicateList.add(builder.isNotNull(root.get("type")));
                predicateList.add(builder.like(root.get("type"), "%" + type + "%"));
            }
            Predicate[] arrayType = new Predicate[predicateList.size()];
            return builder.and(predicateList.toArray(arrayType));
        };
        Page<DictType> list = dictTypeRepository.findAll(specification, pageRequest);
        return Ret.ok("list", list);
    }

    /**
     * 获取字典类型详细信息
     *
     * @param id 字典类型id
     * @return 字典类型对象详情结果
     */
    @Override
    public Ret getDictType(Long id) {
        DictType dictType = dictTypeRepository.findTopById(id);
        return Ret.ok("dictType", dictType);
    }

    /**
     * 新增字典类型
     *
     * @param dictType 字典类型对象
     * @return 新增结果
     */
    @Override
    public Ret insertDictType(DictType dictType) {
        User user = userService.getCurrentLoginUser();
        dictType.setCreateUser(user);
        dictTypeRepository.save(dictType);
        return Ret.ok("message", "增加字典类型成功");
    }

    /**
     * 修改字典类型
     *
     * @param dictType 字典类型对象
     * @return 修改结果
     */
    @Override
    public Ret updateDictType(DictType dictType) {
        User user = userService.getCurrentLoginUser();
        DictType oldDictType = dictTypeRepository.findTopById(dictType.getId());
        BeanUtil.copyProperties(dictType, oldDictType, CopyOptions.create().setIgnoreNullValue(true));
        oldDictType.setUpdateUser(user);
        dictTypeRepository.save(oldDictType);
        return Ret.ok("message", "修改字典类型成功");
    }

    /**
     * 删除字典类型
     *
     * @param id 字典类型id
     * @return 删除结果
     */
    @Override
    public Ret deleteDictType(Long id) {
        User user = userService.getCurrentLoginUser();
        DictType dictType = dictTypeRepository.findTopById(id);
        if (dictType == null) {
            return Ret.failMsg("找不到要删除的字典类型");
        }
        dictType.setIsDelete(1);
        dictType.setUpdateUser(user);
        dictTypeRepository.save(dictType);
        return Ret.ok("message", "删除字典类型成功");
    }

}
