package com.njupt.dzyh.service;

import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.Information;
import com.njupt.dzyh.utils.CommonResult;

import java.util.List;

/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   tjk
 * 创建时间:
 */
public interface InformationService {

    CommonResult add(Goods goods);
    CommonResult subtract(Goods goods);
    CommonResult selectByCondition(Information con,int current,int size);
    CommonResult selectAllInfoByPage(int current,int size);
}
