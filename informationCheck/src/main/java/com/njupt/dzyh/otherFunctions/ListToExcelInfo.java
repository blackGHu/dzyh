package com.njupt.dzyh.otherFunctions;

import com.njupt.dzyh.domain.UnderStock;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ListToExcelInfo {
    public static void underStockToExcel(String resource, List<UnderStock> underStocks) throws IOException {
        //exportFilePath：D:/测试
        System.out.println("hhhh");
        //定义表头
        String[] title = {"类别","名称","规格","型号","数量","申请人","申请日期","阅读状态"};
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
        System.out.println("here");
        //写入数据
        for (int i = 0; i < underStocks.size(); i++){
            XSSFRow nrow = sheet.createRow(i+1);

            XSSFCell ncell = nrow.createCell(0);
            ncell.setCellValue(underStocks.get(i).getUsType());

            ncell = nrow.createCell(1);
            ncell.setCellValue(underStocks.get(i).getUsName());
            ncell = nrow.createCell(2);
            ncell.setCellValue(underStocks.get(i).getUsSize());
            ncell = nrow.createCell(3);
            ncell.setCellValue(underStocks.get(i).getUsModel());
            ncell = nrow.createCell(4);
            ncell.setCellValue(underStocks.get(i).getUsNumbers());
            ncell = nrow.createCell(5);
            ncell.setCellValue(underStocks.get(i).getUserName());
            ncell = nrow.createCell(6);
            ncell.setCellValue(underStocks.get(i).getCreateTime().toString());
            ncell = nrow.createCell(7);
            String readStatus="已读";
            if(underStocks.get(i).getReadStatus()==0)
                readStatus = "未读";
            ncell.setCellValue(readStatus);
        }
        //创建excel文件

        File file = new File(resource,"underStock.xlsx");


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
