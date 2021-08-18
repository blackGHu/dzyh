package com.njupt.dzyh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.MaterialList;
import com.njupt.dzyh.domain.MaterialListOrder;
import com.njupt.dzyh.domain.dto.GenerateMaterialListExcel;
import com.njupt.dzyh.domain.dto.GenerateMaterialListOrderExcel;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.otherFunctions.DownLoad;
import com.njupt.dzyh.service.InformationService;
import com.njupt.dzyh.service.MaterialListService;
import com.njupt.dzyh.utils.CommonResult;
import com.njupt.dzyh.utils.CommonUtil;
import com.njupt.dzyh.utils.ListToExcel;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 一句话功能描述.
 * 项目名称:  料单处理  ——  新建、料单订单处理
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:
 */
@RestController
@RequestMapping("/dzyh/materialList")
public class MaterialListApplyController {

    private final static Logger logger = LoggerFactory.getLogger(MaterialListApplyController.class);

    @Value("${resource}")
    private String resource;

    @Autowired
    private InformationService informationService;

    @Autowired
    private MaterialListService materialListService;



//     ---------插入操作--------------

    /**
     * 新建料单
     * @param materialList
     * @return
     */
    @RequestMapping("/insertMaterialList")
    //@RequiresPermissions("insertMaterialList")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult insertMaterialList(@RequestBody MaterialList materialList){

        return materialListService.insertMaterialList(materialList);

    }

    /**
     * 新建料单申请
     * @param materialListOrder
     * @return
     */
