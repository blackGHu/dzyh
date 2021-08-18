package com.njupt.dzyh.config;


import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

        Map<String, Filter> filter = new LinkedHashMap<>();
        filter.put("auth", new ShiroFilter());



        Map<String,String> filterMap = new HashMap<>();

        filterMap.put("/logout","logout");
        filterMap.put("/dzyh/set/*","authc");
        filterMap.put("/dzyh/manager/*","authc");
        filterMap.put("/dzyh/super/*","authc");
        /*
        filterMap.put("/dzyh/set/*","authc,auth");
        filterMap.put("/dzyh/manager/*","authc,auth");
        filterMap.put("/dzyh/super/*","authc,auth");*/

        factoryBean.setFilterChainDefinitionMap(filterMap);

        //factoryBean.setLoginUrl("/login.html");
        //factoryBean.setSuccessUrl("/index.html");
        //factoryBean.setUnauthorizedUrl("/error.html");
        factoryBean.setFilters(filter);



        return factoryBean;
    }


}















