package com.njupt.dzyh.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 一句话功能描述.
 * 项目名称:  物品实体类
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_goods")
@Accessors(chain = true)
public class Goods implements Serializable {

    @TableId("goods_id")
    private Integer goodsId;

    private String goodsName;

    private String goodsSize;

    private String goodsModel;

    private Double goodsPrice;

    private Integer goodsNumbers;

    private String goodsAddress;

    private Integer categoryId;

    private Integer purposeId;

    private String userId;

    private Integer goodsApprovalStatus;

    private Integer goodsUseStatus;

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
