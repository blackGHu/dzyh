package com.njupt.dzyh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njupt.dzyh.dao.GoodsDao;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.dto.GenerateExcel;
import com.njupt.dzyh.domain.dto.GoodsPageDto;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.GoodsService;
import com.njupt.dzyh.service.InformationService;
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
public class GoodsServiceImpl extends ServiceImpl<GoodsDao,Goods> implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private InformationService informationService;



//     ---------查询操作--------------
    /**
     * 查询所有物品
     * @return
     */
    public CommonResult selectAll() {
        System.out.println("hello--- ");
        List<Goods> goodsList =  goodsDao.selectList(null);
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
        Goods goods = goodsDao.selectById(goodsId);
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
        Page<Goods> page = new Page<>(current,size);
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        if(null == conditionsMap){
            goodsDao.selectPage(page,null);
        }else {
            if(conditionsMap.containsKey("goodsName")){
                wrapper.like("goods_name",conditionsMap.get("goodsName"));
                conditionsMap.remove("goodsName");
            }
            if(conditionsMap.containsKey("goodsSize")){
                wrapper.like("goods_size",conditionsMap.get("goodsSize"));
                conditionsMap.remove("goodsSize");
            }
            if(conditionsMap.containsKey("goodsModel")){
                wrapper.like("goods_model",conditionsMap.get("goodsModel"));
                conditionsMap.remove("goodsModel");
            }
            if(conditionsMap.containsKey("buyUserName")){
                wrapper.like("buy_user_name",conditionsMap.get("buyUserName"));
                conditionsMap.remove("buyUserName");
            }
            for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (CommonUtil.isNotNull(value)) {
                    wrapper.eq(CommonUtil.camel2Underline(key), value);
                }
            }
            goodsDao.selectPage(page, wrapper);
        }
        List<Goods> goodsList = page.getRecords();
        System.out.println("分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==goodsList.size()){
            return CommonResult.error();
        }
        return CommonResult.success(new GoodsPageDto(total,goodsList));
    }


    public CommonResult selectByConditions(Map<String, Object> map) {
        List<Goods> result = goodsDao.selectByMap(map);
        if(0 == result.size()){
            return CommonResult.error();
        }
        return CommonResult.success(result);
    }


