package com.njupt.dzyh.domain.roles;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
@Accessors(chain = true)
public class UserInfo {
    @TableId(type = IdType.AUTO)
    private int id;

    private String userId;

    private String userPassword;

    private String userName;

    private String userPhone;

    private int roleId;

    //0待审核，1审核通过，-1审核不通过
    private int registStatus;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}











