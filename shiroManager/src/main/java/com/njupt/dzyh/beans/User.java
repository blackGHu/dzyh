package com.njupt.dzyh.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;

    private String userPassword;

    private String userName;

    private String userPhone;

    private int currentRoleId;

    private Map<Integer,String> roleNames;

    //对应的角色集合
    private Set<Role> roles;
}
