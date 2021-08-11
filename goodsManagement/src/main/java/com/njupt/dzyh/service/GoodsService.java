package com.njupt.dzyh.service;


import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.dto.GenerateExcel;
import com.njupt.dzyh.utils.CommonResult;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface GoodsService {
    CommonResult selectAll();

    CommonResult selectById(int goodsId);

    CommonResult insert(Goods user);

    CommonResult insertBatch(List<Goods> goodsList);

    CommonResult deletById(int goodsId);

    CommonResult update(Goods goods);

    CommonResult updateBatch(List<Goods> goodsList);

    CommonResult selectByConditions(Map<String, Object> map);

    CommonResult selectByPage(int current,int size,Map<String,Object> conditionsMap);

    CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException;
}
