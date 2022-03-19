package com.abc.cx.config.security;

import cn.hutool.core.util.StrUtil;
import com.abc.cx.domain.Menu;
import com.abc.cx.domain.RoleMenu;
import com.abc.cx.domain.User;
import com.abc.cx.domain.repository.MenuRepository;
import com.abc.cx.domain.repository.RoleMenuRepository;
import com.abc.cx.enums.MenuTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: ChangXuan
 * @Decription: 权限校验
 * @Date: 9:21 2020/4/23
 **/
@Component("rbacAuthorityService")
public class RbacAuthorityService {

    private MenuRepository menuRepository;

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }


    private RoleMenuRepository roleMenuRepository;

    @Autowired
    public void setRoleMenuRepository(RoleMenuRepository roleMenuRepository) {
        this.roleMenuRepository = roleMenuRepository;
    }

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object userInfo = authentication.getPrincipal();
        boolean hasPermission = false;
        if (userInfo instanceof UserDetails){
            User user = (User)userInfo;
            Long roleId = user.getRole().getId();
            List<RoleMenu> roleMenus = roleMenuRepository.findAllByRoleId(roleId);
            List<Long> menuIds = new ArrayList<>();
            for (int i = 0; i < roleMenus.size(); i++){
                menuIds.add(roleMenus.get(i).getMenuId());
            }
            List<Menu> menus = menuRepository.findAllById(menuIds);
            List<Menu> btnMenus = menus.stream()
                    .filter(menu -> StrUtil.equals(menu.getType(), MenuTypeEnum.BUTTON.toString()))
                    .filter(menu -> StrUtil.isNotBlank(menu.getUrl()))
                    .filter(menu -> StrUtil.isNotBlank(menu.getMethod()))
                    .collect(Collectors.toList());
            for (Menu menu : btnMenus) {
                AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(menu.getUrl(), menu.getMethod());
                if (antPathMatcher.matches(request)) {
                    hasPermission = true;
                    break;
                }
            }
            return hasPermission;
        }else {

            return false;
        }

    }
}
