package com.njupt.dzyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njupt.dzyh.domain.UnderStock;
import com.njupt.dzyh.domain.roles.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UnderStockDao extends BaseMapper<UnderStock> {
}
