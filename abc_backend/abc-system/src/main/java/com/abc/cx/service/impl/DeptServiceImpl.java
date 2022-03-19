package com.abc.cx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.abc.cx.constant.SysConstants;
import com.abc.cx.domain.Dept;
import com.abc.cx.domain.RoleDept;
import com.abc.cx.domain.User;
import com.abc.cx.domain.repository.DeptRepository;
import com.abc.cx.domain.repository.RoleDeptRepository;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.enums.ResultEnum;
import com.abc.cx.service.IDeptService;
import com.abc.cx.vo.DeptVO;
import com.abc.cx.vo.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:39 2021/11/6
 **/
@Service
public class DeptServiceImpl implements IDeptService {

    private UserServiceImpl userService;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    private DeptRepository deptRepository;

    @Autowired
    public void setDeptRepository(DeptRepository deptRepository) {
        this.deptRepository = deptRepository;
    }

    private RoleDeptRepository roleDeptRepository;

    @Autowired
    public void setRoleDeptRepository(RoleDeptRepository roleDeptRepository) {
        this.roleDeptRepository = roleDeptRepository;
    }

    /**
     * 获取用户所属角色的部门列表
     *
     * @return ret
     */
    @Override
    public Ret getRoleDeptList() {
        User user = userService.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        List<RoleDept> roleDeptList = roleDeptRepository.findAllByRoleId(user.getRole().getId());
        List<Dept> deptList = new ArrayList<>();
        for (RoleDept roleDept : roleDeptList) {
            Dept dept = deptRepository.findTopById(roleDept.getDeptId());
            deptList = getChildList(dept, deptList);
        }
        return Ret.ok("list", deptList);
    }

    @Override
    public List<Dept> getChildList(Dept dept, List<Dept> deptList) {
        deptList.add(dept);
        List<Dept> childList = deptRepository.findAllByIsDeleteAndParentDeptOrderByOrderNum(0, dept);
        if (childList.size() > 0) {
            for (Dept child : childList) {
                getChildList(child, deptList);
            }
        }
        return deptList;
    }

    /**
     * 获取角色部门树值
     *
     * @return ret
     */
    @Override
    public Ret getRoleDeptTreeKey(Long roleId) {
        User user = userService.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        List<RoleDept> roleDeptList = roleDeptRepository.findAllByRoleId(roleId);
        List<String> deptKeyList = new ArrayList<>();
        for (RoleDept roleDept : roleDeptList) {
            Dept dept = deptRepository.findTopById(roleDept.getDeptId());
            deptKeyList.add(dept.getId().toString());
        }
        return Ret.ok("deptKeyList", deptKeyList);
    }

