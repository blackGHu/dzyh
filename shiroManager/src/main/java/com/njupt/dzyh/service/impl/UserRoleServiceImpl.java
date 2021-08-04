package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.njupt.dzyh.beans.User;
import com.njupt.dzyh.beans.UserRole;
import com.njupt.dzyh.dao.UserRoleDao;
import com.njupt.dzyh.domain.Information;
import com.njupt.dzyh.domain.roles.UserInfo;
import com.njupt.dzyh.domain.roles.UserRoleInfo;
import com.njupt.dzyh.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public List<UserRole> getByUserId(String userId) {
        QueryWrapper<UserRoleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<UserRoleInfo> userRoleInfos = userRoleDao.selectList(queryWrapper);
        //System.out.println("userRoleInfos数量"+userRoleInfos.size());
        List<UserRole> userRoles = new ArrayList<>();
        if(userRoleInfos.size()>0) {
            for (UserRoleInfo userRoleInfo : userRoleInfos) {
                UserRole userRole = new UserRole(userRoleInfo.getId(), userRoleInfo.getUserId(), userRoleInfo.getRoleId());
                userRoles.add(userRole);
            }
        }
        return userRoles;
    }

    @Override
    public Set<String> getByCondition(User user) {

        QueryWrapper<UserRoleInfo> queryWrapper = new QueryWrapper<>();
        if(user.getCurrentRoleId()>0&&user.getCurrentRoleId()<6)
            queryWrapper.eq("role_id",user.getCurrentRoleId())
                    .like("user_id",user.getUserId());
        else
            queryWrapper.like("user_id",user.getUserId());
        List<UserRoleInfo> records = userRoleDao.selectList(queryWrapper);

        Set<String> userIds = new HashSet<>();
        if(records==null) return userIds;
        for(UserRoleInfo record : records)
            userIds.add(record.getUserId());
        return userIds;
    }

    @Override
    public Set<String> getByCondition(User user,int current,int size) {
        System.out.println("分页查询 begin");
        if(current<=0){
            current = 1;
        }
        if(size<=0){
            size = 4;
        }
        QueryWrapper<UserRoleInfo> queryWrapper = new QueryWrapper<>();
        if(user.getCurrentRoleId()>0&&user.getCurrentRoleId()<6)
            queryWrapper.eq("role_id",user.getCurrentRoleId())
                    .like("user_id",user.getUserId());
        else
            queryWrapper.like("user_id",user.getUserId());
        Page<UserRoleInfo> page = new Page<>(current,size);
        userRoleDao.selectPage(page,queryWrapper);
        List<UserRoleInfo> records = page.getRecords();
        System.out.println("分页查询 end");
        Set<String> userIds = new HashSet<>();
        if(records==null) return userIds;
        for(UserRoleInfo record : records)
            userIds.add(record.getUserId());
        return userIds;
    }

    @Override
    public int insert(String userId, int[] roleIds) {
        QueryWrapper<UserRoleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<UserRoleInfo> userRoleInfos = userRoleDao.selectList(queryWrapper);
        Set<Integer> existRoleIds = new HashSet<>();
        List<UserRoleInfo> insertUserRoles = new ArrayList<>();
        if(userRoleInfos.size()>0){
            for(UserRoleInfo userRoleInfo : userRoleInfos)
                existRoleIds.add(userRoleInfo.getRoleId());
        }
        else if(roleIds.length>0){

            for(int roleId : roleIds){
                if(roleId<1||roleId>6) continue;//超出roleId的范围
                UserRoleInfo uRInfo = new UserRoleInfo();
                uRInfo.setUserId(userId);
                uRInfo.setRoleId(roleId);
                insertUserRoles.add(uRInfo);

            }
        }
        if(existRoleIds.size()>0 && roleIds.length>0){
            for(int roleId : roleIds){
                if(!existRoleIds.contains(roleId)){
                    if(roleId<1||roleId>6) continue;
                    UserRoleInfo uRInfo = new UserRoleInfo();
                    uRInfo.setUserId(userId);
                    uRInfo.setRoleId(roleId);
                    insertUserRoles.add(uRInfo);
                }
            }
        }

        if(insertUserRoles.size()>0){

            for(UserRoleInfo userRoleInfo : insertUserRoles){
                int rec = userRoleDao.insert(userRoleInfo);
                if(rec!=1) return -1;
            }
            return 0;
        }
        return 1;
    }

    @Override
    public int deleteByUserId(String userId) {
        QueryWrapper<UserRoleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        int rec = userRoleDao.delete(queryWrapper);
        if(rec==0) return -1;
        return 0;
    }
}












