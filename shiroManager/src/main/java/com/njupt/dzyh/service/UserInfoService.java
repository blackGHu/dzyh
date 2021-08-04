package com.njupt.dzyh.service;


import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.domain.roles.UserInfo;

import java.util.List;

public interface UserInfoService {
    //查
    UserInfo getUserById(String userId);
    //增
    int insertUser(UserInfo userInfo);
    //改
    int upDateUser(UserInfo userInfo);
    //删
    int deleteUserById(String userId);
    //重置密码
    int resetPassById(String userId);
    //注册审批
    int registerApprove(String userId,int status);
    //更新当前登录角色
    int updateCurrRoleId(User user);
    //修改用户密码
    int changePassword(UserInfo userInfo);
    //条件查询
    List<UserInfo> selectByCondition(UserInfo user,int current,int pageSize);
    List<UserInfo> selectByCondition(UserInfo user);
}
