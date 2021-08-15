package com.njupt.dzyh.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.njupt.dzyh.utils.concasts.CommonConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_goods_apply")
@Accessors(chain = true)
public class GoodsApply implements Serializable {

    @TableId(value = "order_id",type = IdType.AUTO)
    private Integer orderId;

    private String repertoryName;

    private String repertorySize;

    private String repertoryModel;

    private Integer repertoryNumbers;

    private String repertoryAddress;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date borrowTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    private String categoryName;

    private String purposeName;



    // 申请人ID —— user_name
    private String applyUserName;

    // 审批人ID —— user_name
    private String approvalUserName;




    //申请审核状态   默认 -1 未申请 0 待审核 1 通过 2 拒绝
    private Integer goodsApprovalStatus;

    //使用状态    默认 -1  未使用  0 借用 1 领用 2 归还 3 报废
    private Integer goodsUseStatus;


    @TableField(fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