//     ---------插入操作--------------

    /**
     * 物品信息的录入
     * @param goods
     * @return
     */
    public CommonResult insert(Goods goods) {
        if(CommonUtil.isNull(goods)){
            return CommonResult.error();
//        }else if(CommonUtil.isNotNull(goodsDao.selectById(goods.getGoodsId()))){
//            return CommonResult.error(CommonResultEm.ALREADY_EXIST);
        }else{
            Goods newGoods = new Goods();
            //  暂定 自己编写
            newGoods.setGoodsName(goods.getGoodsName())
                    .setGoodsSize(goods.getGoodsSize())
                    .setGoodsModel(goods.getGoodsModel())
                    .setGoodsPrice(goods.getGoodsPrice())
                    .setGoodsNumbers(goods.getGoodsNumbers())
                    .setCategoryName(goods.getCategoryName())
                    .setRoleId(goods.getRoleId())
                    .setBuyUserName(goods.getBuyUserName())
                    .setGoodsAddress(goods.getGoodsAddress())
                    .setPurposeName(goods.getPurposeName());
            int rec = goodsDao.insert(newGoods);
            if (rec == 1) {
                CommonResult result = informationService.add(goods);
                if(!result.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                    return CommonResult.error();
                }
                return CommonResult.success();
            }
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
    public CommonResult insertBatch(List<Goods> goodsList){
        if(0 == goodsList.size()){
            return CommonResult.error();
        }
        Integer rec = goodsDao.insertBatchSomeColumn(goodsList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }



//     ---------删除操作--------------

    /**
     * 根据ID删除物品
     * @param goodsId
     * @return
     */
    public CommonResult deletById(int goodsId) {
        Goods goods = goodsDao.selectById(goodsId);
        if(CommonUtil.isNull(goods)){
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }
        int rec = goodsDao.deleteById(goodsId);
        if(rec == 1){
            return CommonResult.success();
        }else
            return CommonResult.error();
    }

//     ---------更新操作--------------

    /**
     * 更新物品
     * @param goods
     * @return
     */
    public CommonResult update(Goods goods) {
        if (CommonUtil.isNull(goods)) {
            return CommonResult.error();
        } else{
            Goods newGoods = goodsDao.selectById(goods.getGoodsId());
            if (CommonUtil.isNull(newGoods)) {
                return CommonResult.error(CommonResultEm.NOT_EXIST);
            } else {
                //判断数量是否变更，如果变更，同时还需要进行回加和减
                int nowNum = goods.getGoodsNumbers();
                int oldNum = newGoods.getGoodsNumbers();
                if(oldNum < nowNum){
                    //加num  now-old
                    Goods tempGoods = new Goods();
                    tempGoods.setGoodsName(goods.getGoodsName())
                            .setGoodsSize(goods.getGoodsSize())
                            .setGoodsNumbers(nowNum-oldNum)
                            .setGoodsModel(goods.getGoodsModel())
                            .setGoodsAddress(goods.getGoodsAddress());

                    CommonResult tempResult1 = informationService.add(tempGoods);
                    if(!tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                        return tempResult1;
                    }
                }else if(oldNum > nowNum){
                    //减num  old-now
                    Goods tempGoods = new Goods();
                    tempGoods.setGoodsName(goods.getGoodsName())
                            .setGoodsSize(goods.getGoodsSize())
                            .setGoodsNumbers(oldNum-nowNum)
                            .setGoodsModel(goods.getGoodsModel())
                            .setGoodsAddress(goods.getGoodsAddress());
                    CommonResult tempResult1 = informationService.subtract(tempGoods);
                    if(!tempResult1.getResultCode().equals(CommonResultEm.SUCCESS.getEcode())){
                        return tempResult1;
                    }
                }
                newGoods.setGoodsId(goods.getGoodsId())
                        .setGoodsName(goods.getGoodsName())
                        .setGoodsSize(goods.getGoodsSize())
                        .setGoodsModel(goods.getGoodsModel())
                        .setGoodsAddress(goods.getGoodsAddress())
                        .setGoodsPrice(goods.getGoodsPrice())
                        .setGoodsNumbers(goods.getGoodsNumbers())
                        .setBuyUserName(goods.getBuyUserName())
                        .setRoleId(goods.getRoleId())
                        .setCategoryName(goods.getCategoryName())
                        .setPurposeName(goods.getPurposeName());
                int rec = goodsDao.updateById(newGoods);
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
    public CommonResult updateBatch(List<Goods> goodsList){
        if(0 == goodsList.size()){
            return CommonResult.error();
        }
        Integer rec = goodsDao.updateBatchSomeColumn(goodsList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }


//     ---------导出报表--------------

    /**
     * 导出EXCEL报表   xsc
     * @param generateExcel
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException {
        // 1、首先根据查询查出所需信息（查询全部、条件查询）
        // 2、然后将结果封装成List<T>后 再封装成GenerateExcel实体
        // 3、调用service.generateExcel（。。。）
            List<Goods> goodsList = generateExcel.getGoodsList();
            System.out.println("[generateExcel]  goodsList---\t" + goodsList);
            // goodsList 中的 createTime 和 updateTime 正常，后面转成JSON就不正常了
            // 需添加注解 @JSONField(format = "yyyy-MM-dd HH:mm:ss")  或者 将数据转换成json对象时，使用JSON.toJSON
            if (0 == goodsList.size()) {
                return CommonResult.error();
            }
            JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(goodsList));
            System.out.println("[generateExcel]  jsonArray---\t" + jsonArray.toJSONString());

            //        "../excelTest/","test.xls"
            CommonUtil.JsonToExcel(jsonArray, generateExcel.getOutUrl(), generateExcel.getFileName());
            return CommonResult.success();
    }



}
