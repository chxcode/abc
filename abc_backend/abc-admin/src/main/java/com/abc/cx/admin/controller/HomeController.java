package com.abc.cx.admin.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 2:29 2021/11/7
 **/
@Controller
public class HomeController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    /**
     * 路由使用@RequestMapping设定，也可以给controller加@RequestMapping
     * @return
     */
    @RequestMapping(value = "/")
    public String home() {
        //直接返回一个页面，果不是模板，在页面名前加forward:，后面跟上文件名，搜索顺序是templates->static->public
        return "forward:index.html";
        //如果是模板，可以这样写 return "index";
    }

    @RequestMapping(value = ERROR_PATH)
    public String handleError() {
        //404错误返回主页，由前端自行处理路由
        return "forward:index.html";
    }

}