//  =============================================================

    /**
     * 新建料单申请
     * 传入：该申请的料单的  materialListId  + 申请数量  + 申请人
     *
     * 料单就是，每个学期开学，班长或者老师会发一个工具箱给你，里面装了这学期需要的所有实验器材。
     * 由于只要课程一样，需要的东西就一样，所以以班级为单位申请。
     * 任课老师根据课程需要创建料单，料单包含这门课这学期所有的器材。
     * 然后班长根据班级同学人数去申请料单，实验室直接配好，装工具箱里面批量发
     * @return
     */
    @RequestMapping("/materialListApply")
    //@RequiresPermissions("materialListApply")
    @RequiresRoles(value = {"教师","学生"}, logical = Logical.OR)
    public CommonResult materialListApply(@RequestBody MaterialListOrder materialListOrder){
//        MaterialList materialList= (MaterialList) materialListService
//                .selectBymaterialListId(materialListOrder.getMaterialListId()).getObj();
        String orderDescribes = materialListOrder.getOrderDescribe(); // 获取该料单中申请的物品对象str  以“；”为分割
        String[] materialListArr = orderDescribes.trim().split(";");
        System.out.println("materialListArr--------" + materialListArr.toString());
        int applyNumber = materialListOrder.getApplyNumber();
//        List<CommonResult> results = new ArrayList<>();
//        for(String tempMaterialList:materialListArr){
//            String[] temp = tempMaterialList.trim().split(" ");
//            System.out.println("temp----------" + temp.toString());
//            String name = temp[1];
//            String size = temp[2];
//            String model = temp[3];
//            int newNum = Integer.parseInt(temp[4].substring(0,1));
//            System.out.println("newNum-----" + newNum);
//            Goods goods = new Goods();
//            goods.setGoodsModel(model)
//                    .setGoodsName(name)
//                    .setGoodsSize(size);
//            int oldNum = informationService.getItem(goods);
//            System.out.println("oldNum-----" + oldNum);
//
//            if(oldNum > newNum * applyNumber){
//                informationService.subtract(goods);
//                results.add(CommonResult.error(CommonResultEm.ERROR,model + " " + size + "库存不足"));
//            }
//        }
//        if(0 != results.size() || null != results ){
//            return CommonResult.error(CommonResultEm.ERROR,"有库存不足，料单申请失败");
//        }
        List<Goods> goodsList = new ArrayList<>();
        for(String tempMaterialList:materialListArr){
//            applyNumber * 里面的数  然后进行封装Goods  然后进行subtract
//            成功则修改审核状态为待审核，插入料单申请表
            System.out.println("tempMaterialList----------" + tempMaterialList);

            String[] temp = tempMaterialList.trim().split(" ");
            String name = temp[3];
            String size = temp[2];
            String model = temp[1];
//            String address = temp[4];
            //int num = Integer.parseInt(temp[4].substring(0,1));?？？？思考思考，这么写能没有问题？就每次只能借个位数的数量？？
            int num = Integer.parseInt(temp[4]);
            Goods goods = new Goods();
            goods.setGoodsModel(model)
                    .setGoodsName(name)
                    .setGoodsSize(size)
                    .setGoodsNumbers(num*applyNumber)
                    .setBuyUserName(materialListOrder.getApplyUserName()+"(料单)");
//                    .setGoodsAddress(address);
            System.out.println(goods);
            System.out.println(materialListOrder.getApplyUserName());
            goodsList.add(goods);
            System.out.println("list<Goods>------" + goodsList);
            CommonResult tempResult1 = informationService.subtract(goods);
            System.out.println("tempResult1-------" + tempResult1);
            // 一旦有一个不足就全不给申请
            if(!tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                //回加
                System.out.println("list<Goods>------" + goodsList);
                goodsList.remove(goodsList.size()-1);
                for(Goods goods1:goodsList){
                    informationService.add(goods1);
                }
                return tempResult1;
            }
        }
        materialListOrder.setGoodsApprovalStatue(0);
        CommonResult tempResult2 = materialListService.insertMaterialListOrder(materialListOrder);
        if(tempResult2.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
            return CommonResult.success();
        }else
            return tempResult2;
    }

    /**
     * 料单申请审批
     */
    @RequestMapping("/doMaterialListOrderApprove")
    //@RequiresPermissions("doMaterialListOrderApprove")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult doMaterialListOrderApprove(@RequestParam("materialListOrderId") int materialListOrderId ,
                                            @RequestParam(value = "goodsApprovalStatue",required = false) Integer goodsApprovalStatus){

        CommonResult commonResult = materialListService.selectBymaterialListOrderId(materialListOrderId);
        MaterialListOrder materialListOrder = JSON.parseObject(JSONObject.toJSONString(commonResult.getObj()),MaterialListOrder.class);
        if(CommonUtil.isNull(materialListOrder)){
            return CommonResult.error();
        }
        if(CommonUtil.isNotNull(goodsApprovalStatus) && null != goodsApprovalStatus){
            materialListOrder.setGoodsApprovalStatue(goodsApprovalStatus);
        }

//        materialListOrder.setGoodsApprovalStatue(goodsApprovalStatus);

        //  更新料单申请表
        CommonResult result = materialListService.updateMaterialListOrder(materialListOrder);
        /**
         * 借用需要归还，有归还日期；领用和报废不需要归还，无归还日期。
         * 借用、领用和报废都需要将库存内的指定物品减去对应数量，归还需加上对应数量。
         */
        //System.out.println(result.getResultCode());
        //System.out.println(CommonResultEm.SUCCESS.getEcode());
        if(result.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {

            //System.out.println(goodsApprovalStatus);
            if (goodsApprovalStatus == 2) {
                /** 如果该料单被拒绝 则已扣的余量需要回加
                    1、
                 */

//                MaterialList materialList = (MaterialList) materialListService
//                        .selectBymaterialListId(materialListOrder.getMaterialListId()).getObj();
                String orderDescribes = materialListOrder.getOrderDescribe(); // 获取该料单中申请的物品对象str  以“；”为分割
                String[] materialListArr = orderDescribes.trim().split(";");
                int applyNumber = materialListOrder.getApplyNumber();
                //System.out.println("guo");
                for(String tempMaterialList:materialListArr){
//            applyNumber * 里面的数  然后进行封装Goods  然后进行subtract
//            成功则修改审核状态为待审核，插入料单申请表
                    String[] temp = tempMaterialList.trim().split(" ");
                    String name = temp[1];
                    String size = temp[2];
                    String model = temp[3];
//            String address = temp[4];

                    //int num = Integer.parseInt(temp[4].substring(0,1));
                    int num = Integer.parseInt(temp[4]);
                    Goods goods = new Goods();
                    goods.setGoodsModel(model)
                            .setGoodsName(name)
                            .setGoodsSize(size)
                            .setGoodsNumbers(num*applyNumber);
                    System.out.println(goods);
//                    .setGoodsAddress(address);
                    CommonResult tempResult1 = informationService.add(goods);
                    if(!tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                        return new CommonResult(CommonResultEm.ERROR.getEcode(),
                                tempResult1.getObj()+"回加出错",null);
                    }
                }
                return CommonResult.success();

            }
        }else{
            return CommonResult.error();
        }
        return CommonResult.success();
    }
//     ---------删除操作--------------

    /**
     * 删除新建料单
     * @param materialListId
     * @return
     */
    @RequestMapping("/deletByMaterialListId")
    //@RequiresPermissions("deletByMaterialListId")
    @RequiresRoles(value = {"超级管理员"}, logical = Logical.OR)
    public CommonResult deletByMaterialListId(@Param("materialListId") int materialListId ){
        return materialListService.deletByMaterialListId(materialListId);
    }

    /**
     * 删除料单s申请
     * 根据ID删除料单申请审批表的信息
     * 对于超管可以进行删除操作，如果原先是通过的 删除后要回加
     * @param materialListOrderId
     * @return
     */
    @RequestMapping("/deletByMaterialListOrderId")
    //@RequiresPermissions("deletByMaterialListOrderId")
    @RequiresRoles(value = {"超级管理员"}, logical = Logical.OR)
    public CommonResult deletByMaterialListOrderId(@Param("materialListOrderId") int materialListOrderId){
        MaterialListOrder materialListOrder = JSONObject
                .parseObject(JSONObject.toJSONString(materialListService
                        .selectBymaterialListOrderId(materialListOrderId).getObj()),MaterialListOrder.class);
        CommonResult tempResult1 = materialListService.deletByMaterialListOrderId(materialListOrderId);
        if(tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
            if (materialListOrder.getGoodsApprovalStatue() == 1) {
//                MaterialList materialList = (MaterialList) materialListService
//                        .selectBymaterialListId(materialListOrder.getMaterialListId()).getObj();
                String orderDescribes = materialListOrder.getOrderDescribe(); // 获取该料单中申请的物品对象str  以“；”为分割
                String[] materialListArr = orderDescribes.trim().split(";");
                int applyNumber = materialListOrder.getApplyNumber();
                for(String tempMaterialList:materialListArr){
//            applyNumber * 里面的数  然后进行封装Goods  然后进行subtract
//            成功则修改审核状态为待审核，插入料单申请表
                    String[] temp = tempMaterialList.trim().split(" ");
                    String name = temp[1];
                    String size = temp[2];
                    String model = temp[3];
//            String address = temp[4];
                    int num = Integer.parseInt(temp[4].substring(0,1));
                    Goods goods = new Goods();
                    goods.setGoodsModel(model)
                            .setGoodsName(name)
                            .setGoodsSize(size)
                            .setGoodsNumbers(num*applyNumber)
                            .setBuyUserName(materialListOrder.getApplyUserName());
//                    .setGoodsAddress(address);
                    CommonResult tempResult2 = informationService.add(goods);
                    if(!tempResult2.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                        return new CommonResult(CommonResultEm.ERROR.getEcode(),
                                tempResult2.getObj()+"回加出错",null);
                    }
                }
            }
        }else{
            return tempResult1;
        }
        return CommonResult.success();
    }




//     ---------更新操作--------------

    /**
     * 更新新建料单
     * @param materialList
     * @return
     */
    @RequestMapping("/updateMaterialList")
    //@RequiresPermissions("updateMaterialList")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult updateMaterialList(@RequestBody MaterialList materialList){
        return materialListService.updateMaterialList(materialList);
    }


    /**
     * 更新新建料单申请
     * @param materialListOrder
     * @return
     */
    @RequestMapping("/updateMaterialListOrder")
    //@RequiresPermissions("updateMaterialListOrder")
    public CommonResult updateMaterialListOrder(@RequestBody MaterialListOrder materialListOrder){
        return materialListService.updateMaterialListOrder(materialListOrder);
    }



