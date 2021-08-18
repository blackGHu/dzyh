package com.njupt.dzyh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.GoodsApply;
import com.njupt.dzyh.domain.dto.GenerateExcel;
import com.njupt.dzyh.domain.dto.GenerateGoodsApplyExcel;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.otherFunctions.DownLoad;
import com.njupt.dzyh.service.GoodsApplyService;
import com.njupt.dzyh.service.InformationService;
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
import java.io.*;
import java.text.ParseException;
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
 */
@RestController
@RequestMapping("/dzyh/goodsApply")
public class GoodsApplyController {

    private final static Logger logger = LoggerFactory.getLogger(GoodsApplyController.class);

    @Value("${resource}")
    private String resource;

    @Autowired
    private InformationService informationService;

    @Autowired
    private GoodsApplyService goodsApplyService;

//     ---------查询操作--------------
    /**
     * 查询所有物品
     * @return
     */
    @RequestMapping("/selectAll")
    public CommonResult selectAll(){
        return goodsApplyService.selectAll();
    }

    /**
     * 管理员分页查询所有
     * @param current  当前页码         默认1
     * @param size     每页显示数量     默认4
     * @return
     */
    @RequestMapping("/selectAllByPage/{current}/{size}")
    //@RequiresPermissions("selectAllGoodsApplyByPage")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult selectAllByPage(@PathVariable("current") int current,
                                        @PathVariable("size") int size,
                                        @RequestBody(required = false) Map<String,Object> conditionsMap){
       return goodsApplyService.selectByPage(current,size,conditionsMap);

    }



    /**
     * 教师学生分页查询所有
     * @param current  当前页码         默认1
     * @param size     每页显示数量     默认4
     * @return
     */
    @RequestMapping("/selectPersonalByPage/{current}/{size}")
    //@RequiresPermissions("selectPersonalByPage")
    @RequiresRoles(value = {"教师","学生"}, logical = Logical.OR)
    public CommonResult selectPersonalByPage(@PathVariable("current") int current,
                                        @PathVariable("size") int size,
                                        @RequestBody(required = false) Map<String,Object> conditionsMap) {
        return goodsApplyService.selectPersonalByPage(current, size, conditionsMap);
    }

    /**
     * 根据ID查询物品
     * @param orderId
     * @return
     */
    @RequestMapping("/selectById")
    public CommonResult selectById(@Param("orderId") int orderId){
        return goodsApplyService.selectById(orderId);
    }


    @RequestMapping("/selectByConditions")
    public CommonResult selectByConditions(@RequestBody(required = false) Map<String,Object> conditionsMap){
        return goodsApplyService.selectByConditions(conditionsMap);
    }





    /**
     * 根据ID删除物品申请审批表的信息
     * 对于超管可以进行删除操作，如果原先是通过的 删除后要回加
     * @param orderId
     * @return
     */
    @RequestMapping("/deleteByGoodsApplyId")
    //@RequiresPermissions("deleteByGoodsApplyId")
    @RequiresRoles(value = {"超级管理员"}, logical = Logical.OR)
    public CommonResult deleteByGoodsApplyId(@Param("orderId") int orderId){
        GoodsApply goodsApply = JSONObject
                .parseObject(JSONObject.toJSONString(goodsApplyService.selectById(orderId).getObj()),GoodsApply.class);
        CommonResult tempResult1 = goodsApplyService.deletById(orderId);
        CommonResult result = new CommonResult();
        if(tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
            if (goodsApply.getGoodsApprovalStatus() == 1) {
                Goods goods = new Goods();
                goods.setGoodsNumbers(goodsApply.getRepertoryNumbers())
                        .setGoodsModel(goodsApply.getRepertoryModel())
                        .setGoodsSize(goodsApply.getRepertorySize())
                        .setGoodsName(goodsApply.getRepertoryName())
                        .setGoodsAddress(goodsApply.getRepertoryAddress())
                        .setPurposeName(goodsApply.getPurposeName())
                        .setCategoryName(goodsApply.getCategoryName());
                CommonResult tempResult2 = informationService.add(goods);
                if(tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                    result.setResultCode(tempResult2.getResultCode());
                    result.setResultMessage(tempResult2.getResultMessage());
                }else {
                    return tempResult2;
                }
            }
        }else{
            return tempResult1;
        }
        return result;
    }




