package com.njupt.dzyh.domain.dto;

import com.alibaba.fastjson.JSONObject;
import com.njupt.dzyh.domain.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 一句话功能描述.
 * 项目名称:  生成Excel
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateExcel implements Serializable {
    List<Goods> goodsList;
    String outUrl;
    String fileName;

}
