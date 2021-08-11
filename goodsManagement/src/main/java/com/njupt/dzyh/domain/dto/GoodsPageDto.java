package com.njupt.dzyh.domain.dto;

import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.MaterialList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
@Accessors(chain = true)
public class GoodsPageDto implements Serializable {

    private Long total;

    private List<Goods> goodsList;




}