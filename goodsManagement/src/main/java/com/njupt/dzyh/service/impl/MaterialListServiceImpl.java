package com.njupt.dzyh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njupt.dzyh.dao.MaterialListDao;
import com.njupt.dzyh.dao.MaterialListOrderDao;
import com.njupt.dzyh.domain.MaterialList;
import com.njupt.dzyh.domain.MaterialListOrder;
import com.njupt.dzyh.domain.dto.GenerateMaterialListExcel;
import com.njupt.dzyh.domain.dto.GenerateMaterialListOrderExcel;
import com.njupt.dzyh.domain.dto.MaterialListDto;
import com.njupt.dzyh.domain.dto.MaterialListOrderDto;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.MaterialListService;
import com.njupt.dzyh.utils.CommonResult;
import com.njupt.dzyh.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:
 *
 * 注意：ID设置为自增还没有进行操作
 */
@Service
public class  MaterialListServiceImpl extends ServiceImpl<MaterialListDao, MaterialList> implements MaterialListService {

    @Autowired
    private MaterialListDao materialListDao;

    @Autowired
    private MaterialListOrderDao materialListOrderDao;






//     ---------查询操作--------------
    /**
     * 查询所有审批订单
     * @return
     */
    public CommonResult selectAllMaterialList() {
        System.out.println("hello--- ");
        List<MaterialList> goodsList =  materialListDao.selectList(null);
        if(0 == goodsList.size()){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(goodsList);
    }

    /**
     * 根据ID查询物品
     * @param materialListId
     * @return
     */
    public CommonResult selectBymaterialListId(int materialListId) {
        MaterialList materialList = materialListDao.selectById(materialListId);
        if(CommonUtil.isNull(materialList)){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(materialList);
    }

    @Override
    public CommonResult selectBymaterialListOrderId(int materialListOrderId) {
        MaterialListOrder materialListOrder = materialListOrderDao.selectById(materialListOrderId);
        if(CommonUtil.isNull(materialListOrder)){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(materialListOrder);
    }


    /**
     * 分页查询所有新建料单
     * @param current  当前页码         默认1
     * @param size     每页显示数量     默认4
     * @return
     */
    public CommonResult selectAllMaterialListByPage(int current,int size,Map<String,Object> conditionsMap) {
        System.out.println("分页查询 begin");
        if(current<=0 || CommonUtil.isNull(current)){
            current = 1;
        }
        if(size<=0 || CommonUtil.isNull(size)){
            size = 4;
        }
        Page<MaterialList> page = new Page<>(current,size);
        QueryWrapper<MaterialList> wrapper = new QueryWrapper<>();
        if(null == conditionsMap){
            materialListDao.selectPage(page,null);
        }else {
            if(conditionsMap.containsKey("collegeId")){
                wrapper.eq("college_id",conditionsMap.get("collegeId"));
                conditionsMap.remove("collegeId");
            }
            if(conditionsMap.containsKey("majorId")){
                wrapper.eq("major_id",conditionsMap.get("majorId"));
                conditionsMap.remove("majorId");
            }
            for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (CommonUtil.isNotNull(value)) {
                    wrapper.like(CommonUtil.camel2Underline(key), value);
                }
            }
            materialListDao.selectPage(page, wrapper);
        }
        List<MaterialList> materialLists = page.getRecords();


        System.out.println("分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==materialLists.size()){
            return CommonResult.error();
        }
        return CommonResult.success(new MaterialListDto(total,materialLists));
    }


    /**
     * 分页查询所有料单申请
     * @param current  当前页码         默认1
     * @param size     每页显示数量     默认4
     * @return
     */
    public CommonResult selectAllMaterialListOrderByPage(int current,int size,Map<String,Object> conditionsMap) {
        System.out.println("分页查询 begin");
        if(current<=0 || CommonUtil.isNull(current)){
            current = 1;
        }
        if(size<=0 || CommonUtil.isNull(size)){
            size = 4;
        }
        Page<MaterialListOrder> page = new Page<>(current,size);
        QueryWrapper<MaterialListOrder> wrapper = new QueryWrapper<>();
        if(null == conditionsMap){
            materialListOrderDao.selectPage(page,null);
        }else {
            if(conditionsMap.containsKey("collegeName")){
                wrapper.eq("college_name",conditionsMap.get("collegeName"));
                conditionsMap.remove("collegeName");
            }
            if(conditionsMap.containsKey("majorName")){
                wrapper.eq("major_name",conditionsMap.get("majorName"));
                conditionsMap.remove("majorName");
            }
            for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (CommonUtil.isNotNull(value)) {
                    wrapper.like(CommonUtil.camel2Underline(key), value);
                }
            }
            materialListOrderDao.selectPage(page,wrapper);
        }

        List<MaterialListOrder> materialListOrders = page.getRecords();
        // 根据materialListID查询materialList对象获取其学院等信息后封装
//        List<MaterialListOrderDto> materialListOrderDtos = new ArrayList<>();
//        for(MaterialListOrder materialListOrder:materialListOrders){
//            MaterialList materialList = materialListDao.selectById(materialListOrder.getMaterialListId());
//            MaterialListOrderDto materialListOrderDto = new MaterialListOrderDto();
//            materialListOrderDto.setApplyNumber(materialListOrder.getApplyNumber())
//                    .setApplyUserName(materialListOrder.getApplyUserName())
//                    .setClassId(materialList.getClassId())
//                    .setCollegeId(materialList.getCollegeId())
//                    .setCourseId(materialList.getCourseId())
//                    .setGoodsApprovalStatue(materialListOrder.getGoodsApprovalStatue())
//                    .setTeacherName(materialList.getTeacherName())
//                    .setOrderDescribe(materialList.getOrderDescribe())
//                    .setMajorId(materialList.getMajorId());
//
//            materialListOrderDtos.add(materialListOrderDto);
//        }

        System.out.println("分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==materialListOrders.size()){
            return CommonResult.error();
        }
        return CommonResult.success(new MaterialListOrderDto(total,materialListOrders));
    }


//    public CommonResult selectByConditions(Map<String, Object> map) {
//        List<MaterialList> result = materialListDao.selectByMap(map);
//        if(0 == result.size()){
//            return CommonResult.error();
//        }
//        return CommonResult.success(result);
//    }


    public CommonResult selectByMaterialListConditions(Map<String, Object> conditionsMap) {
        System.out.println("条件查询 begin");
        List<MaterialList> list = null;
        QueryWrapper<MaterialList> wrapper = new QueryWrapper<>();
        //没有条件
        if(null == conditionsMap || 0 == conditionsMap.size()){
            list = materialListDao.selectList(null);
        }else {
            // 有条件
            if(conditionsMap.containsKey("collegeId")){
                wrapper.eq("college_id",conditionsMap.get("collegeId"));
                conditionsMap.remove("collegeId");
            }
            if(conditionsMap.containsKey("majorId")){
                wrapper.eq("major_id",conditionsMap.get("majorId"));
                conditionsMap.remove("majorId");
            }
            for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (CommonUtil.isNotNull(value)) {
                    wrapper.like(CommonUtil.camel2Underline(key), value);
                }
            }
            list = materialListDao.selectList(wrapper);
        }
        if(0 == list.size() || null == list){
            return CommonResult.error();
        }else {
            return CommonResult.success(list);
        }
    }

