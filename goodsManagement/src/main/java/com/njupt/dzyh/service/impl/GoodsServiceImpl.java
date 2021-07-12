package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njupt.dzyh.dao.GoodsDao;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.GoodsService;
import com.njupt.dzyh.utils.CommonResult;
import com.njupt.dzyh.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsDao,Goods> implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;


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
    public CommonResult selectByPage(int current,int size) {
        System.out.println("分页查询 begin");
        if(current<=0){
            current = 1;
        }
        if(size<=0){
            size = 4;
        }
        Page<Goods> page = new Page<>(current,size);
        goodsDao.selectPage(page,null);
        List<Goods> records = page.getRecords();
        System.out.println("分页查询 end");
        long total = page.getTotal();
//        long current = page.getCurrent();
//        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        if(0 ==records.size()){
            return CommonResult.error();
        }
        return CommonResult.success(records);
    }


    public List<Goods> selectByConditions(Map<String, Object> map) {
        return null;
    }


//     ---------插入操作--------------

    /**
     * 物品信息的录入
     * @param goods
     * @return
     */
    public CommonResult insert(Goods goods) {
        /*
        newGoods.setGoodsId(goods.getGoodsId())
                .setGoodsName(goods.getGoodsName() +  CommonUtil.getRandomCode(100))
                .setGoodsSize("222")
                .setGoodsModel("111")
                .setGoodsAddress("asd")
                .setGoodsApprovalStatus(1)
                .setGoodsPrice(100.0)
                .setGoodsNumbers(10)
                .setGoodsUseStatus(1)
                .setPurposeId(1)
                .setUserId("1");
         */
        if(CommonUtil.isNull(goods)){
            return CommonResult.error();
        }else if(CommonUtil.isNotNull(goodsDao.selectById(goods.getGoodsId()))){
            return CommonResult.error(CommonResultEm.ALREADY_EXIST);
        }else {
            Goods newGoods = new Goods();
            //  暂定 自己编写
            newGoods.setGoodsId(goods.getGoodsId())
                    .setGoodsName(goods.getGoodsName())
                    .setGoodsSize(goods.getGoodsSize())
                    .setGoodsModel(goods.getGoodsModel())
                    .setGoodsAddress(goods.getGoodsAddress())
                    .setGoodsApprovalStatus(goods.getGoodsApprovalStatus())
                    .setGoodsPrice(goods.getGoodsPrice())
                    .setGoodsNumbers(goods.getGoodsNumbers())
                    .setGoodsUseStatus(goods.getGoodsUseStatus())
                    .setPurposeId(goods.getPurposeId())
                    .setUserId(goods.getUserId());
            int rec = goodsDao.insert(newGoods);
            if(rec == 1)
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
        /*
        条件构造器作为参数进行更新
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("goods_name","xsc");

            User user = new User();
            user.setAge(18);

            Integer rows = userMapper.update(user, updateWrapper);

         */
        if (CommonUtil.isNull(goods)) {
            return CommonResult.error();
        } else{
            Goods newGoods = goodsDao.selectById(goods.getGoodsId());
            if (CommonUtil.isNull(newGoods)) {
                return CommonResult.error(CommonResultEm.NOT_EXIST);
            } else {
                //  暂定 自己编写
                newGoods.setGoodsName(goods.getGoodsName())
                        .setGoodsSize(goods.getGoodsSize())
                        .setGoodsModel(goods.getGoodsModel())
                        .setGoodsAddress(goods.getGoodsAddress())
                        .setGoodsApprovalStatus(goods.getGoodsApprovalStatus())
                        .setGoodsPrice(goods.getGoodsPrice())
                        .setGoodsNumbers(goods.getGoodsNumbers())
                        .setGoodsUseStatus(goods.getGoodsUseStatus())
                        .setPurposeId(goods.getPurposeId())
                        .setUserId(goods.getUserId());
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





}
