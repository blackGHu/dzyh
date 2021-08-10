package com.njupt.dzyh.service;

import com.njupt.dzyh.domain.SelectResult;
import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.beans.UserRole;

import java.util.List;
import java.util.Set;

public interface UserRoleService {
    List<UserRole> getByUserId(String userId);

    Set<String> getByCondition(User user);
    SelectResult getByCondition(User user, int current, int size);

    int insert(String userId,int[] roleIds);

    int deleteByUserId(String userId);
}
