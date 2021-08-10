package com.njupt.dzyh.config;

import com.njupt.dzyh.beans.Role;
import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.service.impl.UserServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserServiceImpl userService;

    private Logger logger = LoggerFactory.getLogger(MyRealm.class);

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("doGetAuthorizationInfo 授权");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //System.out.println(principalCollection.getPrimaryPrincipal());
        //String name = (String) principalCollection.getPrimaryPrincipal();
        //User user = userService.getUserById(name);
        User user = (User) principalCollection.getPrimaryPrincipal();
        Set<Role> roleSet = user.getRoles();
        for(Role role : roleSet){
            if(role.getRoleId()==user.getCurrentRoleId()) {
                simpleAuthorizationInfo.addRole(role.getRoleName());
                //System.out.println(role.getPermissions());
                if(role.getPermissions()!=null)
                simpleAuthorizationInfo.addStringPermissions(role.getPermissions());
            }
        }

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        logger.info("doGetAuthenticationInfo 认证");

        //UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        String name = authenticationToken.getPrincipal().toString();
        //System.out.println(name);
        User user = userService.getUserById(name);
        //System.out.println(user);
        if(user == null) {
            logger.info("用户不存在");
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,user.getUserPassword(),getName());

        return simpleAuthenticationInfo;
    }
}
