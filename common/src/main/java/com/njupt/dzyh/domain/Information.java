package com.njupt.dzyh.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   tjk
 * 创建时间:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_repertory")
public class Information implements Serializable {

    @TableId(value = "repertory_id",type = IdType.AUTO)
    private Integer repertoryId;

    private String repertoryType;

    private String repertoryName;

    private String repertorySize;

    private String repertoryModel;

    private Integer repertoryNumbers;

    private String repertoryAddress;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

}
