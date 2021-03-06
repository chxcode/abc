package com.abc.cx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.abc.cx.config.Configure;
import com.abc.cx.constant.SysConstants;
import com.abc.cx.domain.Menu;
import com.abc.cx.domain.RoleMenu;
import com.abc.cx.domain.User;
import com.abc.cx.domain.repository.*;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.enums.GenderTypeEnum;
import com.abc.cx.enums.ResultEnum;
import com.abc.cx.events.UserRegisterEvent;
import com.abc.cx.module.email.MailService;
import com.abc.cx.service.IRoleService;
import com.abc.cx.service.IUserService;
import com.abc.cx.utils.RedisUtil;
import com.abc.cx.vo.Ret;
import com.abc.cx.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:48 2021/11/6
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService, IUserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private RoleMenuRepository roleMenuRepository;

    @Autowired
    public void setRoleMenuRepository(RoleMenuRepository roleMenuRepository) {
        this.roleMenuRepository = roleMenuRepository;
    }


    private MenuRepository menuRepository;

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    private IRoleService roleService;

    @Autowired
    public void setRoleService(IRoleService iRoleService) {
        this.roleService = iRoleService;
    }

    private TemplateEngine templateEngine;

    private MailService mailService;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    private static final String RETRIEVE_PWD_KEY = "retrieve:pwd:key:";


    private Configure configure;

    @Autowired
    public void setConfigure(Configure configure) {
        this.configure = configure;
    }

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    @Override
    public User getCurrentLoginUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof User) {
            return userRepository.findTopById(((User) user).getId());
        } else {
            return null;
        }
    }

    /**
     * ????????????????????????
     *
     * @param oldPassword ?????????
     * @param newPassword ?????????
     * @return ????????????
     */
    @Override
    public Ret updatePassword(String oldPassword, String newPassword) {
        User user = this.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        if (passwordEncoder.encode(oldPassword).equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return Ret.ok("message", "??????????????????");
        } else {
            return Ret.failMsg("??????????????????");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsernameAndStatusAndIsDelete(s, EnableStatusEnum.ENABLED, 0);
    }

    /**
     * ????????????????????????
     *
     * @param username  ?????????
     * @param realName  ??????
     * @param telephone ??????
     * @param gender    ??????
     * @param status    ??????
     * @param beginDate ????????????
     * @param endDate   ????????????
     * @param deptId    ??????id
     * @param page      ??????
     * @param pageSize  ??????
     * @return ????????????????????????
     */
    @Override
    public Ret pageUsers(String username, String realName, String telephone, GenderTypeEnum gender, EnableStatusEnum status, String beginDate, String endDate, Long deptId, Integer page, Integer pageSize) {
        return pageUsersPro(username, realName, telephone, gender, status, beginDate, endDate, deptId, page, pageSize, null, null);
    }


    @Override
    public Ret pageUsersPro(String username, String realName, String telephone, GenderTypeEnum gender, EnableStatusEnum status, String beginDate, String endDate, Long deptId, Integer page, Integer pageSize, List<Long> ids, String name) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order);
        Sort sort = Sort.by(orders);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        Specification<User> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("isDelete"), 0));
            predicateList.add(builder.notEqual(root.get("username"), SysConstants.DEFAULT_ADMIN));
            if (StrUtil.isNotEmpty(realName)) {
                predicateList.add(builder.isNotNull(root.get("realName")));
                predicateList.add(builder.like(root.get("realName"), "%" + realName + "%"));
            }
            if (StrUtil.isNotEmpty(username)) {
                predicateList.add(builder.isNotNull(root.get("username")));
                predicateList.add(builder.like(root.get("username"), "%" + username + "%"));
            }
            if (StrUtil.isNotEmpty(name)) {
                predicateList.add(builder.or(builder.like(root.get("realName"), "%" + name + "%"),
                        builder.like(root.get("username"), "%" + name + "%"),
                        builder.like(root.get("nickname"), "%" + name + "%")));
            }
            if (ObjectUtil.isNotEmpty(ids)) {
                CriteriaBuilder.In<Long> in = builder.in(root.get("id"));
                for (Long id : ids) {
                    in.value(id);
                }
                predicateList.add(in);
            }
            if (StrUtil.isNotEmpty(telephone)) {
                predicateList.add(builder.isNotNull(root.get("telephone")));
                predicateList.add(builder.like(root.get("telephone"), "%" + telephone + "%"));
            }
            if (StrUtil.isNotEmpty(beginDate) && StrUtil.isNotEmpty(endDate)) {
                String keyBegin = beginDate + " 00:00:00";
                String keyEnd = endDate + " 23:59:59";
                predicateList.add(builder.between(root.get("createTime"), DateUtil.parse(keyBegin), DateUtil.parse(keyEnd)));
            }
            if (gender != null) {
                predicateList.add(builder.isNotNull(root.get("gender")));
                predicateList.add(builder.equal(root.get("gender"), gender));
            }
            Predicate[] arrayType = new Predicate[predicateList.size()];
            return builder.and(predicateList.toArray(arrayType));
        };
        Page<User> list = userRepository.findAll(specification, pageRequest);
        return Ret.ok("list", list);
    }

    /**
     * ??????????????????
     *
     * @param id ??????id
     * @return ????????????????????????
     */
    @Override
    public Ret getUser(Long id) {
        User user = userRepository.findTopById(id);
        UserVO userVO = UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .gender(user.getGender())
                .password(user.getPassword())
                .realName(user.getRealName())
                .telephone(user.getTelephone())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
        return Ret.ok("user", userVO);
    }

    /**
     * ????????????
     *
     * @param userVO ????????????
     * @return ????????????
     */
    @Override
    public Ret insertUser(UserVO userVO) {
        if (isExist(userVO)) {
            return Ret.failMsg(ResultEnum.USER_EXIST.getMessage());
        }
        User user = User.builder()
                .username(userVO.getUsername())
                .password(passwordEncoder.encode(userVO.getPassword()))
                .role(userVO.getRole())
                .email(userVO.getEmail())
                .gender(userVO.getGender())
                .realName(userVO.getRealName())
                .status(EnableStatusEnum.ENABLED)
                .telephone(userVO.getTelephone())
                .wechat(userVO.getWechat())
                .isDelete(0)
                .build();
        userRepository.save(user);
        return Ret.ok("message", "??????????????????");
    }

    /**
     * ????????????
     *
     * @param userVO ????????????
     * @return ????????????
     */
    @Override
    public Ret updateUser(UserVO userVO) {
        if (isExist(userVO)) {
            return Ret.failMsg(ResultEnum.USER_EXIST.getMessage());
        }
        if (userVO.getRole() != null) {
            List<RoleMenu> roleMenuList = roleMenuRepository.findAllByRoleId(userVO.getRole().getId());
            if (roleMenuList.size() <= 0) {
                return Ret.failMsg("????????????????????????????????????");
            }
        }
        User oldUser = userRepository.findTopById(userVO.getId());
        if (!oldUser.getPassword().equals(userVO.getPassword())) {
            userVO.setPassword(passwordEncoder.encode(userVO.getPassword()));
        }
        BeanUtil.copyProperties(userVO, oldUser, CopyOptions.create().setIgnoreNullValue(true));
        userRepository.save(oldUser);
        return Ret.ok("message", "??????????????????");
    }

    /**
     * ????????????
     *n
     * @param id ??????id
     * @return ????????????
     */
    @Override
    public Ret deleteUser(Long id) {
        User user = userRepository.findTopById(id);
        Assert.notNull(user, ResultEnum.USER_NOT_FOUND.getMessage());
        if (id.equals(SysConstants.DEFAULT_ID)) {
            return Ret.failMsg(ResultEnum.USER_IS_ADMIN.getMessage());
        }
        User currUser = getCurrentLoginUser();
        if (id.equals(currUser.getId())) {
            return Ret.failMsg(ResultEnum.USER_DELETE_SELF.getMessage());
        }
        user.setIsDelete(1);
        userRepository.save(user);
        return Ret.ok("message", "??????????????????");
    }

    /**
     * ??????????????????
     *
     * @param ids ??????id??????
     * @return ??????????????????
     */
    @Override
    public Ret deleteUsers(List<Long> ids) {
        List<User> list = userRepository.findAllByIdIn(ids);
        for (User user : list) {
            user.setIsDelete(0);
        }
        userRepository.saveAll(list);
        return Ret.ok("message", "????????????????????????");
    }

    @Override
    @Cacheable(cacheNames = "user.service.menus")
    public Ret getMenuList(Long id) {
        User user = userRepository.findTopById(id);
        List<RoleMenu> roleMenuList = roleMenuRepository.findAllByRoleId(user.getRole().getId());
        List<Menu> menuList = new ArrayList<>();
        for (RoleMenu roleMenu : roleMenuList) {
            Menu menu = menuRepository.findTopById(roleMenu.getMenuId());
            if (!"BUTTON".equals(menu.getType())) {
                menuList.add(menu);
            }
        }
        return Ret.ok("menus", menuList);
    }

    /**
     * ?????????????????????
     *
     * @param userVO ??????VO ??????
     * @return ?????? true ??? false ???
     */
    private boolean isExist(UserVO userVO) {
        User existUser = userRepository.findByUsernameAndIsDelete(userVO.getUsername(), 0);
        if (existUser == null) {
            return false;
        } else {
            return userVO.getId() == null || !userVO.getId().equals(existUser.getId());
        }
    }

    @Override
    public Ret register(UserVO userVO, HttpServletRequest httpServletRequest) {
        if (userNameIsExist(userVO.getUsername())) {
            return Ret.failMsg(ResultEnum.USER_EXIST.getMessage());
        }
        if (emailIsExist(userVO.getEmail())) {
            return Ret.failMsg(ResultEnum.USER_EMAIL_EXIST.getMessage());
        }
        User user;
        User res;
        String password = RandomUtil.randomString(8);
        if (emailIsExist(userVO.getEmail())) {
            user = userRepository.findByEmailAndIsDelete(userVO.getEmail(), 0);
            user.setUsername(userVO.getUsername());
            user.setPassword(DigestUtil.md5Hex(password));
        } else {
            user = User.builder()
                    .username(userVO.getUsername())
                    .password(DigestUtil.md5Hex(password))
                    .email(userVO.getEmail())
                    .gender(GenderTypeEnum.MAN)
                    .status(EnableStatusEnum.DISABLED)
                    .role(roleService.getRoleByKey(SysConstants.REGISTER_USER_DEFAULT_ROLE_KEY))
                    .isDelete(0)
                    .build();
        }
        res = userRepository.save(user);
        if (res != null) {
            applicationContext.publishEvent(UserRegisterEvent.create(applicationContext, user, password, httpServletRequest));
            return Ret.ok("msg", "????????????????????????????????????????????????????????????");
        } else {
            log.error("?????????{} ????????????", userVO.getEmail());
            return Ret.failMsg(ResultEnum.SERVER_ERROR.getMessage());
        }
    }

    @Override
    public Ret userNameIsExit(String userName) {
        if (userNameIsExist(userName)) {
            return Ret.failMsg(ResultEnum.USER_EXIST.getMessage());
        } else {
            return Ret.ok("msg", "??????????????????");
        }
    }

    @Override
    public Ret emailIsExit(String email) {
        if (emailIsExist(email)) {
            return Ret.failMsg(ResultEnum.USER_EMAIL_EXIST.getMessage());
        } else {
            return Ret.ok("msg", "???????????????");
        }
    }

    @Override
    public Ret accountVerify(String key) {
        Assert.isTrue(StrUtil.isNotBlank(key), "????????????");
        String userId = redisUtil.getString("verify:email:key:" + key);
        if (StrUtil.isEmpty(userId)) {
            return Ret.failMsg("????????????????????????");
        }
        User user = userRepository.findTopById(Long.parseLong(userId));
        Assert.notNull(user, "???????????????????????????????????????");
        String password = RandomUtil.randomString(8);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(EnableStatusEnum.ENABLED);
        user.setRole(roleService.getRoleByKey(SysConstants.REGISTER_USER_DEFAULT_ROLE_KEY));
        userRepository.save(user);
        redisUtil.del("verify:email:key:" + key);
        return Ret.ok("msg", "????????????");
    }

    private boolean emailIsExist(String email) {
        return userRepository.existsByEmailAndIsDelete(email, 0);
    }

    private boolean userNameIsExist (String userName){
        User user = userRepository.findByUsernameAndIsDelete(userName, 0);
        return user != null;
    }

    @Override
    public Ret retrieve(String email, HttpServletRequest request) {
        Assert.isTrue(StrUtil.isNotBlank(email), "????????????");
        User user = userRepository.findByEmailAndIsDelete(email, 0);
        Assert.notNull(user, "???????????????");
        String uuid = IdUtil.simpleUUID();
        redisUtil.setString(RETRIEVE_PWD_KEY + uuid, user.getId().toString(), 86400);
        Context context = new Context();
        context.setVariable("url",  configure.getFrontendAddress() + "/passport/retrieve/" + uuid);
        String emailTemplate = templateEngine.process("retrieve-password", context);
        new Thread(() ->{
            try {
                mailService.sendHtmlMail(user.getEmail(), "????????????", emailTemplate);
            } catch (Exception e) {
                log.error("??????????????????????????????", e);
                redisUtil.del(RETRIEVE_PWD_KEY + uuid);
            }
        }).start();
        return Ret.ok("msg", "?????????????????????????????????????????????????????????");
    }

    @Override
    public Ret resetPwd(String uuid, String pwd) {
        Assert.isTrue(StrUtil.isNotBlank(uuid), "uuid ????????????");
        Assert.isTrue(StrUtil.isNotBlank(pwd), "pwd ????????????");
        String userId = redisUtil.getString(RETRIEVE_PWD_KEY + uuid);
        if (StrUtil.isEmpty(userId)) {
            return Ret.failMsg("??????????????????????????????");
        }
        User user = userRepository.findTopById(Long.parseLong(userId));
        Assert.notNull(user, "???????????????");
        user.setPassword(passwordEncoder.encode(pwd));
        userRepository.save(user);
        return Ret.ok("msg", "??????????????????");
    }

}
