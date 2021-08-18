package com.njupt.dzyh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.njupt.dzyh.domain.Major;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.CategoryService;
import com.njupt.dzyh.service.MajorService;
import com.njupt.dzyh.utils.CommonResult;
import com.njupt.dzyh.utils.CommonUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/dzyh/major")
public class MajorController {

    @Autowired
    private MajorService majorService;


    @RequestMapping("/selectAllMajor")
    //@RequiresPermissions("selectAllMajor")
    @RequiresRoles(value = {"管理员","超级管理员","游客","教师","学生"}, logical = Logical.OR)
    public CommonResult selectAllMajor(){
        return majorService.selectAll();
    }


    /**
     * 批量——学院信息录入  传入的是Excel还是Json格式的goodsList
     * 该为 正式版功能，最后调用该接口，但是不好测试，需要等前台联调
     * 暂用下方url方式测试
     * @param file
     * @return
     */
    @RequestMapping("/insertBatchByFile")
    @RequiresPermissions("insertMajorByFile")
    public CommonResult insertBatchByFile(@RequestParam("file") MultipartFile file){
        CommonResult result = new CommonResult();
        try {
            //1. 先转换成json格式
            String fileName = file.getOriginalFilename();
            if(fileName.endsWith("xls") || fileName.endsWith(("XLS")) || fileName.endsWith(("XLSX")) || fileName.endsWith(("xlsx"))){
                JSONObject majorJsObj = CommonUtil.excelToJson(file);
                System.out.println("majorJsonObj---\t" + majorJsObj.toJSONString());
                String key = "";
                if(majorJsObj.size()>1){
                    return  CommonResult.error(CommonResultEm.ERROR,"表格有多个子表，无法对应对象，Excel转换失败");
                }
                for(Map.Entry<String, Object> entry:majorJsObj.entrySet()){
                    key = entry.getKey();
                    System.out.println("key" + key);
                }
                List<Major> majorList = JSONArray
                        .parseArray(JSONArray.toJSONString(majorJsObj.get(key))).toJavaList(Major.class);
                System.out.println("majorList---\t" + majorList);
//                CommonResult rec = goodsService.insertBatch(goodsList);
                List<Major> errorList = new ArrayList<>();
                if (0 == majorList.size() || null == majorList) {
                    return CommonResult.error();
                } else {
                    for (Major major : majorList) {
                        CommonResult rec = majorService.insert(major);
                        if(!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                            errorList.add(major);
                            continue;
                        }
                    }
                    if (0 == errorList.size())
                        return CommonResult.success();
                    else
                        return CommonResult.error(CommonResultEm.ERROR, "失败的列表（可能数据重复）：" + errorList);
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
            JSONObject majorJsonObj = CommonUtil
                    .excelToJson(url);
            System.out.println("majorJsonObj---\t" + majorJsonObj.toJSONString());
            //  即表格中只允许只有一个sheet
            String key = "";
            if(majorJsonObj.size()>1){
                return  CommonResult.error(CommonResultEm.ERROR,"表格有多个子表，无法对应对象，Excel转换失败");
            }
            for(Map.Entry<String, Object> entry:majorJsonObj.entrySet()){
                key = entry.getKey();
                System.out.println("key" + key);
            }
            List<Major> majorList = JSONArray
                    .parseArray(JSONArray.toJSONString(majorJsonObj.get(key))).toJavaList(Major.class);
            System.out.println("majorList---\t" + majorList);
//            CommonResult rec = goodsService.insertBatch(goodsList);
            List<Major> errorList = new ArrayList<>();
            if (0 == majorList.size() || null == majorList) {
                return CommonResult.error();
            } else {
                for (Major major : majorList) {
                    CommonResult rec = majorService.insert(major);
                    if(!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                        errorList.add(major);
                        continue;
                    }
                }
                if (0 == errorList.size())
                    return CommonResult.success();
                else
                    return CommonResult.error(CommonResultEm.ERROR, "失败的列表（可能数据重复）：" + errorList);
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
}
