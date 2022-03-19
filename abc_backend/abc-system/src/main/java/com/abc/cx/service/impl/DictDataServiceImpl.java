package com.abc.cx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.abc.cx.domain.DictData;
import com.abc.cx.domain.User;
import com.abc.cx.domain.repository.DictDataRepository;
import com.abc.cx.service.IDictDataService;
import com.abc.cx.vo.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:48 2021/11/6
 **/
@Service
public class DictDataServiceImpl implements IDictDataService {

    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManagerPrimary) {
        this.entityManager = entityManagerPrimary;
    }

    private UserServiceImpl userService;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    private DictDataRepository dictDataRepository;

    @Autowired
    public void setDictDataRepository(DictDataRepository dictDataRepository) {
        this.dictDataRepository = dictDataRepository;
    }

    /**
     * 获取字典数据列表
     *
     * @param type  类型
     * @param label 标签
     * @return 字典数据对象列表结果
     */
    @Override
    public Ret listDictData(String type, String label) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "orderNum");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        Sort sort = Sort.by(orders);
        Specification<DictData> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("isDelete"), 0));
            if (StrUtil.isNotEmpty(type)) {
                predicateList.add(builder.isNotNull(root.get("type")));
                predicateList.add(builder.equal(root.get("type"), type));
            }
            if (StrUtil.isNotEmpty(label)) {
                predicateList.add(builder.isNotNull(root.get("label")));
                predicateList.add(builder.like(root.get("label"), "%" + label + "%"));
            }
            Predicate[] arrayType = new Predicate[predicateList.size()];
            return builder.and(predicateList.toArray(arrayType));
        };
        List<DictData> list = dictDataRepository.findAll(specification, sort);
        return Ret.ok("list", list);
    }

    /**
     * 根据条件获取字典数据列表
     *
     * @param name     字典名称
     * @param page     页面
     * @param pageSize 页数
     * @return 字典数据对象列表结果
     */
    @Override
    public Ret pageDictDatas(String name, Integer page, Integer pageSize) {
        StringBuilder sql = new StringBuilder();
        sql.append("from dict_data d,dict_type t" +
                " where d.type = t.type and d.is_delete = 0");
        Map<String, Object> paramMap = new HashMap<>(16);
        if (!StringUtils.isEmpty(name)) {
            sql.append(" and t.name like :name ");
            paramMap.put("name", "%" + name + "%");
        }
        String selectSql = "select d.label, d.value, d.type, " +
                " d.is_default, d.status, d.order_num, d.create_time, t.name ";
        String countSql = "select count(*) " + sql.toString();
        sql.append(" order by d.order_num asc, d.create_time asc limit :offset, :pageSize");
        paramMap.put("offset", (page - 1) * pageSize);
        paramMap.put("pageSize", pageSize);
        Query query = entityManager.createNativeQuery(selectSql + sql.toString());
        for (String key : paramMap.keySet()) {
            query.setParameter(key, paramMap.get(key));
        }
        List list = query.getResultList();
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (Object object : list) {
            Map<String, Object> map = new HashMap<>(16);
            Object[] obj = (Object[]) object;
            map.put("dictLabel", obj[0]);
            map.put("dictValue", obj[1]);
            map.put("dictType", obj[2]);
            map.put("isDefault", obj[3]);
            map.put("status", obj[4]);
            map.put("orderNum", obj[5]);
            map.put("createTime", obj[6]);
            map.put("dictName", obj[7]);
            mapList.add(map);
        }
        Query countQuery = entityManager.createNativeQuery(countSql);
        paramMap.remove("offset");
        paramMap.remove("pageSize");
        for (String key : paramMap.keySet()) {
            countQuery.setParameter(key, paramMap.get(key));
        }
        Long count = ((BigInteger) countQuery.getSingleResult()).longValue();
        return Ret.ok("list", mapList).set("count", count);
    }

    /**
     * 获取字典数据详细信息
     *
     * @param id 字典数据id
     * @return 字典数据对象详情结果
     */
    @Override
    public Ret getDictData(Long id) {
        DictData dictData = dictDataRepository.findTopById(id);
        return Ret.ok("dictData", dictData);
    }

    /**
     * 增加字典数据
     *
     * @param dictData 字典数据对象
     * @return 增加结果
     */
    @Override
    public Ret insertDictData(DictData dictData) {
        User user = userService.getCurrentLoginUser();
        dictData.setCreateUser(user);
        dictDataRepository.save(dictData);
        return Ret.ok("message", "增加字典数据成功");
    }

    /**
     * 修改字典数据
     *
     * @param dictData 字典数据对象
     * @return 修改结果
     */
    @Override
    public Ret updateDictData(DictData dictData) {
        User user = userService.getCurrentLoginUser();
        DictData oldDictData = dictDataRepository.findTopById(dictData.getId());
        BeanUtil.copyProperties(dictData, oldDictData, CopyOptions.create().setIgnoreNullValue(true));
        oldDictData.setUpdateUser(user);
        dictDataRepository.save(oldDictData);
        return Ret.ok("message", "修改字典数据成功");
    }

    /**
     * 删除字典数据
     *
     * @param id 字典数据id
     * @return 删除结果
     */
    @Override
    public Ret deleteDictData(Long id) {
        User user = userService.getCurrentLoginUser();
        DictData dictData = dictDataRepository.findTopById(id);
        if (dictData == null) {
            return Ret.failMsg("找不到要删除的字典数据");
        }
        dictData.setIsDelete(1);
        dictData.setUpdateUser(user);
        dictDataRepository.save(dictData);
        return Ret.ok("message", "删除字典数据成功");
    }

}
