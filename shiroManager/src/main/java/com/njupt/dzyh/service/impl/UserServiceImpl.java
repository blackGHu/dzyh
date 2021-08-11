package com.njupt.dzyh.service.impl;

import com.njupt.dzyh.beans.Role;
import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.beans.UserRole;
import com.njupt.dzyh.domain.roles.UserInfo;
import com.njupt.dzyh.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserInfoServiceImpl userInfoService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Override
    public User getUserById(String userId) {
        UserInfo userInfo = userInfoService.getUserById(userId);
        if(userInfo==null) return null;
        //System.out.println(userInfo);
        List<UserRole> userRoles = userRoleService.getByUserId(userId);
        Set<Role> roles  = new HashSet<>();
        Map<Integer,String> roleNames  = new HashMap<>();
        if(userRoles.size()>0) {
            for (UserRole userRole : userRoles) {
                Role role = roleService.getByRoleId(userRole.getRoleId());
                if(role==null) continue;
                roles.add(role);
                roleNames.putIfAbsent(role.getRoleId(),role.getRoleName());
            }
        }

        User user = new User(userInfo.getUserId(),userInfo.getUserPassword(),userInfo.getUserName(),userInfo.getUserPhone(),userInfo.getRoleId(),roleNames,roles);
        return user;
    }
}
