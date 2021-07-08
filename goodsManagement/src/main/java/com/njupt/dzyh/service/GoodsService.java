package com.njupt.dzyh.service;


import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.utils.CommonResult;

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

    List<Goods> selectByConditions(Map<String, Object> map);

    CommonResult selectByPage(int current,int size);
}