    /**
     * 获取部门列表
     *
     * @param name      部门名称
     * @param status    状态
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 部门对象列表结果
     */
    @Override
    public Ret listDepts(String name, EnableStatusEnum status, String beginDate, String endDate) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "orderNum");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        Sort sort = Sort.by(orders);
        Specification<Dept> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("isDelete"), 0));
            if (StrUtil.isNotEmpty(name)) {
                predicateList.add(builder.isNotNull(root.get("name")));
                predicateList.add(builder.like(root.get("name"), "%" + name + "%"));
            }
            if (StrUtil.isNotEmpty(beginDate) && StrUtil.isNotEmpty(endDate)) {
                String keyBegin = beginDate + " 00:00:00";
                String keyEnd = endDate + " 23:59:59";
                predicateList.add(builder.between(root.get("createTime"), DateUtil.parse(keyBegin), DateUtil.parse(keyEnd)));
            }
            if (status != null) {
                predicateList.add(builder.isNotNull(root.get("status")));
                predicateList.add(builder.equal(root.get("status"), status));
            }
            Predicate[] arrayType = new Predicate[predicateList.size()];
            return builder.and(predicateList.toArray(arrayType));
        };
        List<Dept> list = deptRepository.findAll(specification, sort);
        return Ret.ok("list", list);
    }

    /**
     * 获取部门详细信息
     *
     * @param id 部门id
     * @return 部门对象详情结果
     */
    @Override
    public Ret getDept(Long id) {
        Dept dept = deptRepository.findTopById(id);
        DeptVO deptVO = DeptVO.builder()
                .id(dept.getId())
                .name(dept.getName())
                .leader(dept.getLeader())
                .email(dept.getEmail())
                .phone(dept.getPhone())
                .parentDept(dept.getParentDept())
                .orderNum(dept.getOrderNum())
                .status(dept.getStatus().getMessage())
                .build();
        return Ret.ok("dept", deptVO);
    }

    /**
     * 增加部门
     *
     * @param dept 部门对象
     * @return 增加结果
     */
    @Override
    public Ret insertDept(Dept dept) {
        User user = userService.getCurrentLoginUser();
        if (isNameExist(dept)) {
            return Ret.failMsg(ResultEnum.DEPT_EXIT.getMessage());
        }
        if (dept.getParentDept() != null && dept.getStatus() == EnableStatusEnum.ENABLED) {
            if (isParentDisabled(dept)) {
                return Ret.failMsg(ResultEnum.DEPT_PARENT_DISABLED.getMessage());
            }
        }
        dept.setCreateUser(user);
        deptRepository.save(dept);
        return Ret.ok("message", "增加部门成功");
    }

    /**
     * 修改部门
     *
     * @param dept 部门对象
     * @return 修改结果
     */
    @Override
    public Ret updateDept(Dept dept) {
        User user = userService.getCurrentLoginUser();
        if (isNameExist(dept)) {
            return Ret.failMsg(ResultEnum.DEPT_EXIT.getMessage());
        }
        if (dept.getParentDept() != null) {
            if (isChild(dept, dept.getParentDept())) {
                return Ret.failMsg(ResultEnum.PARENT_IS_CHILD.getMessage());
            }
        }
        if (dept.getStatus() != null && dept.getStatus() == EnableStatusEnum.DISABLED) {
            if (isUse(dept)) {
                return Ret.failMsg(ResultEnum.DEPT_ROLE_USE.getMessage());
            }
            if (isChildNotDisabled(dept)) {
                return Ret.failMsg(ResultEnum.DEPT_CHILD_NOT_DISABLED.getMessage());
            }
            if (dept.getId().equals(SysConstants.DEFAULT_ID)) {
                return Ret.failMsg(ResultEnum.DEPT_IS_DEFAULT.getMessage());
            }
        }
        if (dept.getParentDept() != null && dept.getStatus() == EnableStatusEnum.ENABLED) {
            if (isParentDisabled(dept)) {
                return Ret.failMsg(ResultEnum.DEPT_PARENT_DISABLED.getMessage());
            }
        }
        Dept oldDept = deptRepository.findTopById(dept.getId());
        BeanUtil.copyProperties(dept, oldDept, CopyOptions.create().setIgnoreNullValue(true));
        if (dept.getParentDept() == null) {
            oldDept.setParentDept(null);
        }
        oldDept.setUpdateUser(user);
        deptRepository.save(oldDept);
        return Ret.ok("message", "修改部门成功");
    }

    /**
     * 删除部门
     *
     * @param id 部门对象id
     * @return 删除结果
     */
    @Override
    public Ret deleteDept(Long id) {
        User user = userService.getCurrentLoginUser();
        Dept dept = deptRepository.findTopById(id);
        if (id.equals(SysConstants.DEFAULT_ID)) {
            return Ret.failMsg(ResultEnum.DEPT_IS_DEFAULT.getMessage());
        }
        if (isChildNotDelete(dept)) {
            return Ret.failMsg(ResultEnum.CHILD_NOT_DELETE.getMessage());
        }
        if (isUse(dept)) {
            return Ret.failMsg(ResultEnum.DEPT_ROLE_USE.getMessage());
        }
        dept.setIsDelete(1);
        dept.setUpdateUser(user);
        deptRepository.save(dept);
        return Ret.ok("message", "删除部门成功");
    }

    /**
     * 同一父级下不能有同名部门
     *
     * @param dept 部门对象
     * @return 结果 true 是 false 否
     */
    private boolean isNameExist(Dept dept) {
        Dept existDept = deptRepository.findTopByNameAndParentDeptAndIsDeleteAndStatus(dept.getName(), dept.getParentDept(), 0, EnableStatusEnum.ENABLED);
        if (existDept == null) {
            return false;
        } else {
            boolean isSame = (dept.getId() != null && dept.getId().equals(existDept.getId()));
            return !isSame;
        }
    }

    /**
     * 修改部门时不能挂载到子部门下
     *
     * @param dept       部门对象
     * @param parentDept 父部门对象
     * @return 结果 true 是 false 否
     */
    private boolean isChild(Dept dept, Dept parentDept) {
        Dept curParentDept = deptRepository.findTopById(parentDept.getId());
        if (dept.getId().equals(parentDept.getId())) {
            return true;
        }
        if (curParentDept.getParentDept() == null) {
            return false;
        }
        return isChild(dept, curParentDept.getParentDept());
    }

    /**
     * 判断部门是否已在使用
     *
     * @param dept 部门对象
     * @return 结果 true 是 false 否
     */
    private boolean isUse(Dept dept) {
        List<RoleDept> roleDeptList = roleDeptRepository.findAllByDeptId(dept.getId());
        return !roleDeptList.isEmpty();
    }

    /**
     * 是否存在未删除的子部门
     *
     * @param dept 部门对象
     * @return 结果 true 是 false 否
     */
    private boolean isChildNotDelete(Dept dept) {
        List<Dept> list = deptRepository.findAllByIsDeleteAndParentDeptOrderByOrderNum(0, dept);
        return !list.isEmpty();
    }

    /**
     * 是否存在未删除且未禁用的子部门
     *
     * @param dept 部门对象
     * @return 结果 true 是 false 否
     */
    private boolean isChildNotDisabled(Dept dept) {
        List<Dept> list = deptRepository.findAllByIsDeleteAndStatusAndParentDeptOrderByOrderNum(0, EnableStatusEnum.ENABLED, dept);
        return !list.isEmpty();
    }

    /**
     * 父部门是否禁用
     *
     * @param dept 部门对象
     * @return 结果 true 是 false 否
     */
    private boolean isParentDisabled(Dept dept) {
        Dept parentDept = deptRepository.findTopById(dept.getParentDept().getId());
        return parentDept.getStatus() == EnableStatusEnum.DISABLED;
    }

}
