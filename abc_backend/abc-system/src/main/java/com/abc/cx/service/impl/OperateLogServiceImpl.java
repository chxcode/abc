package com.abc.cx.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.abc.cx.domain.OperateLog;
import com.abc.cx.domain.User;
import com.abc.cx.domain.repository.OperateLogRepository;
import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.service.IOperateLogService;
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
public class OperateLogServiceImpl implements IOperateLogService {

    private UserServiceImpl userService;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    private OperateLogRepository operateLogRepository;

    @Autowired
    public void setOperateLogRepository(OperateLogRepository operateLogRepository) {
        this.operateLogRepository = operateLogRepository;
    }

    /**
     * 获取操作日志分页列表
     *
     * @param title     标题
     * @param type      类型
     * @param status    操作状态
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param page      页码
     * @param pageSize  页数
     * @return 操作日志对象列表
     */
    @Override
    public Ret pageOperateLogs(String title, OperateTypeEnum type, OperateStatusEnum status, String beginDate, String endDate, Integer page, Integer pageSize) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order);
        Sort sort = Sort.by(orders);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        Specification<OperateLog> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("isDelete"), 0));
            if (StrUtil.isNotEmpty(title)) {
                predicateList.add(builder.isNotNull(root.get("title")));
                predicateList.add(builder.like(root.get("title"), "%" + title + "%"));
            }
            if (status != null) {
                predicateList.add(builder.isNotNull(root.get("status")));
                predicateList.add(builder.equal(root.get("status"), status));
            }
            if (StrUtil.isNotEmpty(beginDate) && StrUtil.isNotEmpty(endDate)) {
                String keyBegin = beginDate + " 00:00:00";
                String keyEnd = endDate + " 23:59:59";
                predicateList.add(builder.between(root.get("createTime"), DateUtil.parse(keyBegin), DateUtil.parse(keyEnd)));
            }
            Predicate[] arrayType = new Predicate[predicateList.size()];
            return builder.and(predicateList.toArray(arrayType));
        };
        Page<OperateLog> list = operateLogRepository.findAll(specification, pageRequest);
        return Ret.ok("list", list);
    }

    /**
     * 获取操作日志详情
     *
     * @return ret
     */
    @Override
    public Ret getOperateLog(Long id) {
        OperateLog operateLog = operateLogRepository.findTopById(id);
        return Ret.ok("operateLog", operateLog);
    }

    /**
     * 新增操作日志
     * @param operateLog 操作日志对象
     */
    @Override
    public void insertOperateLog(OperateLog operateLog) {
        User user = userService.getCurrentLoginUser();
        operateLog.setCreateUser(user);
        operateLogRepository.save(operateLog);
    }

    /**
     * 删除操作日志
     * @param id 操作日志对象id
     * @return 删除结果
     */
    @Override
    public Ret deleteOperateLog(Long id) {
        User user = userService.getCurrentLoginUser();
        OperateLog operateLog = operateLogRepository.findTopById(id);
        if (operateLog == null) {
            return Ret.failMsg("找不到要删除的操作日志");
        }
        operateLog.setIsDelete(1);
        operateLog.setUpdateUser(user);
        operateLogRepository.save(operateLog);
        return Ret.ok("message", "删除操作日志成功");
    }

}
