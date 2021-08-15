package com.njupt.dzyh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.dto.GenerateExcel;
import com.njupt.dzyh.domain.roles.UserInfo;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.otherFunctions.DownLoad;
import com.njupt.dzyh.service.GoodsService;
import com.njupt.dzyh.service.InformationService;
import com.njupt.dzyh.utils.*;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        System.out.println("xscxscsxscscscs");
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

//    /**
//     * 批量——物品信息录入  传入的是Excel还是Json格式的goodsList
//     * 该为 正式版功能，最后调用该接口，但是不好测试，需要等前台联调
//     * 暂用下方url方式测试
//     * @param file
//     * @return
//     */
//    @RequestMapping("/insertBatchByFile")
//    public CommonResult insertBatch(@RequestParam("file") MultipartFile file){
//        CommonResult result = new CommonResult();
//        try {
//            //1. 先转换成json格式
//            String fileName = file.getOriginalFilename();
//            if(fileName.endsWith(".xls") || fileName.endsWith((".xlsx"))){
//                JSONObject goodsJsonObj = CommonUtil.excelToJson(file);
//                System.out.println("goodsJsonObj---\t" + goodsJsonObj.toJSONString());
//                String key = "";
//                if(goodsJsonObj.size()>1){
//                    return  CommonResult.error(CommonResultEm.ERROR,"表格有多个子表，无法对应对象，Excel转换失败");
//                }
//                for(Map.Entry<String, Object> entry:goodsJsonObj.entrySet()){
//                    key = entry.getKey();
//                    System.out.println("key" + key);
//                }
//                List<Goods> goodsList = JSONArray
//                        .parseArray(JSONArray.toJSONString(goodsJsonObj.get(key))).toJavaList(Goods.class);
//                System.out.println("goodsList---\t" + goodsList);
////                CommonResult rec = goodsService.insertBatch(goodsList);
//                List<Goods> errorList = new ArrayList<>();
//                if (0 == goodsList.size() || null == goodsList) {
//                    return CommonResult.error();
//                } else {
//                    for (Goods goods : goodsList) {
//                        CommonResult rec = goodsService.insert(goods);
//                        if(!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
//                            errorList.add(goods);
//                            continue;
//                        }
//                    }
//                    if (0 == errorList.size())
//                        return CommonResult.success();
//                    else
//                        return CommonResult.error(CommonResultEm.ERROR, "失败的列表：" + errorList);
//                }
////                    Integer rec = goodsDao.insertBatchSomeColumn(goodsList);
//            }else{
//                result.setResultCode(CommonResultEm.ERROR.getEcode());
//                result.setResultMessage("文件格式不对【只接收xls、xlsx格式的文件】");
//            }
//        } catch (Exception e) {
//            result.setResultCode(CommonResultEm.ERROR.getEcode());
//            result.setResultMessage("Excel读取失败");
//        }
//        return result;
//    }
//
//    public void deleteFile(File... files) {
//        for (File file : files) {
//            if (file.exists()) {
//                file.delete();
//            }
//        }
//    }

    /**
     * 批量——物品信息录入  传入的是Excel还是Json格式的goodsList
     * 该为 正式版功能，最后调用该接口，但是不好测试，需要等前台联调
     * 暂用下方url方式测试
     * @param multfile
     * @return
     */
    @RequestMapping("/insertBatchByFile")
    public CommonResult insertBatch(@RequestParam("file") MultipartFile multfile){
        CommonResult result = new CommonResult();
        List<Goods> goodsList = new ArrayList<>();
        try {
//            // 获取文件名
//            String fileName = multfile.getOriginalFilename();
//            // 获取文件后缀
//            String prefix=fileName.substring(fileName.lastIndexOf("."));
//            // 用uuid作为文件名，防止生成的临时文件重复
//            File excel = File.createTempFile(UUIDGenerator.getUUID(), prefix);
//            // MultipartFile to File
//            multfile.transferTo(excel);

            //你的业务逻辑

            File excel = MultipartFileToFileUtils.multipartFileToFile(multfile);

            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new XSSFWorkbook(fis);
                } else {
                    System.out.println("文件类型错误!");
                    return CommonResult.error(CommonResultEm.ERROR,"文件类型错误");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
                System.out.println("开始读取文件");
                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);
                DecimalFormat df = new DecimalFormat("0");
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        Goods goods = new Goods();
                        Cell cell = null;
                        String categoryName = row.getCell(0).toString();
                        String goodsName = row.getCell(1).toString();
                        String goodsSize = row.getCell(2).toString();
                        String goodsModel = row.getCell(3).toString();
                        System.out.println("goodsModel:\t" + goodsModel);
//                      Cell cell = row.getCell(4);
                        Double goodsPrice = Double.parseDouble(row.getCell(4).toString());
                        cell = row.getCell(5);
                        Double goodsNum = Double.parseDouble(row.getCell(5).toString());
                        Object inputValue1 = null;// 单元格值
                        if (!CommonUtil.isNull(cell) && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            int longVal = (int) Math.round(cell.getNumericCellValue());
                            if (Double.parseDouble(longVal + ".0") == goodsNum)
                                inputValue1 = longVal;
                            else
                                inputValue1 = goodsNum;
                        }
                        String goodsAddress = row.getCell(6).toString();
                        String goodsPup = row.getCell(7).toString();
                        String buyer = row.getCell(8).toString();
                        String roleName = row.getCell(9).toString();
//                        cell = row.getCell(9);
//                        Double roleId = Double.parseDouble(row.getCell(9).toString());
//                        Object inputValue2 = null;// 单元格值
//                        if (!CommonUtil.isNull(cell) && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            int longVal = (int) Math.round(cell.getNumericCellValue());
//                            if (Double.parseDouble(longVal + ".0") == roleId)
//                                inputValue2 = longVal;
//                            else
//                                inputValue2 = roleId;
//                        }
//                        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

//                        System.out.println("storageTime:\t" + row.getCell(10).toString());
//                        Date storageTime = DateCommonUtil.stringToDate(row.getCell(10).toString(), "yyyy-MM-dd HH:mm:ss");
                        goods.setCategoryName(categoryName)
                                .setStorageTime(DateCommonUtil.getSpecifyFormatDate(new Date(),"yyyy-MM-dd HH:mm:ss"))
                                .setGoodsName(goodsName)
                                .setBuyUserName(buyer)
                                .setGoodsAddress(goodsAddress)
                                .setGoodsSize(goodsSize)
                                .setPurposeName(goodsPup)
                                .setGoodsPrice(goodsPrice)
                                .setGoodsModel(goodsModel)
                                .setRoleName(roleName)
                                .setGoodsNumbers((int) inputValue1);
                        System.out.println(goods);
                        goodsList.add(goods);
                    }
                }
                List<Goods> errorList = new ArrayList<>();
                if (0 == goodsList.size() || null == goodsList) {
                    return CommonResult.error();
                } else {
                    for (Goods goods1 : goodsList) {
                        CommonResult rec = goodsService.insert(goods1);
                        if (!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
                            errorList.add(goods1);
                            continue;
                        }
                    }
                    if (0 == errorList.size())
                        return CommonResult.success();
                    else
                        return CommonResult.error(CommonResultEm.ERROR, "失败的列表：" + errorList);
                }
                //程序结束时，删除临时文件
            } else {
//                deleteFile(excel);
                System.out.println("找不到指定的文件");
                return CommonResult.error(CommonResultEm.ERROR,"找不到指定的文件");
            }
        } catch (Exception e) {
            result.setResultCode(CommonResultEm.ERROR.getEcode());
            result.setResultMessage("Excel读取失败");
            e.printStackTrace();
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

    /**
     * 批量——物品信息录入  传入的是Excel还是Json格式的goodsList
     * 测试版——url测试批量录入
     * @param url
     * @return
     */
    @RequestMapping("/insertBatch")
    public CommonResult insertBatch(@Param("url") String url){
        CommonResult result = new CommonResult();
        List<Goods> goodsList = new ArrayList<>();
        try {

            File excel = new File(url);
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new XSSFWorkbook(fis);
                } else {
                    System.out.println("文件类型错误!");
                    return CommonResult.error(CommonResultEm.ERROR,"文件类型错误");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
                System.out.println("开始读取文件");
                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);
                DecimalFormat df = new DecimalFormat("0");
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        Goods goods = new Goods();
                        Cell cell = null;
                        String categoryName = row.getCell(0).toString();
                        String goodsName = row.getCell(1).toString();
                        String goodsSize = row.getCell(2).toString();
                        String goodsModel = row.getCell(3).toString();
                        System.out.println("goodsModel:\t" + goodsModel);
//                      Cell cell = row.getCell(4);
                        Double goodsPrice = Double.parseDouble(row.getCell(4).toString());
                        cell = row.getCell(5);
                        Double goodsNum = Double.parseDouble(row.getCell(5).toString());
                        Object inputValue1 = null;// 单元格值
                        if (!CommonUtil.isNull(cell) && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            int longVal = (int) Math.round(cell.getNumericCellValue());
                            if (Double.parseDouble(longVal + ".0") == goodsNum)
                                inputValue1 = longVal;
                            else
                                inputValue1 = goodsNum;
                        }
                        String goodsAddress = row.getCell(6).toString();
                        String goodsPup = row.getCell(7).toString();
                        String buyer = row.getCell(8).toString();
                        String roleName = row.getCell(9).toString();
//                        cell = row.getCell(9);
//                        Double roleId = Double.parseDouble(row.getCell(9).toString());
//                        Object inputValue2 = null;// 单元格值
//                        if (!CommonUtil.isNull(cell) && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            int longVal = (int) Math.round(cell.getNumericCellValue());
//                            if (Double.parseDouble(longVal + ".0") == roleId)
//                                inputValue2 = longVal;
//                            else
//                                inputValue2 = roleId;
//                        }
                        Date storageTime = DateCommonUtil.stringToDate(row.getCell(10).toString(), "yyyy-MM-dd HH:mm:ss");
                        goods.setCategoryName(categoryName)
                                .setStorageTime(storageTime)
                                .setGoodsName(goodsName)
                                .setBuyUserName(buyer)
                                .setGoodsAddress(goodsAddress)
                                .setGoodsSize(goodsSize)
                                .setPurposeName(goodsPup)
                                .setGoodsPrice(goodsPrice)
                                .setGoodsModel(goodsModel)
                                .setRoleName(roleName)
                                .setGoodsNumbers((int) inputValue1);
                        System.out.println(goods);
                        goodsList.add(goods);
                    }
                }
                    List<Goods> errorList = new ArrayList<>();
                    if (0 == goodsList.size() || null == goodsList) {
                        return CommonResult.error();
                    } else {
                        for (Goods goods1 : goodsList) {
                            CommonResult rec = goodsService.insert(goods1);
                            if (!rec.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
                                errorList.add(goods1);
                                continue;
                            }
                        }
                        if (0 == errorList.size())
                            return CommonResult.success();
                        else
                            return CommonResult.error(CommonResultEm.ERROR, "失败的列表：" + errorList);
                    }
            } else {
                System.out.println("找不到指定的文件");
                return CommonResult.error(CommonResultEm.ERROR,"找不到指定的文件");
            }
        } catch (Exception e) {
            result.setResultCode(CommonResultEm.ERROR.getEcode());
            result.setResultMessage("Excel读取失败");
            e.printStackTrace();
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
     * @param fileName
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping("/generateExcel")
    public CommonResult generateExcel(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                      @Param("fileName") String fileName,HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {

        Object obj = goodsService.selectByConditions(conditionsMap).getObj();
        if(null == obj){
            return CommonResult.error(CommonResultEm.ERROR,"记录为空,无需导出报表");
        }
        List<Goods> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(Goods.class);
        ListToExcel.goodsToExcel(resource,fileName,list);

        DownLoad.downloadFile(resource,fileName,request,response);
        return CommonResult.success();
//        GenerateExcel generateExcel = new GenerateExcel(list,outUrl,fileName);
//        return goodsService.generateExcel(generateExcel);
    }

    //    ----------模板下载------------------
    @RequestMapping("/downLoadGoodsApplyTemplate")
    public void downLoadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        String fileName = "goodsTemplate.xls";
        ListToExcel.goodsToExcel(resource,fileName,null);
        DownLoad.downloadFile(resource,fileName,request,response);
    }




}
