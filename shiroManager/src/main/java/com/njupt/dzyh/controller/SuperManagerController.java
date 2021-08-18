package com.njupt.dzyh.controller;


import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.otherFunctions.DownLoad;
import com.njupt.dzyh.service.impl.UserInfoServiceImpl;
import com.njupt.dzyh.service.impl.UserRoleServiceImpl;
import com.njupt.dzyh.utils.CommonResult;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequiresRoles("超级管理员")
@RequestMapping("dzyh/super")
public class SuperManagerController {
    Logger log = LoggerFactory.getLogger(SuperManagerController.class);

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private UserInfoServiceImpl userInfoService;


    //数据库配置信息

    //数据库地址
    @Value("${spring.datasource.url}")
    String url;
    //用户名
    @Value("${spring.datasource.username}")
    String username;
    //密码
    @Value("${spring.datasource.password}")
    String password;
    //需要备份的数据库名称，可以多个，多个用逗号隔开
    @Value("${dbNames}")
    String dbNames;
    //备份保存路径
    @Value("${dbFilePath}")
    String dbFilePath;
    @Value("${hostIp}")
    String host;


    @RequestMapping("/deleteUserById")
    public CommonResult deleteUserById(@RequestBody User user){
        String userId = user.getUserId();
        int recUser = userInfoService.deleteUserById(userId);
        if(recUser==1) return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        else if(recUser==-1) return CommonResult.error(CommonResultEm.ERROR,"删除用户失败");

        int recRole = userRoleService.deleteByUserId(userId);
        if(recRole==-1) return CommonResult.error(CommonResultEm.ERROR,"角色删除失败");
        return CommonResult.success("删除成功");
    }

    //数据库备份
    @RequestMapping("/exportSQL")
    public void exportSQL(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        String fileName = date + ".sql";
        try {
            if (exportDatabaseTool(host, username, password, dbFilePath, fileName, dbNames)) {
                System.out.println("数据库成功备份！！！");
            } else {
                System.out.println("数据库备份失败！！！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DownLoad.downloadFile(dbFilePath,fileName,request,response);

        //return CommonResult.success("备份成功");

    }


    public static boolean exportDatabaseTool(String hostIP, String userName, String password, String savePath, String fileName, String databaseName) throws InterruptedException {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            savePath = savePath + File.separator;
        }

        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + fileName), "utf8"));
            //备份指定表
//            Process process = Runtime.getRuntime().exec("mysqldump remoteservice -h" + hostIP + " -u" + userName + " -p" + password +" --set-charset=UTF8 " + "--tables t_area");
            //备份整个库
            Process process = Runtime.getRuntime().exec("mysqldump -h" + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 " + databaseName);
            //备份除xxx表以外
//            Process process = Runtime.getRuntime().exec("mysqldump -h" + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 " + "--ignore-table=" + databaseName + ".t_area " + databaseName);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
                printWriter.flush();
            }
            if (process.waitFor() == 0) {//0 表示线程正常终止。
                return true;
            }
            System.out.println("code" + process.waitFor());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }










}





















