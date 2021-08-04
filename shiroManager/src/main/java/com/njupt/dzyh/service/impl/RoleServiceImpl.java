package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njupt.dzyh.beans.Role;
import com.njupt.dzyh.beans.RolePermission;
import com.njupt.dzyh.dao.PermissionsDao;
import com.njupt.dzyh.dao.RoleDao;
import com.njupt.dzyh.dao.RolePermissionsDao;
import com.njupt.dzyh.domain.roles.PermissionsInfo;
import com.njupt.dzyh.domain.roles.RoleInfo;
import com.njupt.dzyh.domain.roles.RolePermissionsInfo;
import com.njupt.dzyh.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RolePermissionsDao rolePermissionsDao;

    @Autowired
    private PermissionsDao permissionsDao;

    @Override
    public Role getByRoleId(int roleId) {
        RoleInfo roleInfo = roleDao.selectById(roleId);
        QueryWrapper<RolePermissionsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        List<RolePermissionsInfo> rolePermissionsInfos= rolePermissionsDao.selectList(queryWrapper);
        Set<String> permissions = new HashSet<>();
        for(RolePermissionsInfo rolePermissionsInfo:rolePermissionsInfos){
            PermissionsInfo permissionsInfo = permissionsDao.selectById(rolePermissionsInfo.getPermissionsId());
            permissions.add(permissionsInfo.getPermissionsName());
        }
        //if(permissions.size()==0) return null;
        Role role = new Role(roleInfo.getId(),roleInfo.getRoleName(),permissions);
        return role;
    }
}