//     ---------更新操作--------------

    /**
     * 更新物品
     * @param goodsApply
     * @return
     */
    @RequestMapping("/update")
    //@RequiresPermissions("updateGoodsApply")
    public CommonResult update(@RequestBody GoodsApply goodsApply){
        return goodsApplyService.update(goodsApply);
    }

    @RequestMapping("/updateBatch")
    //@RequiresPermissions("updateGoodsApplyBatch")
    public CommonResult updateBatch(@RequestBody List<GoodsApply> goodsApplyList){
        return goodsApplyService.updateBatch(goodsApplyList);
    }





//     ---------导出报表--------------
    @RequestMapping("/generateExcel")
    //@RequiresPermissions("goodsApplyFile")
    public CommonResult generateExcel(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                      @Param("fileName") String fileName,HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        Object obj = goodsApplyService.selectByConditions(conditionsMap).getObj();
        if(null == obj){
            return CommonResult.error(CommonResultEm.ERROR,"记录为空,无需导出报表");
        }
        List<GoodsApply> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(GoodsApply.class);
        ListToExcel.goodsApplyToExcel(resource,fileName,list);

        DownLoad.downloadFile(resource,fileName,request,response);
//        GenerateGoodsApplyExcel generateGoodsApplyExcel = new GenerateGoodsApplyExcel(list,outUrl,fileName);
//        return goodsApplyService.generateExcel(generateGoodsApplyExcel);
        return CommonResult.success();
    }

//    ----------模板下载------------------
    @RequestMapping("/downLoadGoodsApplyTemplate")
    //@RequiresPermissions("downLoadApplyTemplate")
    public void downLoadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) throws UnsupportedEncodingException {
        String fileName = "goodsApplyTemplate.xls";
        DownLoad.downloadFile(resource,fileName,request,response);
    }


