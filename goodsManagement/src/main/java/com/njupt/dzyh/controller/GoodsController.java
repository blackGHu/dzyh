package com.njupt.dzyh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.GoodsService;
import com.njupt.dzyh.service.InformationService;
import com.njupt.dzyh.utils.CommonResult;
import com.njupt.dzyh.utils.CommonUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class GoodsController {

    private final static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private InformationService informationService;

//     ---------查询操作--------------
    /**
     * 查询所有物品
     * @return
     */
    @RequestMapping("/selectAll")
    public CommonResult selectAll(){
        return goodsService.selectAll();
    }

    /**
     * 分页查询所有
     * @param current  当前页码         默认1
     * @param size     每页显示数量     默认4
     * @return
     */
    @RequestMapping("/selectAllByPage/{current}/{size}")
    public CommonResult selectAllByPage(@PathVariable("current") int current,
                                        @PathVariable("size") int size){
       return goodsService.selectByPage(current,size);

    }

    /**
     * 根据ID查询物品
     * @param goodsId
     * @return
     */
    @RequestMapping("/selectById")
    public CommonResult selectById(@Param("goodsId") int goodsId){
        return goodsService.selectById(goodsId);
    }



//     ---------插入操作--------------
    /**
     * 物品信息的录入
     * @param goods
     * @return
     */
    @RequestMapping("/insert")
    public CommonResult insert(@RequestBody Goods goods){
        informationService.add(goods);
        return goodsService.insert(goods);
    }

    /**
     * 批量插入  传入的是Excel还是Json格式的goodsList
     * @param goodsList
     * @return
     */
    @RequestMapping("/insertBatchByList")
    public CommonResult insertBatch(@RequestBody List<Goods> goodsList){
        System.out.println("goodsList---\t" + goodsList);
        return goodsService.insertBatch(goodsList);
    }

    @RequestMapping("/insertBatchByFile")
    public CommonResult insertBatch(@RequestParam("file") MultipartFile file){
        CommonResult result = new CommonResult();
        try {
            //1. 先转换成json格式
            JSONObject goodsJsonObj = CommonUtil.excelToJson(file);
            List<Goods> goodsList = JSONArray.parseArray(goodsJsonObj.toJSONString(),Goods.class);
            goodsService.insertBatch(goodsList);
            result.setResultCode(CommonResultEm.SUCCESS.getEcode());
            result.setResultMessage(CommonResultEm.SUCCESS.getEmsg());

        } catch (Exception e) {
            result.setResultCode(CommonResultEm.ERROR.getEcode());
            result.setResultMessage("Excel读取失败");
        }
        return result;
    }

    @RequestMapping("/insertBatchByUrl")
//    @RequestParam("url") String url
    public CommonResult insertBatch(String url){
        CommonResult result = new CommonResult();
        try {
            //1. 先转换成json格式
            JSONObject goodsJsonObj = CommonUtil
                    .excelToJson(url);
            System.out.println("goodsJsonObj---\t" + goodsJsonObj.toJSONString());
            List<Goods> goodsList = JSONArray
                    .parseArray(JSONArray.toJSONString(goodsJsonObj.get("Sheet1"))).toJavaList(Goods.class);
            System.out.println("goodsList---\t" + goodsList);
            CommonResult rec = goodsService.insertBatch(goodsList);
            result.setResultCode(rec.getResultCode());
            result.setResultMessage(rec.getResultMessage());

        } catch (Exception e) {
            result.setResultCode(CommonResultEm.ERROR.getEcode());
            result.setResultMessage("Excel读取失败");
        }
        return result;
    }


//     ---------删除操作--------------
    /**
     * 根据ID删除物品
     * @param goodsId
     * @return
     */
    @RequestMapping("/deleteById")
    public CommonResult deleteById(@Param("goodsId") int goodsId){
        return goodsService.deletById(goodsId);
    }




//     ---------更新操作--------------

    /**
     * 更新物品
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public CommonResult update(@RequestBody Goods goods){
        return goodsService.update(goods);
    }

    @RequestMapping("/updateBatch")
    public CommonResult updateBatch(@RequestBody List<Goods> goodsList){
        return goodsService.updateBatch(goodsList);
    }
}
