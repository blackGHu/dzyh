package com.njupt.dzyh.otherFunctions;

import com.njupt.dzyh.controller.RegisterController;
import com.njupt.dzyh.domain.roles.UserInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelToList {

    public static List<UserInfo> excelToUserInfo(String resource,String fileName) {
        String excelPath = resource+fileName;

        List<UserInfo> list = new ArrayList<>();

        try {
            //String encoding = "GBK";
            File excel = new File(excelPath);
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
                    return null;
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
                System.out.println("开始读取文件");
                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);
                Map<String,Integer> map = new HashMap<>();
                map.put("游客",1);
                map.put("教师",2);
                map.put("学生",3);
                map.put("管理员",4);
                map.put("超级管理员",5);
                DecimalFormat df = new DecimalFormat("0");
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if(row!=null){
                        UserInfo userInfo = new UserInfo();
                        Cell cell = row.getCell(1);
                        String userId = df.format(cell.getNumericCellValue());
                        userInfo.setUserId(userId);
                        userInfo.setRoleId(map.get(row.getCell(0).toString()));
                        userInfo.setUserName(row.getCell(2).toString());

                        cell = row.getCell(3);
                        String userPhone = df.format(cell.getNumericCellValue());
                        userInfo.setUserPhone(userPhone);
                        userInfo.setUserPassword(userId);
                        userInfo.setRegistStatus(1);
                        list.add(userInfo);
                    }
                    /*if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                System.out.println(cell.toString());
                            }
                        }
                    }*/
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