//  =============================================================



    /**
     * 物品申请操作   申请人 为当前申请的用户名称
     * 物品申请就是，比如你电阻坏了，或者要竞赛，只需要个别几个物品，以个人为单位单独申请一个或者几个物品
     * @return
     */
    @RequestMapping("/goodsApply")
    //@RequiresPermissions("goodsApply")
    @RequiresRoles(value = {"教师","学生"}, logical = Logical.OR)
    public CommonResult goodsApply(@RequestBody GoodsApply goodsApply){
        /**
         * 订单表是否需要？
         1、物品申请 传入申请的物品信息  Information + 申请人（）
         2、根据传入的信息
            1、该物品是否存在
            2、申请的数量是否超过余量
                1、成功
                2、失败    库存不足记录添加
            3、申请成功后进行审批操作

         */
//        int applyNum = goodsApply.getRepertoryNumbers();
//        Information tempInfo = new Information();
//        tempInfo.setRepertoryName(goodsApply.getRepertoryName())
//                .setRepertorySize(goodsApply.getRepertorySize())
//                .setRepertoryModel(goodsApply.getRepertoryModel());
//        Information goodsInfo = (Information) informationService.selectByCondition(tempInfo).getObj();
//        int leftNum = goodsInfo.getRepertoryNumbers();
//        if(applyNum>leftNum){
//            // 记录到库存不足中  ——  未写
//            return new CommonResult(CommonResultEm.NUMBER_IS_NOT_ENOUGH.getEcode(),
//                    CommonResultEm.NUMBER_IS_NOT_ENOUGH.getEmsg(),null);
//        }else{
            //  设置成待审核状态
            goodsApply.setGoodsApprovalStatus(0);
            Goods goods = new Goods();
            goods.setGoodsName(goodsApply.getRepertoryName())
                    .setGoodsAddress(goodsApply.getRepertoryAddress())
                    .setGoodsSize(goodsApply.getRepertorySize())
                    .setGoodsNumbers(goodsApply.getRepertoryNumbers())
                    .setGoodsModel(goodsApply.getRepertoryModel())
                    .setPurposeName(goodsApply.getPurposeName())
                    .setCategoryName(goodsApply.getCategoryName())
                    .setBuyUserName(goodsApply.getApplyUserName());


        // 按照规格和型号去查询记录表
            // 申请的时候先扣除余量
            CommonResult tempResult1 = informationService.subtract(goods);
            if(tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                CommonResult tempResult2 = goodsApplyService.insert(goodsApply);
                if(tempResult2.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
                    return CommonResult.success();
                }else{
                    return CommonResult.error();
                }
            }else {
                return tempResult1;
            }
    }


    /**
     * 物品审批操作 （二次审批）   第一次 通过、拒绝

     * @param orderId
     * @param approvalUserName
     * @param goodsApprovalStatus
     * @param goodsUseStatus
     * @return
     */
    @RequestMapping("/doNormalGoodsApplyApprove")
    //@RequiresPermissions("doNormalGoodsApplyApprove")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult doNormalGoodsApplyApprove(@RequestParam(value = "orderId") int orderId,
                                  @RequestParam(value = "approvalUserName",required = false) String approvalUserName,
                                  @RequestParam(value = "goodsApprovalStatus") Integer goodsApprovalStatus,
                                  @RequestParam(value = "goodsUseStatus",required = false) Integer goodsUseStatus){
        // 前台传给后台该记录封装的goods对象
        // 并传当前登录用户名赋值给approvaleName
        // 以及   goodsApprovalStatus  物品申请审核状态：-1 未申请 0——待审核（默认）1——通过  2——拒绝
        // goodsUseStatus   物品使用状态：-1 未使用  0——借用 1——领用 2——归还 3——报废
        // 进行更新操作
        CommonResult commonResult = goodsApplyService.selectById(orderId);
        GoodsApply goodsApply = JSON.parseObject(JSONObject.toJSONString(commonResult.getObj()),GoodsApply.class);
        if(CommonUtil.isNull(goodsApply)){
            return CommonResult.error();
        }
        if(StringUtils.isNotBlank(approvalUserName)){
            goodsApply.setApprovalUserName(approvalUserName);
        }
        if(CommonUtil.isNotNull(goodsApprovalStatus) && null != goodsApprovalStatus){
            goodsApply.setGoodsApprovalStatus(goodsApprovalStatus);
        }
        if(CommonUtil.isNotNull(goodsUseStatus) && null != goodsUseStatus){
            goodsApply.setGoodsUseStatus(goodsUseStatus);
        }
        CommonResult result = goodsApplyService.update(goodsApply);
        /**
         * 借用需要归还，有归还日期；领用和报废不需要归还，无归还日期。
         * 借用、领用和报废都需要将库存内的指定物品减去对应数量，归还需加上对应数量。
         */
        if(result.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
            if (goodsApprovalStatus == 2) {
                Goods goods = new Goods();
                goods.setGoodsName(goodsApply.getRepertoryName())
                        .setGoodsSize(goodsApply.getRepertorySize())
                        .setGoodsNumbers(goodsApply.getRepertoryNumbers())
                        .setGoodsModel(goodsApply.getRepertoryModel())
                        .setGoodsAddress(goodsApply.getRepertoryAddress())
                        .setPurposeName(goodsApply.getPurposeName())
                        .setCategoryName(goodsApply.getCategoryName());

                return informationService.add(goods);
            }
        }else{
            return CommonResult.error();
        }
        return CommonResult.success("存放地址为："+goodsApply.getRepertoryAddress());
    }

    /**
     * 物品审批操作 （二次审批）  归还、报废

     * @param orderId
     * @param approvalUserName
     * @param goodsApprovalStatus
     * @param goodsUseStatus
     * @return
     */
    @RequestMapping("/doSpecialGoodsApplyApprove")
    //@RequiresPermissions("doSpecialGoodsApplyApprove")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult doSpecialGoodsApplyApprove(@RequestParam(value = "orderId") int orderId,
                                                   @RequestParam(value = "approvalUserName",required = false) String approvalUserName,
                                                   @RequestParam(value = "goodsApprovalStatus",required = false) Integer goodsApprovalStatus,
                                                   @RequestParam(value = "goodsUseStatus") Integer goodsUseStatus){
        // 前台传给后台该记录封装的goods对象
        // 并传当前登录用户名赋值给approvaleName
        // 以及   goodsApprovalStatus  物品申请审核状态：-1 未申请 0——待审核（默认）1——通过  2——拒绝
        // goodsUseStatus   物品使用状态：-1 未使用  0——借用 1——领用 2——归还 3——报废
        // 进行更新操作
        CommonResult commonResult = goodsApplyService.selectById(orderId);
        GoodsApply goodsApply = JSON.parseObject(JSONObject.toJSONString(commonResult.getObj()),GoodsApply.class);
        if(CommonUtil.isNull(goodsApply)){
            return CommonResult.error();
        }
        if(StringUtils.isNotBlank(approvalUserName)){
            goodsApply.setApprovalUserName(approvalUserName);
        }
        if(CommonUtil.isNotNull(goodsApprovalStatus) && null != goodsApprovalStatus){
            goodsApply.setGoodsApprovalStatus(goodsApprovalStatus);
        }
        if(CommonUtil.isNotNull(goodsUseStatus) && null != goodsUseStatus){
            goodsApply.setGoodsUseStatus(goodsUseStatus);
        }
//
//        goodsApply.setApprovalUserName(approvalUserName)
//                .setGoodsApprovalStatus(goodsApprovalStatus)
//                .setGoodsUseStatus(goodsUseStatus);
        CommonResult result = goodsApplyService.update(goodsApply);
        /**
         * 借用需要归还，有归还日期；领用和报废不需要归还，无归还日期。
         * 借用、领用和报废都需要将库存内的指定物品减去对应数量，归还需加上对应数量。
         */
        if(result.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
            if (goodsApply.getGoodsApprovalStatus() == 1) {
                if(goodsUseStatus == 2) { // 如果是归还 需要回加
                    Goods goods = new Goods();
                    goods.setGoodsName(goodsApply.getRepertoryName())
                            .setGoodsSize(goodsApply.getRepertorySize())
                            .setGoodsNumbers(goodsApply.getRepertoryNumbers())
                            .setGoodsModel(goodsApply.getRepertoryModel())
                            .setGoodsAddress(goodsApply.getRepertoryAddress())
                            .setPurposeName(goodsApply.getPurposeName())
                            .setCategoryName(goodsApply.getCategoryName());

                    return informationService.add(goods);
                }
            }
        }else{
            return CommonResult.error();
        }
        return CommonResult.success();
    }



    //  ======================直接申请报废=======================================

    /**
     * 管理员直接申请报废
     * @param goodsApply
     * @return
     */
    @RequestMapping("/scrapApply")
    //@RequiresPermissions("scrapApply")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult scrapApply(@RequestBody GoodsApply goodsApply){

        /**
         *
         //申请审核状态   默认 -1 未申请 0 待审核 1 通过 2 拒绝
         private Integer goodsApprovalStatus;

         //使用状态    默认 -1  未使用  0 借用 1 领用 2 归还 3 报废
         private Integer goodsUseStatus;


         借用、领用和报废都需要将库存内的指定物品减去对应数量，归还需加上对应数量。

         管理员可以对单个物品申请报废，并将这条信息直接加入审批表中。点击“申请报废”按钮，弹窗填写物品信息。
         若物品原本状态是“借用”，需填写“归还日期”；若物品原本状态是“领用”，无需填写“归还日期”。
         申请人和审批人都为管理员自己，审批结果默认为“通过”，状态应为“报废”。
         *该操作除管理员发现库存内有物品报废时可以发起，在根据料单归还的物品中若发现有物品报废也可发起。
         */
        goodsApply.setGoodsUseStatus(3);
        Goods goods = new Goods();
        goods.setGoodsName(goodsApply.getRepertoryName())
                .setGoodsAddress(goodsApply.getRepertoryAddress())
                .setGoodsSize(goodsApply.getRepertorySize())
                .setGoodsNumbers(goodsApply.getRepertoryNumbers())
                .setGoodsModel(goodsApply.getRepertoryModel())
                .setPurposeName(goodsApply.getPurposeName())
                .setCategoryName(goodsApply.getCategoryName());


        // 按照规格和型号去查询记录表
        // 申请的时候先扣除余量
        CommonResult tempResult1 = informationService.subtract(goods);
        if(tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
            CommonResult tempResult2 = goodsApplyService.insert(goodsApply);
            if(tempResult2.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())) {
                return CommonResult.success();
            }else{
                return CommonResult.error();
            }
        }else {
            return tempResult1;
        }

    }






}
