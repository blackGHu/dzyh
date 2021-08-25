package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.domain.SelectResult;


import com.njupt.dzyh.domain.roles.UserInfo;

import com.njupt.dzyh.service.UserDao;
import com.njupt.dzyh.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserInfo getUserById(String userId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<UserInfo> userInfos = userDao.selectList(queryWrapper);
        if(userInfos.size()==0) return null;
        UserInfo userInfo = userInfos.get(0);
        return userInfo;
    }

    @Override
    public int insertUser(UserInfo userInfo) {
        if(userInfo==null)
            return -1;//用户为空
        else if(getUserById(userInfo.getUserId())!=null){
            return 1;//用户已存在
        }
        else{
            UserInfo user = new UserInfo();
            user.setUserId(userInfo.getUserId())
                    .setUserPassword(userInfo.getUserPassword())
                    .setUserName(userInfo.getUserName())
                    .setUserPhone(userInfo.getUserPhone())
                    .setRegistStatus(userInfo.getRegistStatus())
                    .setRoleId(userInfo.getRoleId());
            int rec = userDao.insert(user);
            if(rec==1) return 0;//成功
            else return -1;//失败
        }
    }

    @Override
    public int upDateUser(UserInfo userInfo) {
        if(userInfo==null) return -1;
        else if(getUserById(userInfo.getUserId())==null){
            return -1;//用户不存在
        }
        else{
            UserInfo user = getUserById(userInfo.getUserId());
            user.setUserName(userInfo.getUserName());
            user.setUserPhone(userInfo.getUserPhone());
            user.setRoleId(userInfo.getRoleId());
            int rec = userDao.updateById(user);
            if(rec==1) return 0;//成功
            else return -1;
        }
    }

    @Override
    public int deleteUserById(String userId) {
        if(userId.length()==0) return 1;//用户不存在
        if(getUserById(userId)==null)
            return 1;
        int id = getUserById(userId).getId();
        int rec = userDao.deleteById(id);
        if(rec==1)
            return 0;
        else
            return -1;//失败
    }

    @Override
    public int resetPassById(String userId) {
        if(userId.length()==0) return 1;//用户不存在
        if(getUserById(userId)==null)
            return 1;
        UserInfo userInfo = getUserById(userId);
        userInfo.setUserPassword(userInfo.getUserId());
        int rec = userDao.updateById(userInfo);
        if(rec==1)
            return 0;
        else
            return -1;
    }

    @Override
    public int registerApprove(String userId,int status) {
        if(userId.length()==0) return 1;//用户不存在
        if(getUserById(userId)==null)
            return 1;
        if(status!=-1&&status!=1)
            return 2;
        UserInfo userInfo1 = getUserById(userId);
        if(userInfo1.getRegistStatus()!=0)
            return 3;
        //0待审核，1审核通过，-1审核不通过
        userInfo1.setRegistStatus(status);
        int rec = userDao.updateById(userInfo1);
        if(rec==1)
            return 0;
        else
            return -1;
    }

    @Override
    public int updateCurrRoleId(User user) {
        if(user.getCurrentRoleId()<0|| user.getCurrentRoleId()>6)
            return -1;
        if(getUserById(user.getUserId())==null)
            return -1;
        UserInfo userInfo = getUserById(user.getUserId());
        userInfo.setRoleId(user.getCurrentRoleId());
        int rec = userDao.updateById(userInfo);
        if(rec==1)
            return 0;
        else
            return -1;
    }

    @Override
    public int changePassword(UserInfo userInfo) {
        if(getUserById(userInfo.getUserId())==null)
            return -1;
        UserInfo userInfo1 = getUserById(userInfo.getUserId());
        userInfo1.setUserPassword(userInfo.getUserName());
        int rec = userDao.updateById(userInfo1);
        if(rec==1)
            return 0;
        else
            return -1;
    }

    @Override
    public SelectResult selectByCondition(UserInfo user, int current, int size) {
        System.out.println("分页查询 begin");
        if(current<=0){
            current = 1;
        }
        if(size<=0){
            size = 4;
        }

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        String userId = "";
        if(user.getUserId()!=null)
            userId = user.getUserId();
        String userName = "";
        if(user.getUserName()!=null)
            userName = user.getUserName();
        int roleId = user.getRoleId();

        int registStatus = user.getRegistStatus();

        if(roleId==0 && registStatus!=0){
            queryWrapper.like("user_id",userId)

                    .like("user_name",userName)

                    .eq("regist_status",registStatus);
        }
        else if(roleId!=0 && registStatus!=0){
            queryWrapper.like("role_id",roleId)

                    .like("user_id",userId)

                    .like("user_name",userName)

                    .eq("regist_status",registStatus);
        }
        else if(roleId==0 && registStatus==0){
            queryWrapper.like("user_id",userId)

                    .like("regist_status",registStatus)

                    .like("user_name",userName);
        }
        else{
            queryWrapper.like("role_id",roleId)

                    .like("user_id",userId)

                    .like("user_name",userName)

                    .eq("regist_status",registStatus);
        }
       // queryWrapper.eq("deleted",0);
        Page<UserInfo> page = new Page<>(current,size);
        userDao.selectPage(page,queryWrapper);
        List<UserInfo> records = page.getRecords();
        if(records!=null){
            for(UserInfo record : records){
                record.setUserPassword(null);
                record.setUpdateTime(null);
                record.setVersion(null);
                record.setDeleted(null);
                record.setCreateTime(null);
            }
        }
        System.out.println("分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        return new SelectResult(total,records);

    }

    @Override
    public List<UserInfo> selectByCondition(UserInfo user) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        String userId = "";
        if(user.getUserId()!=null)
            userId = user.getUserId();
        String userName = "";
        if(user.getUserName()!=null)
            userName = user.getUserName();
        int roleId = user.getRoleId();

        int registStatus = user.getRegistStatus();

        if(roleId==0 && registStatus!=0){
            queryWrapper.like("user_id",userId)

                    .like("user_name",userName)

                    .like("regist_status",registStatus);
        }
        else if(roleId!=0 && registStatus!=0){
            queryWrapper.like("role_id",roleId)

                    .like("user_id",userId)

                    .like("user_name",userName)

                    .like("regist_status",registStatus);
        }
        else if(roleId==0 && registStatus==0){
            queryWrapper.like("user_id",userId)

                    .like("user_name",userName);
        }
        else{
            queryWrapper.like("role_id",roleId)

                    .like("user_id",userId)

                    .like("user_name",userName);
        }
        List<UserInfo> records = userDao.selectList(queryWrapper);
        return records;
    }
}

























