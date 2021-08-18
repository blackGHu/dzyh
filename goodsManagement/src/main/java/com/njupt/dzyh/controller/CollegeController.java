package com.njupt.dzyh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.njupt.dzyh.domain.College;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.CategoryService;
import com.njupt.dzyh.service.CollegeService;
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
@RequestMapping("/dzyh/college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;


    @RequestMapping("/selectAllCollege")
    //@RequiresPermissions("selectAllCollege")
    @RequiresRoles(value = {"管理员","超级管理员","游客","教师","学生"}, logical = Logical.OR)
    public CommonResult selectAllCollege(){
        return collegeService.selectAll();
    }


    /**
     * 批量——学院信息录入  传入的是Excel还是Json格式的goodsList
     * 该为 正式版功能，最后调用该接口，但是不好测试，需要等前台联调
     * 暂用下方url方式测试
     * @param file
     * @return
     */
    @RequestMapping("/insertBatchByFile")
    @RequiresPermissions("insertBatchByFile")
    public CommonResult insertBatchByFile(@RequestParam("file") MultipartFile file){
        CommonResult result = new CommonResult();
        try {
            //1. 先转换成json格式
            String fileName = file.getOriginalFilename();
            if(fileName.endsWith("xls") || fileName.endsWith(("XLS")) || fileName.endsWith(("XLSX")) || fileName.endsWith(("xlsx"))){
                JSONObject collegeJsonObj = CommonUtil.excelToJson(file);
                System.out.println("collegeJsonObj---\t" + collegeJsonObj.toJSONString());
                String key = "";
                if(collegeJsonObj.size()>1){
                    return  CommonResult.error(CommonResultEm.ERROR,"表格有多个子表，无法对应对象，Excel转换失败");
                }
                for(Map.Entry<String, Object> entry:collegeJsonObj.entrySet()){
                    key = entry.getKey();
                    System.out.println("key" + key);
                }
                List<College> collegeList = JSONArray
                        .parseArray(JSONArray.toJSONString(collegeJsonObj.get(key))).toJavaList(College.class);
                System.out.println("collegeList---\t" + collegeList);
//                CommonResult rec = goodsService.insertBatch(goodsList);
                List<College> errorList = new ArrayList<>();
                if (0 == collegeList.size() || null == collegeList) {
                    return CommonResult.error();
                } else {
                    for (College college : collegeList) {
                        CommonResult rec = collegeService.insert(college);
                        if(!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                            errorList.add(college);
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
     * 批量——学院信息录入  传入的是Excel还是Json格式的goodsList
     * 测试版——url测试批量录入
     * @param url
     * @return
     */
    @RequestMapping("/insertBatchByUrl")
    @RequiresPermissions("insertBatchByUrl")
    public CommonResult insertBatchByUrl(@Param("url") String url){
        CommonResult result = new CommonResult();
        try {
            //1. 先转换成json格式
            JSONObject collegeJsonObj = CommonUtil
                    .excelToJson(url);
            System.out.println("collegeJsonObj---\t" + collegeJsonObj.toJSONString());
            //  即表格中只允许只有一个sheet
            String key = "";
            if(collegeJsonObj.size()>1){
                return  CommonResult.error(CommonResultEm.ERROR,"表格有多个子表，无法对应对象，Excel转换失败");
            }
            for(Map.Entry<String, Object> entry:collegeJsonObj.entrySet()){
                    key = entry.getKey();
                System.out.println("key" + key);
            }
            List<College> collegeList = JSONArray
                    .parseArray(JSONArray.toJSONString(collegeJsonObj.get(key))).toJavaList(College.class);
            System.out.println("collegeList---\t" + collegeList);
//            CommonResult rec = goodsService.insertBatch(goodsList);
            List<College> errorList = new ArrayList<>();
            if (0 == collegeList.size() || null == collegeList) {
                return CommonResult.error();
            } else {
                for (College college : collegeList) {
                    CommonResult rec = collegeService.insert(college);
                    if(!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                        errorList.add(college);
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
}
