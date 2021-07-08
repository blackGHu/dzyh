package com.njupt.dzyh.controller;

import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.GoodsService;
import com.njupt.dzyh.utils.CommonResult;
import com.njupt.dzyh.utils.CommonUtil;
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
    public CommonResult selectAll(){
        System.out.println("hello--- ");
        List<Goods> goodsList =  goodsService.selectAll();
        if(0 == goodsList.size()){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(goodsList);
    }

    /**
     * 根据ID查询物品
     * @param goodsId
     * @return
     */

    @RequestMapping("/selectById/{goodsId}")
    public CommonResult selectById(@PathVariable("goodsId") int goodsId){
        Goods good = goodsService.selectById(goodsId);
        if(CommonUtil.isNull(good)){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(good);
    }


    /**
     * 物品信息的录入
     * @param goods
     * @return
     */
    @RequestMapping("/insert")
    public CommonResult insert(Goods goods){
        if(CommonUtil.isNull(goods)){
            return CommonResult.error();
        }else if(CommonUtil.isNotNull(goodsService.selectById(goods.getGoodsId()))){
            return CommonResult.error(CommonResultEm.ALREADY_EXIST);
        }else {
            int rec = goodsService.insert(goods);
            if(rec == 1)
                return CommonResult.success();
            else
                return CommonResult.error();
        }
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
