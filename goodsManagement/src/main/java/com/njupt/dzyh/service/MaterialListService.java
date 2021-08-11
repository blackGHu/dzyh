package com.njupt.dzyh.service;


import com.njupt.dzyh.domain.MaterialList;
import com.njupt.dzyh.domain.MaterialListOrder;
import com.njupt.dzyh.domain.dto.GenerateMaterialListExcel;
import com.njupt.dzyh.domain.dto.GenerateMaterialListOrderExcel;
import com.njupt.dzyh.utils.CommonResult;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface MaterialListService {
    CommonResult selectAllMaterialList();

    CommonResult selectBymaterialListId(int materialListId);
    CommonResult selectBymaterialListOrderId(int materialListOrderId);

    CommonResult insertMaterialList(MaterialList materialList);
    CommonResult insertMaterialListOrder(MaterialListOrder materialListOrder);

    CommonResult insertBatch(List<MaterialList> materialListList);

    CommonResult deletByMaterialListId(int materialListId);
    CommonResult deletByMaterialListOrderId(int materialListorderId);

    CommonResult updateMaterialList(MaterialList materialList);
    CommonResult updateMaterialListOrder(MaterialListOrder materialListOrder);

//    CommonResult updateBatch(List<MaterialList> materialListList);

    CommonResult selectByConditions(Map<String, Object> map);

    CommonResult selectAllMaterialListByPage(int current, int size, Map<String, Object> conditionsMap);
    CommonResult selectAllMaterialListOrderByPage(int current, int size, Map<String, Object> conditionsMap);


    CommonResult generateMaterialListExcel(GenerateMaterialListExcel generateMaterialListExcel) throws IOException, ParseException;
    CommonResult generateMaterialListOrderExcel(GenerateMaterialListOrderExcel generateMaterialListOrderExcel) throws IOException, ParseException;

}
