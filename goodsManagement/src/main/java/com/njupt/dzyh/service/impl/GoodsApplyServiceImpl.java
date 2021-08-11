package com.njupt.dzyh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njupt.dzyh.dao.GoodsApplyDao;
import com.njupt.dzyh.domain.GoodsApply;
import com.njupt.dzyh.domain.dto.GenerateGoodsApplyExcel;
import com.njupt.dzyh.domain.dto.GoodsApplyListDto;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.GoodsApplyService;
import com.njupt.dzyh.utils.CommonResult;
import com.njupt.dzyh.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
 *
 * 注意：ID设置为自增还没有进行操作
 */
@Service
public class GoodsApplyServiceImpl extends ServiceImpl<GoodsApplyDao, GoodsApply> implements GoodsApplyService {

    @Autowired
    private GoodsApplyDao goodsApplyDao;





//     ---------查询操作--------------
    /**
     * 查询所有审批订单
     * @return
     */
    public CommonResult selectAll() {
        System.out.println("hello--- ");
        List<GoodsApply> goodsList =  goodsApplyDao.selectList(null);
        if(0 == goodsList.size()){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(goodsList);
    }

    /**
     * 根据ID查询物品
     * @param goodsId
     * @return
     */
    public CommonResult selectById(int goodsId) {
        GoodsApply goods = goodsApplyDao.selectById(goodsId);
        if(CommonUtil.isNull(goods)){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(goods);
    }


    /**
     * 分页查询所有
     * @param current  当前页码         默认1
     * @param size     每页显示数量     默认4
     * @return
     */
    public CommonResult selectByPage(int current,int size,Map<String,Object> conditionsMap) {
        System.out.println("分页查询 begin");
        if(current<=0 || CommonUtil.isNull(current)){
            current = 1;
        }
        if(size<=0 || CommonUtil.isNull(size)){
            size = 4;
        }
        Page<GoodsApply> page = new Page<>(current,size);
        QueryWrapper<GoodsApply> wrapper = new QueryWrapper<>();
        if(null == conditionsMap){
            goodsApplyDao.selectPage(page,null);
        }else {
            if(conditionsMap.containsKey("categoryId")){
                wrapper.eq("category_id",conditionsMap.get("categoryId"));
                conditionsMap.remove("categoryId");
            }
            if(conditionsMap.containsKey("purposeId")){
                wrapper.eq("purpose_id",conditionsMap.get("purposeId"));
                conditionsMap.remove("purposeId");
            }
            if(conditionsMap.containsKey("goodsUseStatus")){
                wrapper.eq("goods_use_status",conditionsMap.get("goodsUseStatus"));
                conditionsMap.remove("goodsUseStatus");
            }
            //  当有时间范围的时候需要编写   between(...)
            if (conditionsMap.containsKey("borrowTime")) {
                //  如果只有申请日期查询，条件为大于等于这个日期
                //  如果有申请日期，还有归还日期，则筛选这两个日期间的
                if (CommonUtil.isNotNull(conditionsMap.get("borrowTime"))) {
                    wrapper.ge("borrow_time", conditionsMap.get("borrowTime"));
                }
                conditionsMap.remove("borrowTime");
            }
            if (conditionsMap.containsKey("returnTime")) {
                if (CommonUtil.isNotNull(conditionsMap.get("returnTime"))) {
                    wrapper.le("return_time", conditionsMap.get("returnTime"));
                }
                conditionsMap.remove("returnTime");
            }
            for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (CommonUtil.isNotNull(value)) {
                    wrapper.like(CommonUtil.camel2Underline(key), value);
                }
            }
            goodsApplyDao.selectPage(page, wrapper);

        }
        List<GoodsApply> goodsApplyList = page.getRecords();

        System.out.println("分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==goodsApplyList.size()){
            return CommonResult.error();
        }
        return CommonResult.success(new GoodsApplyListDto(total,goodsApplyList));
    }


    public CommonResult selectByConditions(Map<String, Object> conditionsMap) {
        System.out.println("条件查询 begin");
        List<GoodsApply> list = null;
        QueryWrapper<GoodsApply> wrapper = new QueryWrapper<>();
        //没有条件
        if(null == conditionsMap || 0 == conditionsMap.size()){
            list = goodsApplyDao.selectList(null);
        }else {
            // 有条件
            if(conditionsMap.containsKey("categoryId")){
                wrapper.eq("category_id",conditionsMap.get("categoryId"));
                conditionsMap.remove("categoryId");
            }
            if(conditionsMap.containsKey("purposeId")){
                wrapper.eq("purpose_id",conditionsMap.get("purposeId"));
                conditionsMap.remove("purposeId");
            }
            if(conditionsMap.containsKey("goodsUseStatus")){
                wrapper.eq("goods_use_status",conditionsMap.get("goodsUseStatus"));
                conditionsMap.remove("goodsUseStatus");
            }
            //  当有时间范围的时候需要编写   between(...)
            if (conditionsMap.containsKey("borrowTime")) {
                //  如果只有申请日期查询，条件为大于等于这个日期
                //  如果有申请日期，还有归还日期，则筛选这两个日期间的
                if (CommonUtil.isNotNull(conditionsMap.get("borrowTime"))) {
                    wrapper.ge("borrow_time", conditionsMap.get("borrowTime"));
                }
                conditionsMap.remove("borrowTime");
            }
            if (conditionsMap.containsKey("returnTime")) {
                if (CommonUtil.isNotNull(conditionsMap.get("returnTime"))) {
                    wrapper.le("return_time", conditionsMap.get("returnTime"));
                }
                conditionsMap.remove("returnTime");
            }
            for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (CommonUtil.isNotNull(value)) {
                    wrapper.like(CommonUtil.camel2Underline(key), value);
                }
            }
            list = goodsApplyDao.selectList(wrapper);
        }
        if(0 == list.size() || null == list){
            return CommonResult.error();
        }else {
            return CommonResult.success(list);
        }
    }


//     ---------插入操作--------------

