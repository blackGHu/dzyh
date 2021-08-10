package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.njupt.dzyh.dao.UserTempDao;
import com.njupt.dzyh.domain.roles.UserInfo;
import com.njupt.dzyh.service.UserTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserTempServiceImpl implements UserTempService {

    @Autowired
    private UserTempDao userTempDao;

    @Override
    public UserInfo getUserInfoByUserId(String userId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<UserInfo> userInfos = userTempDao.selectList(queryWrapper);
        if(userInfos.size()==0) return null;
        UserInfo userInfo = userInfos.get(0);
        return userInfo;
    }
}
