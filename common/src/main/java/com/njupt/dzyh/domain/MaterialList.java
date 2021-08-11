package com.njupt.dzyh.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 一句话功能描述.
 * 项目名称:   料单新建
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_material_list")
@Accessors(chain = true)
public class MaterialList implements Serializable {

    @TableId(value = "material_list_id",type = IdType.AUTO)
    private Integer materialListId;

    private String collegeName;

    private String majorName;

    private String courseName;

    private String className;

    //记录List<GoodsApply>
    private String orderDescribe;


    private String teacherName;



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
