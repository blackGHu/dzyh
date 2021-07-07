package com.njupt.dzyh.controller;

import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.service.GoodsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/dzyh/goods")
@Transactional
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    /**
     * 查询所有物品
     * @return
     */
    @RequestMapping("/selectAll")
    public List<Goods> selectAll(){
        System.out.println("hello--- ");
        return goodsService.selectAll();
    }

    /**
     * 根据ID查询物品
     * @param goodsId
     * @return
     */

    @RequestMapping("/selectById/{goodsId}")
    public Goods selectById(@PathVariable("goodsId") int goodsId){
        return goodsService.selectById(goodsId);
    }


    @RequestMapping("/insert")
    public int insert(Goods goods){
        return goodsService.insert(goods);
    }

    @RequestMapping("/deleteById")
    public int deleteById(@Param("goodsId") int goodsId){
        return goodsService.deletById(goodsId);
    }

    @RequestMapping("/update")
    public int update(Goods goods){
        return goodsService.update(goods);
    }
}
