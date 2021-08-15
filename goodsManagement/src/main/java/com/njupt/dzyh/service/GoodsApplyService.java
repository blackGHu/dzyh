package com.njupt.dzyh.service;


import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.GoodsApply;
import com.njupt.dzyh.domain.dto.GenerateExcel;
import com.njupt.dzyh.domain.dto.GenerateGoodsApplyExcel;
import com.njupt.dzyh.utils.CommonResult;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface GoodsApplyService {
    CommonResult selectAll();

    CommonResult selectById(int orderId);

    CommonResult insert(GoodsApply goodsApply);

    CommonResult insertBatch(List<GoodsApply> goodsApplyList);

    CommonResult deletById(int orderId);

    CommonResult update(GoodsApply goodsApply);

    CommonResult updateBatch(List<GoodsApply> goodsApplyList);

    CommonResult selectByConditions(Map<String, Object> map);

    CommonResult selectByPage(int current, int size, Map<String, Object> conditionsMap);
    CommonResult selectPersonalByPage(int current, int size, Map<String, Object> conditionsMap);


    CommonResult generateExcel(GenerateGoodsApplyExcel generateExcel) throws IOException, ParseException;
}
