package com.njupt.dzyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njupt.dzyh.domain.roles.RolePermissionsInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolePermissionsDao extends BaseMapper<RolePermissionsInfo> {
}
