package com.njupt.dzyh.otherFunctions;

import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.domain.roles.UserInfo;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListToExcel {
    //User对象写入excel
    public static void userToExcel(String resource, String fileName,List<User> users) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"用户类型(游客，教师，学生，管理员，超级管理员)","账号","姓名","手机"};
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
        if(users!=null){
            for (int i = 0; i < users.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                Map<Integer,String> roleNames = users.get(i).getRoleNames();
                String roleName="";
                for(int k : roleNames.keySet())
                    roleName += roleNames.get(k)+",";
                ncell.setCellValue(roleName);
                ncell = nrow.createCell(1);
                ncell.setCellValue(users.get(i).getUserId());
                ncell = nrow.createCell(2);
                ncell.setCellValue(users.get(i).getUserName());
                ncell = nrow.createCell(3);
                ncell.setCellValue(users.get(i).getUserPhone());
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


//UserInfo对象写入excel
    public static void userInfoToExcel(String resource, List<UserInfo> usersInfo) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"用户类型","账号","姓名","手机","状态"};
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
        Map<Integer,String> roleNames = new HashMap<>();
        roleNames.put(1,"游客");
        roleNames.put(2,"教师");
        roleNames.put(3,"学生");
        roleNames.put(4,"管理员");
        roleNames.put(5,"超级管理员");
        Map<Integer,String> registNames = new HashMap<>();
        registNames.put(-1,"拒绝");
        registNames.put(0,"待审批");
        registNames.put(1,"通过");
        //写入数据
        if(usersInfo!=null){
            for (int i = 0; i < usersInfo.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue(roleNames.get(usersInfo.get(i).getRoleId()));
                ncell = nrow.createCell(1);
                ncell.setCellValue(usersInfo.get(i).getUserId());
                ncell = nrow.createCell(2);
                ncell.setCellValue(usersInfo.get(i).getUserName());
                ncell = nrow.createCell(3);
                ncell.setCellValue(usersInfo.get(i).getUserPhone());
                ncell = nrow.createCell(4);
                ncell.setCellValue(registNames.get(usersInfo.get(i).getRegistStatus()));
            }
        }

        //创建excel文件

        File file = new File(resource,"userInfo.xlsx");


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
