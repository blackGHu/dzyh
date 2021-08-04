package com.njupt.dzyh.service;

import com.njupt.dzyh.domain.roles.RolePermissionsInfo;

import java.util.List;

public interface RolePermissionsService {
    List<RolePermissionsInfo> getByRoleId(int roleId);
}
