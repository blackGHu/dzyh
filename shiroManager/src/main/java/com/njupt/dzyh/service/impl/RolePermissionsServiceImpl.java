package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njupt.dzyh.dao.RolePermissionsDao;
import com.njupt.dzyh.domain.roles.RolePermissionsInfo;
import com.njupt.dzyh.service.RolePermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RolePermissionsServiceImpl implements RolePermissionsService {
    @Autowired
    private RolePermissionsDao rolePermissionsDao;

    @Override
    public List<RolePermissionsInfo> getByRoleId(int roleId) {
        QueryWrapper<RolePermissionsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        List<RolePermissionsInfo> rolePermissionsInfos= rolePermissionsDao.selectList(queryWrapper);
        return rolePermissionsInfos;
    }
}