    public CommonResult selectByMaterialListOrderConditions(Map<String, Object> conditionsMap) {
        System.out.println("条件查询 begin");
        List<MaterialListOrder> list = null;
        QueryWrapper<MaterialListOrder> wrapper = new QueryWrapper<>();
        //没有条件
        if(null == conditionsMap || 0 == conditionsMap.size()){
            list = materialListOrderDao.selectList(null);
        }else {
            // 有条件
            if(conditionsMap.containsKey("collegeName")){
                wrapper.eq("college_name",conditionsMap.get("collegeName"));
                conditionsMap.remove("collegeName");
            }
            if(conditionsMap.containsKey("majorName")){
                wrapper.eq("major_name",conditionsMap.get("majorName"));
                conditionsMap.remove("majorName");
            }
            for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (CommonUtil.isNotNull(value)) {
                    wrapper.like(CommonUtil.camel2Underline(key), value);
                }
            }
            list = materialListOrderDao.selectList(wrapper);
        }
        if(0 == list.size() || null == list){
            return CommonResult.error();
        }else {
            return CommonResult.success(list);
        }
    }


//     ---------插入操作--------------

    /**
     * 新建料单
     * @param materialList
     * @return
     */
    public CommonResult insertMaterialList(MaterialList materialList) {

        if(CommonUtil.isNull(materialList)){
            return CommonResult.error();
        }else if(CommonUtil.isNotNull(materialListDao.selectById(materialList.getMaterialListId()))){
            return CommonResult.error(CommonResultEm.ALREADY_EXIST);
        }else{
            MaterialList newMaterialList = new MaterialList();
            //  暂定 自己编写
            newMaterialList.setCollegeName(materialList.getCollegeName())
                    .setMajorName(materialList.getMajorName())
                    .setCourseName(materialList.getCourseName())
                    .setClassName(materialList.getClassName())
                    .setOrderDescribe(materialList.getOrderDescribe())
                    .setTeacherName(materialList.getTeacherName());
            int rec = materialListDao.insert(newMaterialList);
            if (rec == 1)
                return CommonResult.success();
            else
                return CommonResult.error();
        }
    }

    /**
     * 新建料单申请
     * @param materialListOrder
     * @return
     */
    public CommonResult insertMaterialListOrder(MaterialListOrder materialListOrder) {

        if(CommonUtil.isNull(materialListOrder)){
            return CommonResult.error();
        }else{
            MaterialListOrder newMaterialListOrder = new MaterialListOrder();
            //  暂定 自己编写
            newMaterialListOrder.setApplyNumber(materialListOrder.getApplyNumber())
                    .setGoodsApprovalStatue(materialListOrder.getGoodsApprovalStatue())
                    .setApplyUserName(materialListOrder.getApplyUserName())
                    .setCollegeName(materialListOrder.getCollegeName())
                    .setMajorName(materialListOrder.getMajorName())
                    .setCourseName(materialListOrder.getCourseName())
                    .setClassName(materialListOrder.getClassName())
                    .setOrderDescribe(materialListOrder.getOrderDescribe())
                    .setTeacherName(materialListOrder.getTeacherName());
            int rec = materialListOrderDao.insert(newMaterialListOrder);
            if (rec == 1)
                return CommonResult.success();
            else
                return CommonResult.error();
        }
    }


    /**
     * 批量插入
     * @param materialListApplyList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult insertBatch(List<MaterialList> materialListApplyList){
        if(0 == materialListApplyList.size()){
            return CommonResult.error();
        }
        Integer rec = materialListDao.insertBatchSomeColumn(materialListApplyList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }



//     ---------删除操作--------------

    /**
     * 根据ID删除料单的信息
     * @param materialListId
     * @return
     */
    public CommonResult deletByMaterialListId(int materialListId) {
        MaterialList goods = materialListDao.selectById(materialListId);
        if(CommonUtil.isNull(goods)){
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }
        int rec = materialListDao.deleteById(materialListId);
        if(rec == 1){
            return CommonResult.success();
        }else
            return CommonResult.error();
    }



    /**
     * 根据ID删除料单申请审批表的信息
     * 对于超管可以进行删除操作，如果原先是通过的 删除后要回加
     * @param materialListorderId
     * @return
     */
    public CommonResult deletByMaterialListOrderId(int materialListorderId) {
        MaterialListOrder materialListOrder = materialListOrderDao.selectById(materialListorderId);
        if(CommonUtil.isNull(materialListOrder)){
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }
        int rec = materialListOrderDao.deleteById(materialListorderId);
        if(rec == 1){
            return CommonResult.success();
        }else
            return CommonResult.error();
    }



