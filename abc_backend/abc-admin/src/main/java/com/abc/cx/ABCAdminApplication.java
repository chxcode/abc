package com.abc.cx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @SpringBootApplication = (默认属性)@Configuration + @EnableAutoConfiguration + @ComponentScan
 * @SpringBootConfiguration 继承自@Configuration，二者功能也一致，标注当前类是配置类，
 * 并会将当前类内声明的一个或多个以@Bean注解标记的方法的实例纳入到spring容器中，并且实例名就是方法名。
 * @EnableAutoConfiguration 的作用启动自动的配置，@EnableAutoConfiguration注解的意思就是Springboot
 * 根据你添加的jar包来配置你项目的默认配置，比如根据spring-boot-starter-web ，来判断你的项目是否需要添加了
 * webmvc和tomcat，就会自动的帮你配置web项目中所需要的默认配置。
 * @ComponentScan 扫描当前包及其子包下被@Component，@Controller，@Service，@Repository注解标记的类
 * 并纳入到spring容器中进行管理。
 *
 * @ServletComponentScan Servlet、Filter、Listener 可以直接通过 @WebServlet、@WebFilter、@WebListener
 * 注解自动注册，无需其他代码
 *
 * @EnableCaching 开启缓存功能
 *
 * @EnableScheduling 开启定时器，发现注解@Scheduled的任务并后台执行
 *
 */

@SpringBootApplication(scanBasePackages = {"com.abc.cx"})
@ServletComponentScan
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
public class ABCAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ABCAdminApplication.class, args);
    }
}