//     ---------查询操作--------------
    /**
     * 查询所有物品
     * @return
     */
    @RequestMapping("/selectAllMaterialList")
    //@RequiresPermissions("selectAllMaterialList")
    public CommonResult selectAllMaterialList(){
        return materialListService.selectAllMaterialList();
    }

    /**
     * 分页查询所有
     * @param current  当前页码         默认1
     * @param size     每页显示数量     默认4
     * @return
     */
    @RequestMapping("/selectAllMaterialListByPage/{current}/{size}")
    //@RequiresPermissions("selectAllMaterialListByPage")
    @RequiresRoles(value = {"游客","教师","学生","管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult selectAllMaterialListByPage(@PathVariable("current") int current,
                                        @PathVariable("size") int size,
                                        @RequestBody(required = false) Map<String,Object> conditionsMap){
        return materialListService.selectAllMaterialListByPage(current,size,conditionsMap);

    }


    /**
     * 管理员分页查询所有
     * @param current  当前页码         默认1
     * @param size     每页显示数量     默认4
     * @return
     */
    @RequestMapping("/selectAllMaterialListOrderByPage/{current}/{size}")
    //@RequiresPermissions("selectAllMaterialListOrderByPage")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult selectAllMaterialListOrderByPage(@PathVariable("current") int current,
                                                    @PathVariable("size") int size,
                                                    @RequestBody(required = false) Map<String,Object> conditionsMap){
        return materialListService.selectAllMaterialListOrderByPage(current,size,conditionsMap);

    }

