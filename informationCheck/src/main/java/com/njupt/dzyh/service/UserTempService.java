package com.njupt.dzyh.service;

import com.njupt.dzyh.domain.roles.UserInfo;

public interface UserTempService {
    UserInfo getUserInfoByUserId(String userId);
}
