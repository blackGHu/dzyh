package com.njupt.dzyh.config;


import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //1.realm代表一个系统资源
    @Bean
    public Realm shiroRealm(){
        return new MyRealm();
    }
    //2.SecurityManger 做流程控制
    @Bean
    public DefaultWebSecurityManager securityManager(Realm shiroRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    //3.ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        Map<String,String> filterMap = new HashMap<>();

        filterMap.put("/logout","logout");
        filterMap.put("/dzyh/set/*","authc");

        factoryBean.setFilterChainDefinitionMap(filterMap);
        factoryBean.setLoginUrl("/login.html");
        factoryBean.setSuccessUrl("/index.html");
        factoryBean.setUnauthorizedUrl("/error.html");



        return factoryBean;
    }
}















