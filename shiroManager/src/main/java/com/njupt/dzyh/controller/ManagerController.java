package com.njupt.dzyh.controller;


import com.njupt.dzyh.domain.SelectResult;
import com.njupt.dzyh.beans.UpdateUser;
import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.domain.roles.UserInfo;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.otherFunctions.DownLoad;
import com.njupt.dzyh.otherFunctions.ExcelToList;
import com.njupt.dzyh.otherFunctions.ListToExcel;
import com.njupt.dzyh.otherFunctions.UpLoad;
import com.njupt.dzyh.service.impl.UserInfoServiceImpl;
import com.njupt.dzyh.service.impl.UserRoleServiceImpl;
import com.njupt.dzyh.service.impl.UserServiceImpl;
import com.njupt.dzyh.utils.CommonResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
//@RequiresRoles("管理员")
@RequestMapping("dzyh/manager")

public class ManagerController {
    @Value("${resource}")
    private String resource;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserInfoServiceImpl userInfoService;

    //查询用户、角色信息
    //@RequiresPermissions("getUserByCondition")
    @RequestMapping("/getUserByCondition/{current}/{size}")
    public CommonResult getUserByCondition(@RequestBody User user, @PathVariable("current") int current,@PathVariable("size") int pageSize){
        //用户类型存在currentRole中，数值1,2,3,4,5
        System.out.println("11111");
        SelectResult selectResult = userRoleService.getByCondition(user,current,pageSize+1);
        Set<String> userIds = (Set<String>) selectResult.getList();
        System.out.println("11111");
        if(userIds.size()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到结果");
        List<User> result = new ArrayList<>();

        for(String userId : userIds){

            User temp = userService.getUserById(userId);
            if(temp==null) continue;
            temp.setRoles(null);
            temp.setUserPassword(null);
            result.add(temp);
        }
        if(result.size()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到结果");
        return CommonResult.success(new SelectResult(selectResult.getTotal(),result));
    }

    //下载用户 角色文件
    @RequestMapping("/userFile")
    //@RequestBody User user,
    public void exportUser(@RequestBody User user, HttpServletRequest request,HttpServletResponse response) throws IOException {
        //用户类型存在currentRole中，数值0,1,2,3,4
        Set<String> userIds = userRoleService.getByCondition(user);
        if(userIds==null) return ;
        List<User> result = new ArrayList<>();
        System.out.println("1111");
        for(String userId : userIds){
            User temp = userService.getUserById(userId);
            if(temp==null) continue;
            temp.setRoles(null);
            temp.setUserPassword(null);
            result.add(temp);
        }
        if(result.size()==0) return ;
        System.out.println("22222");
        String fileName= "user.xlsx";

        ListToExcel.userToExcel(resource,fileName,result);


        DownLoad.downloadFile(resource,fileName,request,response);

    }

    //更新某个用户信息
    @RequestMapping("/updateUserById")
    public CommonResult updateUserById(@RequestBody UpdateUser updateUser){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(updateUser.getUserId());
        userInfo.setUserName(updateUser.getUserName());
        userInfo.setUserPhone(updateUser.getUserPhone());
        int recUser = userInfoService.upDateUser(userInfo);
        if(recUser==-1) return CommonResult.error(CommonResultEm.ERROR,"个人信息更新失败");
        int recRole = userRoleService.insert(updateUser.getUserId(), updateUser.getRoleIds());
        if(recRole==1) return CommonResult.success("角色已存在，未更新");
        else if(recRole==-1) return CommonResult.error(CommonResultEm.ERROR,"角色更新失败");
        return CommonResult.success("更新成功");
    }

    /*用户删除在超级管理员中
    @RequestMapping("/deleteUserById")
    public CommonResult deleteUserById(@RequestParam("userId") String userId){
        int recUser = userInfoService.deleteUserById(userId);
        if(recUser==1) return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        else if(recUser==-1) return CommonResult.error(CommonResultEm.ERROR,"删除用户失败");

        int recRole = userRoleService.deleteByUserId(userId);
        if(recRole==-1) return CommonResult.error(CommonResultEm.ERROR,"角色删除失败");
        return CommonResult.success("删除成功");
    }*/

    //密码重置
    @RequestMapping("/resetPassById")
    public CommonResult resetPassById(@RequestBody User user){
        String userId = user.getUserId();
        int recUser = userInfoService.resetPassById(userId);
        if(recUser==1) return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        else if(recUser==-1) return CommonResult.error(CommonResultEm.ERROR,"重置密码失败");

        return CommonResult.success("密码重置为登陆账号");
    }

    //查看用户申请
    @RequestMapping("/getUserInfoByCondition/{current}/{size}")
    public CommonResult getUserInfoByCondition(@RequestBody UserInfo userInfo, @PathVariable("current") int current,@PathVariable("size") int pageSize){
        SelectResult selectResult  = userInfoService.selectByCondition(userInfo,current,pageSize+1);
        List<UserInfo> userInfos = (List<UserInfo>) selectResult.getList();

        if(userInfos==null) return CommonResult.error(CommonResultEm.ERROR,"未查询到用户");

        return CommonResult.success(new SelectResult(selectResult.getTotal(),userInfos));
    }

    //用户申请导出报表
    @RequestMapping("/userInfoFile")
    //@RequestBody UserInfo userInfo,
    public void exportUserInfo(@RequestBody UserInfo userInfo,HttpServletRequest request,HttpServletResponse response) throws IOException {
        List<UserInfo> userInfos = userInfoService.selectByCondition(userInfo);

        if(userInfos==null) return ;

        ListToExcel.userInfoToExcel(resource,userInfos);
        String fileName = "userInfo.xlsx";
        DownLoad.downloadFile(resource,fileName,request,response);
    }

    //注册审核
    @RequestMapping("/registApprove")
    public CommonResult registApprove(@RequestBody UserInfo userInfo){
        //userId registStatus
        int rec = userInfoService.registerApprove(userInfo.getUserId(),userInfo.getRegistStatus());
        if(rec==1) return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        else if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"审批失败");
        else if(rec==2) return CommonResult.error(CommonResultEm.ERROR,"审批状态码错误");
        else if(rec==3) return CommonResult.error(CommonResultEm.ERROR,"该用户已审批");
        return CommonResult.success("审批成功");
    }

    //批量审核
    @RequestMapping("/batchApprove")
    public CommonResult batchApprove(@RequestBody HashMap<String ,Object> map){
        List<String> userIds = (List<String>) map.get("userIds");
        int status = (int)map.get("registStatus");
        for(String userId : userIds){
            int rec = userInfoService.registerApprove(userId,status);
        }

        return CommonResult.success("审批完成");
    }

    //上传文件，批量注册（只是显示预览，未实际注册）
    @RequestMapping("/uploadUserFile")
    public CommonResult uploadUserFile(@RequestParam("file") MultipartFile file) throws IOException {
        int rec =  UpLoad.upLoadFile(resource,file);

        if(rec==-1)
            return CommonResult.error(CommonResultEm.ERROR,"上传失败");

        String fileName = "registUsers.xlsx";
        List<UserInfo> userInfos = ExcelToList.excelToUserInfo(resource,fileName);


        return CommonResult.success(userInfos);
    }

    //获取注册表模板
    @RequestMapping("/getRegistTemp")
    public void getRegistTemp(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String fileName= "registerTemplate.xlsx";

        ListToExcel.userToExcel(resource,fileName,null);
        //System.out.println(resource);
        DownLoad.downloadFile(resource,fileName,request,response);
    }

    //批量注册
    @RequestMapping("/batchRegister")
    public CommonResult batchRegister(){

        String fileName = "registUsers.xlsx";
        List<UserInfo> userInfos = ExcelToList.excelToUserInfo(resource,fileName);
        for(UserInfo userInfo : userInfos){
            int rec = userInfoService.insertUser(userInfo);
            int rec1 = userRoleService.insert(userInfo.getUserId(),new int[]{userInfo.getRoleId()});
        }

        return CommonResult.success("注册成功");
    }





}
