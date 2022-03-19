package com.abc.cx.utils;

/**
 * @Author: ChangXuan
 * @Decription: 框架静态常量
 * @Date: 17:52 2021/3/23
 **/
public class ABCConstant {

    /**
     * 用于IP定位转换
     */
    public static final String REGION = "内网IP|内网IP";

    /**
     * 常用接口
     */
    public static class Url {
        // IP归属地查询
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }

}
