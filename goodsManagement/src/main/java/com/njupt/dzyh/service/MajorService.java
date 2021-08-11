package com.njupt.dzyh.service;


import com.njupt.dzyh.domain.College;
import com.njupt.dzyh.domain.Major;
import com.njupt.dzyh.utils.CommonResult;

import java.util.List;
import java.util.Map;

public interface MajorService {
    CommonResult selectAll();

    CommonResult selectById(int majorId);

    CommonResult insert(Major major);

    CommonResult insertBatch(List<Major> majorList);

    CommonResult deletById(int majorId);

    CommonResult update(Major major);

    CommonResult selectByConditions(Map<String, Object> map);

}
