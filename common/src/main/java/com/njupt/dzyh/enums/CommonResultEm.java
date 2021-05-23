package com.njupt.dzyh.enums;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum CommonResultEm {
    // 通用enum：
    SUCCESS      ("200","成功"),
    ERROR        ("400","失败"),
    BIND_ERROR   ("111103","参数校验异常");

    // 用户登录enum：900xx
    // 管理员...：800xx
    // 订单：700xx

    private String ecode;

    private String emsg;

    public String getEcode() {
        return ecode;
    }

    public String getEmsg() {
        return emsg;
    }

    public static CommonResultEm statOf(String ecode) {
        for (CommonResultEm state : values())
            if (state.getEcode().equals(ecode))
                return state;
        return null;
    }
}
