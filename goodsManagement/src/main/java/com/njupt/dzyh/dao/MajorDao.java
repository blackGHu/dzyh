package com.njupt.dzyh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njupt.dzyh.domain.Category;
import com.njupt.dzyh.domain.Major;
import org.apache.ibatis.annotations.Mapper;

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
public interface MajorDao extends BaseMapper<Major> {

    Integer insertBatchSomeColumn(List<Major> majorList);

    Integer updateBatchSomeColumn(List<Major> majorList);



}
