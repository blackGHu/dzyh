package com.njupt.dzyh.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    private int roleId;

    private String roleName;

    //角色对应的权限集合
    private Set<String> permissions;

}
