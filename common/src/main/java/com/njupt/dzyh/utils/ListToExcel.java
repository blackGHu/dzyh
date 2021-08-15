package com.njupt.dzyh.utils;

import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.GoodsApply;
import com.njupt.dzyh.domain.MaterialList;
import com.njupt.dzyh.domain.MaterialListOrder;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListToExcel {
    //Goods对象写入excel
    public static void goodsToExcel(String resource, String fileName,List<Goods> goodsList) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"类别","名称","规格","型号","价格","数量","存放地址","用途","购买人","权限","入库时间"};
        //创建excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表sheet
        XSSFSheet sheet = workbook.createSheet();
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = null;
        //插入第一行数据的表头
        for(int i = 0; i < title.length; i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }

        //写入数据
        if(goodsList!=null){
            for (int i = 0; i < goodsList.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue(goodsList.get(i).getCategoryName());
                ncell = nrow.createCell(1);
                ncell.setCellValue(goodsList.get(i).getGoodsName());
                ncell = nrow.createCell(2);
                ncell.setCellValue(goodsList.get(i).getGoodsSize());
                ncell = nrow.createCell(3);
                ncell.setCellValue(goodsList.get(i).getGoodsModel());
                ncell = nrow.createCell(4);
                ncell.setCellValue(goodsList.get(i).getGoodsPrice());
                ncell = nrow.createCell(5);
                ncell.setCellValue(goodsList.get(i).getGoodsNumbers());
                ncell = nrow.createCell(6);
                ncell.setCellValue(goodsList.get(i).getGoodsAddress());
                ncell = nrow.createCell(7);
                ncell.setCellValue(goodsList.get(i).getPurposeName());
                ncell = nrow.createCell(8);
                ncell.setCellValue(goodsList.get(i).getBuyUserName());
                ncell = nrow.createCell(9);
                ncell.setCellValue(goodsList.get(i).getRoleName());
                ncell = nrow.createCell(10);
                ncell.setCellValue(DateCommonUtil.getFormatDateStr(goodsList.get(i).getStorageTime(),"yyyy-MM-dd HH:mm:ss"));

            }
        }

        //创建excel文件

        File file = new File(resource,fileName);


        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


//GoodsApply对象写入excel
    public static void goodsApplyToExcel(String resource, String fileName,List<GoodsApply> goodsApplies) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"类别","名称","规格","型号","数量","存放地址","用途","申请时间","归还时间","申请人","审批人","审核状态","使用状态"};
        //创建excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表sheet
        XSSFSheet sheet = workbook.createSheet();
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = null;
        //插入第一行数据的表头
        for(int i = 0; i < title.length; i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        //写入数据
        if(goodsApplies!=null){
            for (int i = 0; i < goodsApplies.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue(goodsApplies.get(i).getCategoryName());
                ncell = nrow.createCell(1);
                ncell.setCellValue(goodsApplies.get(i).getRepertoryName());
                ncell = nrow.createCell(2);
                ncell.setCellValue(goodsApplies.get(i).getRepertorySize());
                ncell = nrow.createCell(3);
                ncell.setCellValue(goodsApplies.get(i).getRepertoryModel());
                ncell = nrow.createCell(4);
                ncell.setCellValue(goodsApplies.get(i).getRepertoryNumbers());
                ncell = nrow.createCell(5);
                ncell.setCellValue(goodsApplies.get(i).getRepertoryAddress());
                ncell = nrow.createCell(6);
                ncell.setCellValue(goodsApplies.get(i).getPurposeName());
                ncell = nrow.createCell(7);
                ncell.setCellValue(DateCommonUtil.getFormatDateStr(goodsApplies.get(i).getBorrowTime(),"yyyy-MM-dd HH:mm:ss"));
                ncell = nrow.createCell(8);
                ncell.setCellValue(DateCommonUtil.getFormatDateStr(goodsApplies.get(i).getReturnTime(),"yyyy-MM-dd HH:mm:ss"));
                ncell = nrow.createCell(9);
                ncell.setCellValue(goodsApplies.get(i).getApplyUserName());
                ncell = nrow.createCell(10);
                ncell.setCellValue(goodsApplies.get(i).getApprovalUserName());
                ncell = nrow.createCell(11);
                ncell.setCellValue(goodsApplies.get(i).getGoodsApprovalStatus());
                ncell = nrow.createCell(12);
                ncell.setCellValue(goodsApplies.get(i).getGoodsUseStatus());
            }
        }

        //创建excel文件

        File file = new File(resource,fileName);


        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //MaterialList对象写入excel
    public static void materialListToExcel(String resource, String fileName, List<MaterialList> materialLists) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"学院","专业","课程","班级","料单描述","教师"};
        //创建excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表sheet
        XSSFSheet sheet = workbook.createSheet();
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = null;
        //插入第一行数据的表头
        for(int i = 0; i < title.length; i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }

        //写入数据
        if(materialLists!=null){
            for (int i = 0; i < materialLists.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue(materialLists.get(i).getCollegeName());
                ncell = nrow.createCell(1);
                ncell.setCellValue(materialLists.get(i).getMajorName());
                ncell = nrow.createCell(2);
                ncell.setCellValue(materialLists.get(i).getCourseName());
                ncell = nrow.createCell(3);
                ncell.setCellValue(materialLists.get(i).getClassName());
                ncell = nrow.createCell(4);
                ncell.setCellValue(materialLists.get(i).getOrderDescribe());
                ncell = nrow.createCell(5);
                ncell.setCellValue(materialLists.get(i).getTeacherName());
            }
        }

        //创建excel文件

        File file = new File(resource,fileName);


        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //MaterialListOrder对象写入excel
    public static void materialListOrderToExcel(String resource, String fileName, List<MaterialListOrder> materialListOrders) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"学院","专业","课程","班级","料单描述","教师","申请数量","审核状态","审核人"};
        //创建excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表sheet
        XSSFSheet sheet = workbook.createSheet();
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = null;
        //插入第一行数据的表头
        for(int i = 0; i < title.length; i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        //写入数据
        if(materialListOrders!=null){
            for (int i = 0; i < materialListOrders.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue(materialListOrders.get(i).getCollegeName());
                ncell = nrow.createCell(1);
                ncell.setCellValue(materialListOrders.get(i).getMajorName());
                ncell = nrow.createCell(2);
                ncell.setCellValue(materialListOrders.get(i).getCourseName());
                ncell = nrow.createCell(3);
                ncell.setCellValue(materialListOrders.get(i).getClassName());
                ncell = nrow.createCell(4);
                ncell.setCellValue(materialListOrders.get(i).getOrderDescribe());
                ncell = nrow.createCell(5);
                ncell.setCellValue(materialListOrders.get(i).getTeacherName());
                ncell = nrow.createCell(6);
                ncell.setCellValue(materialListOrders.get(i).getApplyNumber());
                ncell = nrow.createCell(7);
                ncell.setCellValue(materialListOrders.get(i).getGoodsApprovalStatue());
                ncell = nrow.createCell(8);
                ncell.setCellValue(materialListOrders.get(i).getApplyUserName());
            }
        }

        //创建excel文件

        File file = new File(resource,fileName);


        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
