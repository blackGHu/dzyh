package com.njupt.dzyh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.njupt.dzyh.domain.Category;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.CategoryService;
import com.njupt.dzyh.utils.CommonResult;
import com.njupt.dzyh.utils.CommonUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
@RestController
@RequestMapping("/dzyh/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @RequestMapping("/selectAllCategory")
    public CommonResult selectAllCategory(){
        return categoryService.selectAll();
    }


    @RequestMapping("/insertCategory")
    public CommonResult insert(@RequestBody Category category){
        return categoryService.insert(category);
    }

    @RequestMapping("/deleteCategoryById")
    public CommonResult deleteCategoryById(@Param("categoryId") int categoryId){
        return categoryService.deletById(categoryId);
    }


    /**
    * 批量——类别信息录入  传入的是Excel还是Json格式的goodsList
     * 该为 正式版功能，最后调用该接口，但是不好测试，需要等前台联调
     * 暂用下方url方式测试
     * @param file
     * @return
             */
    @RequestMapping("/insertBatchByFile")
    public CommonResult insertBatchByFile(@RequestParam("file") MultipartFile file){
        CommonResult result = new CommonResult();
        try {
            //1. 先转换成json格式
            String fileName = file.getOriginalFilename();
            if(fileName.endsWith("xls") || fileName.endsWith(("XLS")) || fileName.endsWith(("XLSX")) || fileName.endsWith(("xlsx"))){
                JSONObject categoryJsObj = CommonUtil.excelToJson(file);
                System.out.println("goodsJsonObj---\t" + categoryJsObj.toJSONString());
                String key = "";
                if(categoryJsObj.size()>1){
                    return  CommonResult.error(CommonResultEm.ERROR,"表格有多个子表，无法对应对象，Excel转换失败");
                }
                for(Map.Entry<String, Object> entry:categoryJsObj.entrySet()){
                    key = entry.getKey();
                    System.out.println("key" + key);
                }
                List<Category> categoryList = JSONArray
                        .parseArray(JSONArray.toJSONString(categoryJsObj.get(key))).toJavaList(Category.class);
                System.out.println("goodsList---\t" + categoryList);
//                CommonResult rec = goodsService.insertBatch(goodsList);
                List<Category> errorList = new ArrayList<>();
                if (0 == categoryList.size() || null == categoryList) {
                    return CommonResult.error();
                } else {
                    for (Category category : categoryList) {
                        CommonResult rec = categoryService.insert(category);
                        if(!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                            errorList.add(category);
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
    public CommonResult insertBatchByUrl(@Param("url") String url){
        CommonResult result = new CommonResult();
        try {
            //1. 先转换成json格式
            JSONObject categoryJsonObj = CommonUtil
                    .excelToJson(url);
            System.out.println("categoryJsonObj---\t" + categoryJsonObj.toJSONString());
            //  即表格中只允许只有一个sheet
//            System.out.println("hhh---"+JSONArray.parseArray(JSONArray.toJSONString(categoryJsonObj.get("sheet0"))).toJavaList(Category.class));
            String key = "";
            if(categoryJsonObj.size()>1){
                return  CommonResult.error(CommonResultEm.ERROR,"表格有多个子表，无法对应对象，Excel转换失败");
            }
            for(Map.Entry<String, Object> entry:categoryJsonObj.entrySet()){
                key = entry.getKey();
                System.out.println("key" + key);
            }
            List<Category> categoryList = JSONArray
                    .parseArray(JSONArray.toJSONString(categoryJsonObj.get(key))).toJavaList(Category.class);
            System.out.println("categoryList---\t" + categoryList);
//            CommonResult rec = goodsService.insertBatch(goodsList);
            List<Category> errorList = new ArrayList<>();
            if (0 == categoryList.size() || null == categoryList) {
                return CommonResult.error();
            } else {
                for (Category category : categoryList) {
                    CommonResult rec = categoryService.insert(category);
                    if(!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                        errorList.add(category);
                        continue;
                    }
                }
                if (0 == errorList.size())
                    return CommonResult.success();
                else
                    return CommonResult.error(CommonResultEm.ERROR, "失败的列表(可能数据重复)：" + errorList);
            }
//                    Integer rec = goodsDao.insertBatchSomeColumn(goodsList);
//            result.setResultCode(rec.getResultCode());
//            result.setResultMessage(rec.getResultMessage());

        } catch (Exception e) {
            System.out.println(e.toString()+e.getLocalizedMessage());
            result.setResultCode(CommonResultEm.ERROR.getEcode());
            result.setResultMessage("Excel读取失败");
        }
        return result;
    }

}
