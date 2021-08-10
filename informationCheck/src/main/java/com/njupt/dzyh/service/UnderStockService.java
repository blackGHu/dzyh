package com.njupt.dzyh.service;

import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.SelectResult;
import com.njupt.dzyh.domain.UnderStock;

import java.util.List;

public interface UnderStockService {
    int insert(UnderStock underStock);
    List<UnderStock> getByCondition(UnderStock underStock);
    SelectResult getByCondition(UnderStock underStock, int current, int size);
    int setStatus(UnderStock underStock);
    int setAllStatus();
    int delete(UnderStock underStock);
}