//    /**
//     * 教师学生分页查询所有
//     * @param current  当前页码         默认1
//     * @param size     每页显示数量     默认4
//     * @return
//     */
//    @RequestMapping("/selectAllMaterialListOrderPersonalByPage/{current}/{size}")
//    public CommonResult selectAllMaterialListOrderPersonalByPage(@PathVariable("current") int current,
//                                                         @PathVariable("size") int size,
//                                                         @RequestBody(required = false) Map<String,Object> conditionsMap){
//        return materialListService.selectAllMaterialListOrderPersonalByPage(current,size,conditionsMap);
//
//    }



//    /**
//     * 根据ID查询物品
//     * @param materialListId
//     * @return
//     */
//    @RequestMapping("/selectById")
//    public CommonResult selectById(@Param("materialListId") int materialListId){
//        return materialListService.selectBymaterialListId(materialListId);
//    }


//    @RequestMapping("/selectByConditions")
//    public CommonResult selectByConditions(@RequestBody Map<String,Object> conditionsMap){
//        return materialListService.selectByConditions(conditionsMap);
//    }



    //     ---------导出报表--------------
    @RequestMapping("/generateMaterialListExcel")
    //@RequiresPermissions("generateMaterialListExcel")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult generateMaterialListExcel(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                                  @Param("fileName") String fileName, HttpServletRequest request,
                                                  HttpServletResponse response) throws IOException {
        Object obj = materialListService.selectByMaterialListConditions(conditionsMap).getObj();
        if(null == obj){
            return CommonResult.error(CommonResultEm.ERROR,"记录为空,无需导出报表");
        }
        List<MaterialList> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(MaterialList.class);
        ListToExcel.materialListToExcel(resource,fileName,list);

        DownLoad.downloadFile(resource,fileName,request,response);
//        GenerateMaterialListExcel generateMaterialListExcel = new GenerateMaterialListExcel(list,outUrl,fileName);
//        return materialListService.generateMaterialListExcel(generateMaterialListExcel);
        return CommonResult.success();
    }


    //     ---------导出报表--------------
    @RequestMapping("/generateMaterialListOrderExcel")
    //@RequiresPermissions("generateMaterialListOrderExcel")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult generateMaterialListOrderExcel(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                                       @Param("fileName") String fileName, HttpServletRequest request,
                                                       HttpServletResponse response) throws IOException{
        Object obj = materialListService.selectByMaterialListOrderConditions(conditionsMap).getObj();
        if(null == obj){
            return CommonResult.error(CommonResultEm.ERROR,"记录为空,无需导出报表");
        }
        List<MaterialListOrder> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(MaterialListOrder.class);
        System.out.println("generateMaterialListOrderExcel  list-----" + list);
//        GenerateMaterialListOrderExcel generateMaterialListOrderExcel = new GenerateMaterialListOrderExcel(list,outUrl,fileName);
//        return materialListService.generateMaterialListOrderExcel(generateMaterialListOrderExcel);
        ListToExcel.materialListOrderToExcel(resource,fileName,list);

        DownLoad.downloadFile(resource,fileName,request,response);
        return CommonResult.success();
    }



}
