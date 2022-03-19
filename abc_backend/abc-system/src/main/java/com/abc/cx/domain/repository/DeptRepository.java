package com.abc.cx.domain.repository;

import com.abc.cx.domain.Dept;
import com.abc.cx.enums.EnableStatusEnum;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 部门
 * @Date: 22:29 2021/11/6
 **/
public interface DeptRepository extends JpaRepository<Dept, Long> {

    /**
     * 获取相应id的部门
     *
     * @param id 部门id
     * @return 部门对象
     */
    Dept findTopById(Long id);

    /**
     * 获取相应条件的部门
     *
     * @param name       部门名称
     * @param parentDept 父部门
     * @param isDelete   是否删除
     * @param status     部门状态
     * @return 部门对象
     */
    Dept findTopByNameAndParentDeptAndIsDeleteAndStatus(String name, Dept parentDept, Integer isDelete, EnableStatusEnum status);

    /**
     * 获取相应条件的部门列表
     *
     * @param specification 条件
     * @param sort          排序
     * @return 部门对象列表
     */
    List<Dept> findAll(Specification specification, Sort sort);

    /**
     * 获取相应条件的部门列表
     *
     * @param isDelete   是否删除
     * @param status     部门状态
     * @param parentDept 父部门
     * @return 部门对象列表
     */
    List<Dept> findAllByIsDeleteAndStatusAndParentDeptOrderByOrderNum(Integer isDelete, EnableStatusEnum status, Dept parentDept);

    /**
     * 获取相应条件的部门列表
     *
     * @param isDelete   是否删除
     * @param parentDept 父部门
     * @return 部门对象列表
     */
    List<Dept> findAllByIsDeleteAndParentDeptOrderByOrderNum(Integer isDelete, Dept parentDept);

    /**
     * 获取相应条件的部门列表
     *
     * @param isDelete 是否删除
     * @param status   部门状态
     * @return 部门对象列表
     */
    List<Dept> findAllByIsDeleteAndStatusOrderByOrderNum(Integer isDelete, EnableStatusEnum status);

}
