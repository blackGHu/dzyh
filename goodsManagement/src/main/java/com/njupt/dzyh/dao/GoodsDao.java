package com.njupt.dzyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njupt.dzyh.domain.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;


/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:
 */
@Mapper
public interface GoodsDao extends BaseMapper<Goods> {

    Integer insertBatchSomeColumn(List<Goods> goodsList);

    Integer updateBatchSomeColumn(List<Goods> goodsList);
}
