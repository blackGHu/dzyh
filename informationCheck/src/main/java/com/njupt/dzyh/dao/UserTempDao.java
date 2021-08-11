package com.njupt.dzyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njupt.dzyh.domain.roles.UserInfo;
import org.apache.ibatis.annotations.Mapper;

//为了不从shiro中导入，重写一个userInfo表达的查询
@Mapper
public interface UserTempDao extends BaseMapper<UserInfo> {
}
