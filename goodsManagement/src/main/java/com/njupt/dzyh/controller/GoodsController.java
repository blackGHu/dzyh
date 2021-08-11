package com.njupt.dzyh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.dto.GenerateExcel;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.otherFunctions.DownLoad;
import com.njupt.dzyh.service.GoodsService;
import com.njupt.dzyh.service.InformationService;
import com.njupt.dzyh.utils.CommonResult;
import com.njupt.dzyh.utils.CommonUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${resource}")
    private String resource;
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
                                        @PathVariable("size") int size,
                                        @RequestBody(required = false) Map<String,Object> conditionsMap){
       return goodsService.selectByPage(current,size,conditionsMap);

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


    @RequestMapping("/selectByConditions")
    public CommonResult selectByConditions(@RequestBody(required = false) Map<String,Object> conditionsMap){
        return goodsService.selectByConditions(conditionsMap);
    }



//     ---------插入操作--------------
    /**
     * 单个——物品信息的录入
     * @param goods
     * @return
     */
    @RequestMapping("/insert")
    public CommonResult insert(@RequestBody Goods goods){

        return goodsService.insert(goods);
    }

    /**
     * 批量——物品信息录入  传入的是Excel还是Json格式的goodsList
     * @param goodsList
     * @return
     */
    @RequestMapping("/insertBatchByList")
    public CommonResult insertBatch(@RequestBody List<Goods> goodsList){
        System.out.println("goodsList---\t" + goodsList);
        for(Goods goods:goodsList){
            CommonResult result = informationService.add(goods);
            if(!result.getResultCode().equals(CommonResultEm.SUCCESS.getEcode()))
                return result;
        }
        return goodsService.insertBatch(goodsList);
    }

    /**
     * 批量——物品信息录入  传入的是Excel还是Json格式的goodsList
     * 该为 正式版功能，最后调用该接口，但是不好测试，需要等前台联调
     * 暂用下方url方式测试
     * @param file
     * @return
     */
    @RequestMapping("/insertBatchByFile")
    public CommonResult insertBatch(@RequestParam("file") MultipartFile file){
        CommonResult result = new CommonResult();
        try {
            //1. 先转换成json格式
            String fileName = file.getOriginalFilename();
            if(fileName.endsWith(".xls") || fileName.endsWith((".xlsx"))){
                JSONObject goodsJsonObj = CommonUtil.excelToJson(file);
                System.out.println("goodsJsonObj---\t" + goodsJsonObj.toJSONString());
                String key = "";
                if(goodsJsonObj.size()>1){
                    return  CommonResult.error(CommonResultEm.ERROR,"表格有多个子表，无法对应对象，Excel转换失败");
                }
                for(Map.Entry<String, Object> entry:goodsJsonObj.entrySet()){
                    key = entry.getKey();
                    System.out.println("key" + key);
                }
                List<Goods> goodsList = JSONArray
                        .parseArray(JSONArray.toJSONString(goodsJsonObj.get(key))).toJavaList(Goods.class);
                System.out.println("goodsList---\t" + goodsList);
//                CommonResult rec = goodsService.insertBatch(goodsList);
                List<Goods> errorList = new ArrayList<>();
                if (0 == goodsList.size() || null == goodsList) {
                    return CommonResult.error();
                } else {
                    for (Goods goods : goodsList) {
                        CommonResult rec = goodsService.insert(goods);
                        if(!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                            errorList.add(goods);
                            continue;
                        }
                    }
                    if (0 == errorList.size())
                        return CommonResult.success();
                    else
                        return CommonResult.error(CommonResultEm.ERROR, "失败的列表：" + errorList);
                }
//                    Integer rec = goodsDao.insertBatchSomeColumn(goodsList);
            }else{
                result.setResultCode(CommonResultEm.ERROR.getEcode());
                result.setResultMessage("文件格式不对【只接收xls、xlsx格式的文件】");
            }
        } catch (Exception e) {
            result.setResultCode(CommonResultEm.ERROR.getEcode());
            result.setResultMessage("Excel读取失败");
        }
        return result;
    }

    /**
     * 批量——物品信息录入  传入的是Excel还是Json格式的goodsList
     * 测试版——url测试批量录入
     * @param url
     * @return
     */
    @RequestMapping("/insertBatchByUrl")
    public CommonResult insertBatch(@Param("url") String url){
        CommonResult result = new CommonResult();
        try {
            //1. 先转换成json格式
            JSONObject goodsJsonObj = CommonUtil
                    .excelToJson(url);
            System.out.println("goodsJsonObj---\t" + goodsJsonObj.toJSONString());
            //  即表格中只允许只有一个sheet
            String key = "";
            if(goodsJsonObj.size()>1){
                return  CommonResult.error(CommonResultEm.ERROR,"表格有多个子表，无法对应对象，Excel转换失败");
            }
            for(Map.Entry<String, Object> entry:goodsJsonObj.entrySet()){
                key = entry.getKey();
                System.out.println("key" + key);
            }
            List<Goods> goodsList = JSONArray
                    .parseArray(JSONArray.toJSONString(goodsJsonObj.get(key))).toJavaList(Goods.class);
            System.out.println("goodsList---\t" + goodsList);
//            CommonResult rec = goodsService.insertBatch(goodsList);
            List<Goods> errorList = new ArrayList<>();
            if (0 == goodsList.size() || null == goodsList) {
                return CommonResult.error();
            } else {
                for (Goods goods : goodsList) {
                    CommonResult rec = goodsService.insert(goods);
                    if(!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                        errorList.add(goods);
                        continue;
                    }
                }
                if (0 == errorList.size())
                    return CommonResult.success();
                else
                    return CommonResult.error(CommonResultEm.ERROR, "失败的列表：" + errorList);
            }
//                    Integer rec = goodsDao.insertBatchSomeColumn(goodsList);
//            result.setResultCode(rec.getResultCode());
//            result.setResultMessage(rec.getResultMessage());

        } catch (Exception e) {
            result.setResultCode(CommonResultEm.ERROR.getEcode());
            result.setResultMessage("Excel读取失败");
        }
        return result;
    }


//     ---------删除操作--------------
    /**
     * 根据ID删除录入物品表的信息
     * 对于超管可以进行删除操作，如果原先是通过的 删除后要回加
     * @param goodsId
     * @return
     */
    @RequestMapping("/deleteByGoodsId")
    public CommonResult deleteByGoodsId(@Param("goodsId") int goodsId){
        Goods goods = JSONObject.parseObject(JSONObject.toJSONString(goodsService.selectById(goodsId).getObj()),Goods.class);
        System.out.println("hahahha-----------"+goods);
        CommonResult tempResult1 = goodsService.deletById(goodsId);
        CommonResult result = new CommonResult();
        if(tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
            CommonResult tempResult2 = informationService.add(goods);
            if(tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
                result.setResultCode(tempResult1.getResultCode());
                result.setResultMessage(tempResult1.getResultMessage());
            }else {
                return tempResult2;
            }
        }else {
            return tempResult1;
        }
        return result;
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





//     ---------导出报表--------------

    /**
     *
     * @param conditionsMap   条件封装成对象
     * @param outUrl
     * @param fileName
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping("/generateExcel")
    public CommonResult generateExcel(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                      @Param("outUrl") String outUrl,
                                      @Param("fileName") String fileName) throws IOException, ParseException {

        Object obj = goodsService.selectByConditions(conditionsMap).getObj();
        if(null == obj){
            return CommonResult.error(CommonResultEm.ERROR,"记录为空,无需导出报表");
        }
        List<Goods> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(Goods.class);
        GenerateExcel generateExcel = new GenerateExcel(list,outUrl,fileName);
        return goodsService.generateExcel(generateExcel);
    }

    //    ----------模板下载------------------
    @RequestMapping("/downLoadGoodsApplyTemplate")
    public void downLoadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) throws UnsupportedEncodingException {
        String fileName = "goodsTemplate.xls";
        DownLoad.downloadFile(resource,fileName,request,response);
    }




}
