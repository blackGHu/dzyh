package com.njupt.dzyh.service;


import com.njupt.dzyh.domain.Goods;

import java.util.List;
import java.util.Map;

public interface GoodsService {
    List<Goods> selectAll();

    Goods selectById(int id);

    int insert(Goods user);

    int deletById(int id);

    int update(Goods goods);

    List<Goods> selectByConditions(Map<String, Object> map);

    List<Goods> selectByPage();
}
