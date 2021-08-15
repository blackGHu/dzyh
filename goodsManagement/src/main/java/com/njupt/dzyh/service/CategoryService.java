package com.njupt.dzyh.service;


import com.njupt.dzyh.domain.Category;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.dto.GenerateExcel;
import com.njupt.dzyh.utils.CommonResult;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface CategoryService {
    CommonResult selectAll();

    CommonResult selectById(Integer categoryId);

    CommonResult insert(Category category);

    CommonResult insertBatch(List<Category> categoryList);

    CommonResult deletById(Integer categoryId);

    CommonResult update(Category category);

    CommonResult selectByConditions(Map<String, Object> map);

    CommonResult generateExcel(List<Category> list,String outUrl,String fileName) throws IOException, ParseException;

}