//     ---------更新操作--------------

    /**
     * 更新料单新建
     * @param materialList
     * @return
     */
    public CommonResult updateMaterialList(MaterialList materialList) {
        if (CommonUtil.isNull(materialList)) {
            return CommonResult.error();
        } else{
            MaterialList newMaterialList = materialListDao.selectById(materialList.getMaterialListId());
            if (CommonUtil.isNull(newMaterialList)) {
                return CommonResult.error(CommonResultEm.NOT_EXIST);
            } else {
                //  暂定 自己编写
                newMaterialList.setMaterialListId(materialList.getMaterialListId())
                        .setCollegeName(materialList.getCollegeName())
                        .setMajorName(materialList.getMajorName())
                        .setCourseName(materialList.getCourseName())
                        .setClassName(materialList.getClassName())
                        .setOrderDescribe(materialList.getOrderDescribe())
                        .setTeacherName(materialList.getTeacherName());
                int rec = materialListDao.updateById(newMaterialList);
                if (rec == 1)
                    return CommonResult.success();
                else
                    return CommonResult.error();
            }

        }
    }


    /**
     * 更新料单审批
     * @param materialListOrder
     * @return
     */
    public CommonResult updateMaterialListOrder(MaterialListOrder materialListOrder) {

        if (CommonUtil.isNull(materialListOrder)) {
            return CommonResult.error();
        } else{
            MaterialListOrder newMaterialListOrder = materialListOrderDao.selectById(materialListOrder.getMaterialListOrderId());
            if (CommonUtil.isNull(newMaterialListOrder)) {
                return CommonResult.error(CommonResultEm.NOT_EXIST);
            } else {
                //  暂定 自己编写
                newMaterialListOrder.setMaterialListOrderId(materialListOrder.getMaterialListOrderId())
                                    .setApplyNumber(materialListOrder.getApplyNumber())
                                    .setGoodsApprovalStatue(materialListOrder.getGoodsApprovalStatue())
                                    .setApplyUserName(materialListOrder.getApplyUserName())
                                    .setCollegeName(materialListOrder.getCollegeName())
                                    .setMajorName(materialListOrder.getMajorName())
                                    .setCourseName(materialListOrder.getCourseName())
                                    .setClassName(materialListOrder.getClassName())
                                    .setOrderDescribe(materialListOrder.getOrderDescribe())
                                    .setTeacherName(materialListOrder.getTeacherName());
                int rec = materialListOrderDao.updateById(newMaterialListOrder);
                if (rec == 1)
                    return CommonResult.success();
                else
                    return CommonResult.error();
            }

        }
    }

