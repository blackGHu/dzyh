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

    int getItem(Goods goods);

    Information getInfomation(Goods goods);

    CommonResult updateInformation(Information information);

    CommonResult add(Goods goods);
    CommonResult subtract(Goods goods);
    CommonResult selectByCondition(Information con, int current, int size);
    CommonResult selectAllByCondition(Information con, int current, int size);
    List<Information> selectAllByCondition(Information information);


    CommonResult selectAllInfoByPage(int current, int size);
    CommonResult deleteInformationById(Information information);
}
