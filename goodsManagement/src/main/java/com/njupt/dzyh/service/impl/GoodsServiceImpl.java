package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.njupt.dzyh.dao.GoodsDao;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.service.GoodsService;
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
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<Goods> selectAll() {
        return goodsDao.selectList(null);
    }

    public Goods selectById(int id) {
        return goodsDao.selectById(id);
    }

    public int insert(Goods goods) {
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
        Goods newGoods = new Goods();
        newGoods.setGoodsId(goods.getGoodsId())
                .setGoodsName(goods.getGoodsName() +  CommonUtil.getRandomCode(100))
                .setGoodsSize(goods.getGoodsSize())
                .setGoodsModel(goods.getGoodsModel())
                .setGoodsAddress(goods.getGoodsAddress())
                .setGoodsApprovalStatus(goods.getGoodsApprovalStatus())
                .setGoodsPrice(goods.getGoodsPrice())
                .setGoodsNumbers(goods.getGoodsNumbers())
                .setGoodsUseStatus(goods.getGoodsUseStatus())
                .setPurposeId(goods.getPurposeId())
                .setUserId(goods.getUserId());
        return goodsDao.insert(newGoods);
    }

    public int deletById(int id) {
        return goodsDao.deleteById(id);
    }

    public int update(Goods goods) {
        return goodsDao.updateById(goods);
    }

    public List<Goods> selectByConditions(Map<String, Object> map) {
        return null;
    }

    /**
     * 分页查询
     * @return
     */
    public List<Goods> selectByPage() {
        Page<Goods> page = new Page<>(1,2);
        goodsDao.selectPage(page,null);
        List<Goods> records = page.getRecords();
        long total = page.getTotal();
        long current = page.getCurrent();
        long size = page.getSize();
        System.out.println(total + "--" + current + "--" + size);
        return records;
    }
}