    /**
     * 物品申请信息的录入
     * @param goodsApply
     * @return
     */
    public CommonResult insert(GoodsApply goodsApply) {

        if(CommonUtil.isNull(goodsApply)){
            return CommonResult.error();
        }else if(CommonUtil.isNotNull(goodsApplyDao.selectById(goodsApply.getOrderId()))){
            return CommonResult.error(CommonResultEm.ALREADY_EXIST);
        }else{
            GoodsApply newGoodsApply = new GoodsApply();
            //  暂定 自己编写
            newGoodsApply.setCategoryName(goodsApply.getCategoryName())
                    .setPurposeName(goodsApply.getPurposeName())
                    .setRepertoryName(goodsApply.getRepertoryName())
                    .setRepertorySize(goodsApply.getRepertorySize())
                    .setRepertoryModel(goodsApply.getRepertoryModel())
                    .setRepertoryNumbers(goodsApply.getRepertoryNumbers())
                    .setGoodsApprovalStatus(goodsApply.getGoodsApprovalStatus())
                    .setGoodsUseStatus(goodsApply.getGoodsUseStatus())
                    .setBorrowTime(goodsApply.getBorrowTime())
                    .setReturnTime(goodsApply.getReturnTime())
                    .setApplyUserName(goodsApply.getApplyUserName())
                    .setApprovalUserName(goodsApply.getApprovalUserName())
                    .setRepertoryAddress(goodsApply.getRepertoryAddress());
            int rec = goodsApplyDao.insert(newGoodsApply);
            if (rec == 1)
                return CommonResult.success();
            else
                return CommonResult.error();
        }
    }


