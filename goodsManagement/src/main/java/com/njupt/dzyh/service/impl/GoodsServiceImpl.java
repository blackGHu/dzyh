package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.njupt.dzyh.dao.GoodsDao;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.service.GoodsService;
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
        goods.setGoodsId(2)
                .setGoodsName("asdsada")
                .setGoodsSize("222")
                .setGoodsModel("111")
                .setGoodsAddress("asd")
                .setGoodsApprovalStatus(1)
                .setGoodsPrice(100.0)
                .setGoodsNumbers(10)
                .setGoodsUseStatus(1)
                .setPurposeId(1)
                .setUserId("1");
        return goodsDao.insert(goods);
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
