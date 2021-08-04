package com.njupt.dzyh.controller;

import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.domain.roles.UserInfo;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.impl.UserInfoServiceImpl;
import com.njupt.dzyh.service.impl.UserServiceImpl;
import com.njupt.dzyh.utils.CommonResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


//用户个人的操作
@RestController
@RequestMapping("dzyh/set")
public class SettingController {
    private Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private RegisterController registerController;

    @Autowired
    private UserInfoServiceImpl userInfoService;

    @Autowired
    private UserServiceImpl userService;


    //更改角色
    @RequestMapping("/changeRole")
    public CommonResult changeRole(@RequestBody UserInfo userInfo){
        int roleId = userInfo.getRoleId();
        Subject currentUser = SecurityUtils.getSubject();
        User user = (User) currentUser.getPrincipal();
        if(user.getCurrentRoleId()==roleId)
            return CommonResult.error(CommonResultEm.ERROR,"请切换到新角色");
        Map<Integer,String> roleMap = user.getRoleNames();
        if(!roleMap.containsKey(roleId))
            return CommonResult.error(CommonResultEm.ERROR,"您没有权限切换到该角色");

        user.setCurrentRoleId(roleId);
        int rec = userInfoService.updateCurrRoleId(user);
        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"切换失败请重试");
        currentUser.logout();
        return registerController.login(user);
    }

    //获取个人信息
    @RequestMapping("/getPersonal")
    public CommonResult getPersonal(){
        Subject currentUser = SecurityUtils.getSubject();
        User temp = (User) currentUser.getPrincipal();
        User user = userService.getUserById(temp.getUserId());
        if(user==null) return CommonResult.error(CommonResultEm.ERROR,"当前用户不存在或未登录");
        user.setUserPassword("");
        user.setRoles(null);

        return CommonResult.success(user);
    }

    //更新个人信息
    @RequestMapping("/updatePersonal")
    public CommonResult updatePersonal(@RequestBody UserInfo userInfo){
        Subject currentUser = SecurityUtils.getSubject();
        User temp = (User) currentUser.getPrincipal();
        if(!userInfo.getUserId().equals(temp.getUserId()))
            return CommonResult.error(CommonResultEm.ERROR,"您只能修改当前用户的信息");
        int rec = userInfoService.upDateUser(userInfo);
        if(rec == 0) {
            return CommonResult.success("修改成功");
        }
        else
            return CommonResult.error(CommonResultEm.ERROR,"修改失败");
    }

    //修改密码
    @RequestMapping("/changePass")
    public CommonResult changePass(@RequestBody UserInfo userInfo){
        //新密码存储在对象的userName中
        Subject currentUser = SecurityUtils.getSubject();
        User temp = (User) currentUser.getPrincipal();
        if(!userInfo.getUserId().equals(temp.getUserId())  || !userInfo.getUserPassword().equals(temp.getUserPassword())){
            return CommonResult.error(CommonResultEm.ERROR,"密码错误");
        }
        int rec = userInfoService.changePassword(userInfo);
        if(rec == 0) {
            currentUser.logout();
            return CommonResult.success("修改成功，请重新登录!");
        }
        else
            return CommonResult.error(CommonResultEm.ERROR,"修改失败");
    }


}