    /**
     * 批量插入
     * @param goodsList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult insertBatch(List<GoodsApply> goodsList){
        if(0 == goodsList.size()){
            return CommonResult.error();
        }
        Integer rec = goodsApplyDao.insertBatchSomeColumn(goodsList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }



//     ---------删除操作--------------

    /**
     * 根据ID删除物品申请审批表的信息
     * 对于超管可以进行删除操作，如果原先是通过的 删除后要回加
     * @param orderId
     * @return
     */
    public CommonResult deletById(int orderId) {
        GoodsApply goods = goodsApplyDao.selectById(orderId);
        if(CommonUtil.isNull(goods)){
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }
        int rec = goodsApplyDao.deleteById(orderId);
        if(rec == 1){
            return CommonResult.success();
        }else
            return CommonResult.error();
    }



//     ---------更新操作--------------

    /**
     * 更新物品
     * @param goodsApply
     * @return
     */
    public CommonResult update(GoodsApply goodsApply) {
        /*
        条件构造器作为参数进行更新
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("goods_name","xsc");

            User user = new User();
            user.setAge(18);

            Integer rows = userMapper.update(user, updateWrapper);

         */
        if (CommonUtil.isNull(goodsApply)) {
            return CommonResult.error();
        } else{
            GoodsApply newGoodsApply = goodsApplyDao.selectById(goodsApply.getOrderId());
            if (CommonUtil.isNull(newGoodsApply)) {
                return CommonResult.error(CommonResultEm.NOT_EXIST);
            } else {
                //  暂定 自己编写
                newGoodsApply.setOrderId(goodsApply.getOrderId())
                        .setCategoryName(goodsApply.getCategoryName())
                        .setPurposeName(goodsApply.getPurposeName())
                        .setRepertoryName(goodsApply.getRepertoryName())
                        .setRepertorySize(goodsApply.getRepertorySize())
                        .setRepertoryModel(goodsApply.getRepertoryModel())
                        .setRepertoryNumbers(goodsApply.getRepertoryNumbers())
                        .setGoodsApprovalStatus(goodsApply.getGoodsApprovalStatus())
                        .setGoodsUseStatus(goodsApply.getGoodsUseStatus())
                        .setBorrowTime(goodsApply.getBorrowTime())
                        .setReturnTime(goodsApply.getReturnTime())
                        .setApplyUserName(goodsApply.getApplyUserName())
                        .setApprovalUserName(goodsApply.getApprovalUserName());
                int rec = goodsApplyDao.updateById(newGoodsApply);
                if (rec == 1)
                    return CommonResult.success();
                else
                    return CommonResult.error();
            }

        }
    }

    /**
     * 批量更新
     * @param goodsList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateBatch(List<GoodsApply> goodsList){
        if(0 == goodsList.size()){
            return CommonResult.error();
        }
        Integer rec = goodsApplyDao.updateBatchSomeColumn(goodsList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }


//     ---------导出报表--------------

    /**
     * 导出EXCEL报表   xsc
     * @param generateGoodsApplyExcel
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult generateExcel(GenerateGoodsApplyExcel generateGoodsApplyExcel) throws IOException, ParseException {
        // 1、首先根据查询查出所需信息（查询全部、条件查询）
        // 2、然后将结果封装成List<T>后 再封装成GenerateExcel实体
        // 3、调用service.generateExcel（。。。）
            List<GoodsApply> goodsList = generateGoodsApplyExcel.getGoodsApplyList();
            System.out.println("[generateExcel]  goodsList---\t" + goodsList);
            // goodsList 中的 createTime 和 updateTime 正常，后面转成JSON就不正常了
            // 需添加注解 @JSONField(format = "yyyy-MM-dd HH:mm:ss")  或者 将数据转换成json对象时，使用JSON.toJSON
            if (0 == goodsList.size()) {
                return CommonResult.error();
            }
            JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(goodsList));
            System.out.println("[generateExcel]  jsonArray---\t" + jsonArray.toJSONString());

            //        "../excelTest/","test.xls"
            CommonUtil.JsonToExcel(jsonArray, generateGoodsApplyExcel.getOutUrl(), generateGoodsApplyExcel.getFileName());
            return CommonResult.success();
    }




}
