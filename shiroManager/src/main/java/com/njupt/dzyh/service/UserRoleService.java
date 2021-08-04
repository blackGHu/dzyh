package com.njupt.dzyh.service;

import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.beans.UserRole;
import com.njupt.dzyh.domain.roles.UserRoleInfo;

import java.util.List;
import java.util.Set;

public interface UserRoleService {
    List<UserRole> getByUserId(String userId);

    Set<String> getByCondition(User user);
    Set<String> getByCondition(User user, int current, int size);

    int insert(String userId,int[] roleIds);

    int deleteByUserId(String userId);
}
