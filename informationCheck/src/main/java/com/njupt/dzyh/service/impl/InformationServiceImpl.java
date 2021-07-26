package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.njupt.dzyh.dao.InformationDao;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.Information;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.InformationService;

import com.njupt.dzyh.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InformationServiceImpl implements InformationService {

    @Autowired
    private InformationDao informationDao;

    @Override
    public CommonResult add(Goods goods) {
        String model = goods.getGoodsModel();
        int number = goods.getGoodsNumbers();

        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repertory_model",model);
        Information temp = informationDao.selectOne(queryWrapper);
        //System.out.println(goods);
        if(temp!=null){
            temp.setRepertoryNumbers(temp.getRepertoryNumbers()+number);
            informationDao.update(temp,queryWrapper);
            return CommonResult.success(model+"入库成功");
        }
        else{
            temp = new Information();
            temp.setRepertoryName(goods.getGoodsName());
            temp.setRepertorySize(goods.getGoodsSize());
            temp.setRepertoryModel(goods.getGoodsModel());
            temp.setRepertoryNumbers(goods.getGoodsNumbers());
            //System.out.println(temp);
            informationDao.insert(temp);
            return CommonResult.success(model+"插入成功");
        }
    }

    @Override
    public CommonResult subtract(Goods goods) {
        String model = goods.getGoodsModel();
        int number = goods.getGoodsNumbers();

        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repertory_model",model);
        Information temp = informationDao.selectOne(queryWrapper);
        //System.out.println(goods);
        if(temp!=null){
            if(temp.getRepertoryNumbers()-number<0)
                return CommonResult.error(CommonResultEm.ERROR,model+"余量不足");
            temp.setRepertoryNumbers(temp.getRepertoryNumbers()-number);
            informationDao.update(temp,queryWrapper);
            return CommonResult.success(model+"出库成功");
        }
        else{
            return CommonResult.error(CommonResultEm.ERROR,model+"不存在");
        }
    }

    public CommonResult selectByCondition(Information con) {
        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
        String name=con.getRepertoryName(),
                size = con.getRepertorySize(),
                model = con.getRepertoryModel();
       /* if(name.length()==0) name = " ";
        if(size.length()==0) size=" ";
        if(model.length()==0) model=" ";*/

        queryWrapper.like("repertory_name",name)

                    .like("repertory_size",size)

                    .like("repertory_model",model);
        List<Information> information = informationDao.selectList(queryWrapper);
        //System.out.println("返回信息查询数据");

        if(information.size()==0) return CommonResult.error(CommonResultEm.ERROR);
        return CommonResult.success(information);
    }

    @Override
    public CommonResult selectAllInfoByPage(int current, int size) {
        System.out.println("分页查询 begin");
        if(current<=0){
            current = 1;
        }
        if(size<=0){
            size = 4;
        }
        Page<Information> page = new Page<>(current,size);
        informationDao.selectPage(page,null);
        List<Information> records = page.getRecords();
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
}
