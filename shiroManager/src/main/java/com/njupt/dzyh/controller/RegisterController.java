package com.njupt.dzyh.controller;

import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.domain.roles.UserInfo;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.impl.UserInfoServiceImpl;
import com.njupt.dzyh.service.impl.UserRoleServiceImpl;
import com.njupt.dzyh.utils.CommonResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("dzyh/entry")
public class RegisterController {
    private Logger logger = LoggerFactory.getLogger(RegisterController.class);
    @Autowired
    private UserInfoServiceImpl userInfoService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    //注册
    @RequestMapping("/register")
    public CommonResult register(@RequestBody UserInfo userInfo){
        if(userInfo==null)
            return CommonResult.error(CommonResultEm.ERROR,"用户为空");
        int recRole = userRoleService.insert(userInfo.getUserId(),new int[]{userInfo.getRoleId()});
        logger.info("用户角色表插入状态为（0：成功，-1：失败）"+recRole);
        int rec = userInfoService.insertUser(userInfo);

        if(rec==-1) return CommonResult.error(CommonResultEm.ERROR,"注册失败");
        if(rec==1) return CommonResult.error(CommonResultEm.ERROR,"用户已存在");
        return CommonResult.success("注册成功，等待管理员审核");
    }

    //登录
    @RequestMapping("/login")
    public CommonResult login(@RequestBody User user){
        User returnUser;
        //检查是否审核通过
        UserInfo userInfo = userInfoService.getUserById(user.getUserId());
        if(userInfo==null) return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        else if(userInfo.getRegistStatus()==0) return CommonResult.error(CommonResultEm.ERROR,"等待管理员审核");
        else if(userInfo.getRegistStatus()==-1) return CommonResult.error(CommonResultEm.ERROR,"账号审核不通过");

        Subject currentUser = SecurityUtils.getSubject();
        //System.out.println(user);
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserId(), user.getUserPassword());
        try{
            currentUser.login(token);
            currentUser.getSession().setAttribute("currentUser",currentUser.getPrincipal());
            returnUser = (User) currentUser.getPrincipal();
            Map<Integer,String> roleMap = returnUser.getRoleNames();
            return CommonResult.success(user.getUserId()+"登录成功, 角色："+roleMap.get(returnUser.getCurrentRoleId()));
        }
        catch (UnknownAccountException uae){
            logger.info("用户 "+user.getUserId()+" 不存在");
            return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        }
        catch (IncorrectCredentialsException ice){
            logger.info("用户 "+user.getUserId()+" 的密码错误");
            return CommonResult.error(CommonResultEm.ERROR,"用户密码错误");
        }
        catch (LockedAccountException lae){
            logger.info("用户 "+user.getUserId()+"被锁定");
            return CommonResult.error(CommonResultEm.ERROR,"账号被锁定");
        }
        catch (AuthenticationException ae){
            logger.info("登录失败",ae);
            return CommonResult.error(CommonResultEm.ERROR,"登录失败");
        }

    }
}
