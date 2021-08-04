package com.njupt.dzyh.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser {
    private String userId;

    private String userName;

    private String userPhone;

    private int[] roleIds;
}
