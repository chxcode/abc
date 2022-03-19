package com.abc.cx.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.abc.cx.domain.LoginInfo;
import com.abc.cx.domain.repository.LoginInfoRepository;
import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.service.ILoginInfoService;
import com.abc.cx.utils.CommonUtil;
import com.abc.cx.vo.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:48 2021/11/6
 **/
@Service
public class LoginInfoServiceImpl implements ILoginInfoService {

    private LoginInfoRepository loginInfoRepository;

    @Autowired
    public void setLoginInfoRepository(LoginInfoRepository loginInfoRepository) {
        this.loginInfoRepository = loginInfoRepository;
    }

    private HttpServletRequest request;

    @Autowired
    public void setHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 获取登录信息分页列表
     *
     * @param status    状态
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param page      页码
     * @param pageSize  页数
     * @return 登录信息对象列表结果
     */
    @Override
    public Ret pageLoginInfos(OperateStatusEnum status, String beginDate, String endDate, Integer page, Integer pageSize) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "time");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order);
        Sort sort = Sort.by(orders);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        Specification<LoginInfo> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StrUtil.isNotEmpty(beginDate) && StrUtil.isNotEmpty(endDate)) {
                String keyBegin = beginDate + " 00:00:00";
                String keyEnd = endDate + " 23:59:59";
                predicateList.add(builder.between(root.get("time"), DateUtil.parse(keyBegin), DateUtil.parse(keyEnd)));
            }
            if (status != null) {
                predicateList.add(builder.isNotNull(root.get("status")));
                predicateList.add(builder.equal(root.get("status"), status));
            }
            Predicate[] arrayType = new Predicate[predicateList.size()];
            return builder.and(predicateList.toArray(arrayType));
        };
        Page<LoginInfo> list = loginInfoRepository.findAll(specification, pageRequest);
        return Ret.ok("list", list);
    }

    /**
     * 获取登录信息详细信息
     *
     * @param id 登录信息对象id
     * @return 登录信息对象详情结果
     */
    @Override
    public Ret getLoginInfo(Long id) {
        LoginInfo loginInfo = loginInfoRepository.findTopById(id);
        return Ret.ok("loginInfo", loginInfo);
    }

    /**
     * 增加登录信息
     *
     * @param username 登录名
     * @param status   状态
     * @param message  登录信息
     * @return 增加结果
     */
    @Override
    public Ret insertLoginInfo(String username, OperateStatusEnum status, String message) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setTime(new Date());
        loginInfo.setStatus(status);
        loginInfo.setMsg(message);
        String ip = ServletUtil.getClientIP(request);
        loginInfo.setIp(ip);
        loginInfo.setLocation(CommonUtil.getCityInfo(ip));
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOs().toString();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().toString();
        loginInfo.setOs(os);
        loginInfo.setBrowser(browser);
        loginInfoRepository.save(loginInfo);
        return Ret.ok();
    }

}
