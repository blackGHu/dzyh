package com.njupt.dzyh.service;


import com.njupt.dzyh.domain.Category;
import com.njupt.dzyh.domain.College;
import com.njupt.dzyh.utils.CommonResult;

import java.util.List;
import java.util.Map;

public interface CollegeService {
    CommonResult selectAll();

    CommonResult selectById(int collegeId);

    CommonResult insert(College college);

    CommonResult insertBatch(List<College> collegeList);

    CommonResult deletById(int collegeId);

    CommonResult update(College college);

    CommonResult selectByConditions(Map<String, Object> map);

}
