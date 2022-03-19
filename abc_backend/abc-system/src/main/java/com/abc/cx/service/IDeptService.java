package com.abc.cx.service;

import com.abc.cx.domain.Dept;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.vo.Ret;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:45 2021/11/6
 **/
public interface IDeptService {

    /**
     * 获取用户所属角色的部门列表
     *
     * @return ret
     */
    Ret getRoleDeptList();

    /**
     * 获取子菜单
     *
     * @param dept     部门对象
     * @param deptList
     * @return
     */
    List<Dept> getChildList(Dept dept, List<Dept> deptList);

    /**
     * 获取角色部门树值
     *
     * @param roleId 角色id
     * @return
     */
    Ret getRoleDeptTreeKey(Long roleId);

    /**
     * 获取部门列表
     *
     * @param name      部门名称
     * @param status    状态
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 部门列表结果
     */
    Ret listDepts(String name, EnableStatusEnum status, String beginDate, String endDate);


    /**
     * 获取部门详细信息
     *
     * @param id 部门id
     * @return 部门详情结果
     */
    Ret getDept(Long id);

    /**
     * 新增部门
     *
     * @param dept 部门对象
     * @return 增加结果
     */
    Ret insertDept(Dept dept);

    /**
     * 修改部门
     *
     * @param dept 部门对象
     * @return 修改结果
     */
    Ret updateDept(Dept dept);

    /**
     * 删除部门
     *
     * @param id 部门id
     * @return 删除结果
     */
    Ret deleteDept(Long id);

}