//    /**
//     * 批量更新
//     * @param materialListApplyList
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public CommonResult updateBatch(List<MaterialList> materialListApplyList){
//        if(0 == materialListApplyList.size()){
//            return CommonResult.error();
//        }
//        Integer rec = materialListDao.updateBatchSomeColumn(materialListApplyList);
//        if(rec == 0)
//            return CommonResult.error();
//        else
//            return CommonResult.success();
//    }


//     ---------导出报表--------------

    /**
     * 导出EXCEL报表   xsc
     * @param generateMaterialListExcel
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult generateMaterialListExcel(GenerateMaterialListExcel generateMaterialListExcel) throws IOException, ParseException {
        // 1、首先根据查询查出所需信息（查询全部、条件查询）
        // 2、然后将结果封装成List<T>后 再封装成GenerateExcel实体
        // 3、调用service.generateExcel（。。。）
            List<MaterialList> goodsList = generateMaterialListExcel.getMaterialLists();
            System.out.println("[generateExcel]  goodsList---\t" + goodsList);
            // goodsList 中的 createTime 和 updateTime 正常，后面转成JSON就不正常了
            // 需添加注解 @JSONField(format = "yyyy-MM-dd HH:mm:ss")  或者 将数据转换成json对象时，使用JSON.toJSON
            if (0 == goodsList.size()) {
                return CommonResult.error();
            }
            JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(goodsList));
            System.out.println("[generateExcel]  jsonArray---\t" + jsonArray.toJSONString());

            //        "../excelTest/","test.xls"
            CommonUtil.JsonToExcel(jsonArray, generateMaterialListExcel.getOutUrl(), generateMaterialListExcel.getFileName());
            return CommonResult.success();
    }

    /**
     * 导出EXCEL报表   xsc
     * @param generateMaterialListOrderExcel
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult generateMaterialListOrderExcel(GenerateMaterialListOrderExcel generateMaterialListOrderExcel) throws IOException, ParseException {
        // 1、首先根据查询查出所需信息（查询全部、条件查询）
        // 2、然后将结果封装成List<T>后 再封装成GenerateExcel实体
        // 3、调用service.generateExcel（。。。）
        List<MaterialListOrder> goodsList = generateMaterialListOrderExcel.getMaterialListOrders();
        System.out.println("[generateExcel]  goodsList---\t" + goodsList);
        // goodsList 中的 createTime 和 updateTime 正常，后面转成JSON就不正常了
        // 需添加注解 @JSONField(format = "yyyy-MM-dd HH:mm:ss")  或者 将数据转换成json对象时，使用JSON.toJSON
        if (0 == goodsList.size()) {
            return CommonResult.error();
        }
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(goodsList));
        System.out.println("[generateExcel]  jsonArray---\t" + jsonArray.toJSONString());

        //        "../excelTest/","test.xls"
        CommonUtil.JsonToExcel(jsonArray, generateMaterialListOrderExcel.getOutUrl(), generateMaterialListOrderExcel.getFileName());
        return CommonResult.success();
    }



}
