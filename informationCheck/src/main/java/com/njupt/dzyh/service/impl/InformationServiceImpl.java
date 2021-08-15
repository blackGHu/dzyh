package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.njupt.dzyh.dao.InformationDao;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.Information;
import com.njupt.dzyh.domain.SelectResult;
import com.njupt.dzyh.domain.UnderStock;
import com.njupt.dzyh.enums.CommonResultEm;

import com.njupt.dzyh.service.InformationService;
import com.njupt.dzyh.service.UnderStockService;
import com.njupt.dzyh.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InformationServiceImpl implements InformationService {
    @Autowired
    private UnderStockService underStockService;

    @Autowired
    private InformationDao informationDao;

    @Override
    public int getItem(Goods goods) {
        String model = goods.getGoodsModel();
        String size = goods.getGoodsSize();

        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repertory_model",model)
                .eq("repertory_size",size);
        Information temp = informationDao.selectOne(queryWrapper);
        if(temp==null){
            return 0;
        }
        return temp.getRepertoryNumbers();
    }

    @Override
    public Information getInfomation(Goods goods) {
        String model = goods.getGoodsModel();
        String size = goods.getGoodsSize();

        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repertory_model",model)
                .eq("repertory_size",size);
        Information temp = informationDao.selectOne(queryWrapper);
        return temp;
    }

    @Override
    public CommonResult updateInformation(Information information) {
        /*QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repertory_model",information.getRepertoryModel())
                .eq("repertory_size",information.getRepertorySize());*/
        Information temp = informationDao.selectById(information.getRepertoryId());
        if(temp==null) return CommonResult.error(CommonResultEm.ERROR,"物品不存在");

        temp.setRepertoryType(information.getRepertoryType());
        temp.setRepertoryName(information.getRepertoryName());
        temp.setRepertorySize(information.getRepertorySize());
        temp.setRepertoryModel(information.getRepertoryModel());
        temp.setRepertoryNumbers(information.getRepertoryNumbers());
        temp.setRepertoryUse(information.getRepertoryUse());
        temp.setRepertoryAutho(information.getRepertoryAutho());
        temp.setRepertoryPrice(information.getRepertoryPrice());
        temp.setRepertoryBuyname(information.getRepertoryBuyname());
        temp.setStorageTime(information.getStorageTime());
        temp.setRepertoryAddress(information.getRepertoryAddress());
        temp.setRepertoryMessage(information.getRepertoryMessage());

        int rec = informationDao.updateById(temp);
        if(rec == 0)
            return CommonResult.error(CommonResultEm.ERROR,"更新失败");
        return CommonResult.success("更新成功");
    }

    @Override
    public CommonResult add(Goods goods) {
        String model = goods.getGoodsModel();
        int number = goods.getGoodsNumbers();
        String size = goods.getGoodsSize();

        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repertory_model",model)
                    .eq("repertory_size",size);
        Information temp = new Information();
        if(informationDao.selectOne(queryWrapper)!=null){
            temp = informationDao.selectOne(queryWrapper);
            temp.setRepertoryNumbers(temp.getRepertoryNumbers()+number);
            /*
            if(goods.getGoodsAddress().length()>0)
                temp.setRepertoryAddress(goods.getGoodsAddress());
            if(goods.getPurposeName().length()>0)
                temp.setRepertoryUse(goods.getPurposeName());
            if(goods.getRoleName().length()>0)
                temp.setRepertoryAutho(goods.getRoleName());*/
            informationDao.update(temp,queryWrapper);
            return CommonResult.success(model+"入库成功");
        }
        else{
            temp.setRepertoryType(goods.getCategoryName());
            temp.setRepertoryName(goods.getGoodsName());
            temp.setRepertorySize(goods.getGoodsSize());
            temp.setRepertoryModel(goods.getGoodsModel());
            temp.setRepertoryNumbers(goods.getGoodsNumbers());
            temp.setRepertoryUse(goods.getPurposeName());
            temp.setRepertoryAutho(goods.getRoleName());
            temp.setRepertoryPrice(goods.getGoodsPrice());
            temp.setRepertoryBuyname(goods.getBuyUserName());
            temp.setStorageTime(goods.getStorageTime());
            temp.setRepertoryAddress(goods.getGoodsAddress());
            temp.setRepertoryMessage("空");

            //System.out.println(temp);
            informationDao.insert(temp);
            return CommonResult.success(model+"插入成功");
        }
    }



    @Override
    public CommonResult subtract(Goods goods) {
        String model = goods.getGoodsModel();
        int number = goods.getGoodsNumbers();
        String size = goods.getGoodsSize();

        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repertory_model",model)
                .eq("repertory_size",size);
        Information temp = informationDao.selectOne(queryWrapper);
        //System.out.println(goods);
        if(temp!=null){
            if(temp.getRepertoryNumbers()-number<0){
                UnderStock underStock = new UnderStock();
                underStock.setUsName(temp.getRepertoryName());
                underStock.setUsType(temp.getRepertoryType());
                underStock.setUsSize(temp.getRepertorySize());
                underStock.setUsModel(temp.getRepertoryModel());
                underStock.setUsNumbers(number);
                underStock.setUserName(goods.getBuyUserName());
                underStock.setReadStatus(0);
                int rec=underStockService.insert(underStock);
                System.out.println(rec);
                return CommonResult.error(CommonResultEm.ERROR,model+"余量不足");
            }
            /*if(goods.getGoodsAddress().length()>0)
                temp.setRepertoryAddress(goods.getGoodsAddress());*/
            temp.setRepertoryNumbers(temp.getRepertoryNumbers()-number);
            informationDao.update(temp,queryWrapper);
            /*if(goods.getGoodsAddress().length()>0)//表示是信息编辑，不是真的借用
                return CommonResult.success("信息编辑成功");*/
            return CommonResult.success(model+" "+size+"申请成功，请到 "+temp.getRepertoryAddress()+" 领取");
        }
        else{
            return CommonResult.error(CommonResultEm.ERROR,model+"不存在");
        }
    }

    public CommonResult selectByCondition(Information con,int current,int size) {
        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();


        System.out.println("分页查询 begin");
        if(current<=0){
            current = 1;
        }
        if(size<=0){
            size = 4;
        }

        if(con.getRepertoryName()!=null)
            queryWrapper.like("repertory_name",con.getRepertoryName());
        if(con.getRepertorySize()!=null)
            queryWrapper.like("repertory_size",con.getRepertorySize());
        if(con.getRepertoryModel()!=null)
            queryWrapper.like("repertory_model",con.getRepertoryModel());
        if(con.getRepertoryBuyname()!=null)
            queryWrapper.like("repertory_buyname",con.getRepertoryBuyname());
        if(con.getRepertoryType()!=null)
            queryWrapper.like("repertory_type",con.getRepertoryType());
        if(con.getRepertoryAutho()!=null)
            queryWrapper.like("repertory_autho",con.getRepertoryAutho());
        if(con.getRepertoryUse()!=null)
            queryWrapper.like("repertory_use",con.getRepertoryUse());

        Page<Information> page = new Page<>(current,size);
        informationDao.selectPage(page,queryWrapper);
        List<Information> records = page.getRecords();
        System.out.println("分页查询 end");
        //List<Information> information = informationDao.selectList(queryWrapper);
        //System.out.println("返回信息查询数据");
        List<Information> result = new ArrayList<>();
        for(Information information : records){
            information.setRepertoryBuyname(null);
            information.setStorageTime(null);
            information.setRepertoryPrice(null);
            information.setRepertoryAutho(null);
            information.setRepertoryMessage(null);
            information.setRepertoryUse(null);
            information.setRepertoryAddress(null);
            result.add(information);
        }
        if(records==null) return CommonResult.error(CommonResultEm.ERROR);
        return CommonResult.success(new SelectResult(page.getTotal(),result));
    }

    @Override
    public CommonResult selectAllByCondition(Information con, int current, int size) {
        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();


        System.out.println("分页查询 begin");
        if(current<=0){
            current = 1;
        }
        if(size<=0){
            size = 4;
        }

        if(con.getRepertoryName()!=null)
            queryWrapper.like("repertory_name",con.getRepertoryName());
        if(con.getRepertorySize()!=null)
            queryWrapper.like("repertory_size",con.getRepertorySize());
        if(con.getRepertoryModel()!=null)
            queryWrapper.like("repertory_model",con.getRepertoryModel());
        if(con.getRepertoryBuyname()!=null)
            queryWrapper.like("repertory_buyname",con.getRepertoryBuyname());
        if(con.getRepertoryType()!=null)
            queryWrapper.like("repertory_type",con.getRepertoryType());
        if(con.getRepertoryAutho()!=null)
            queryWrapper.like("repertory_autho",con.getRepertoryAutho());
        if(con.getRepertoryUse()!=null)
            queryWrapper.like("repertory_use",con.getRepertoryUse());

        Page<Information> page = new Page<>(current,size);
        informationDao.selectPage(page,queryWrapper);
        List<Information> records = page.getRecords();
        System.out.println("分页查询 end");
        //List<Information> information = informationDao.selectList(queryWrapper);
        //System.out.println("返回信息查询数据");

        if(records==null) return CommonResult.error(CommonResultEm.ERROR);
        return CommonResult.success(new SelectResult(page.getTotal(),records));
    }

    @Override
    public List<Information> selectAllByCondition(Information con) {
        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();

        if(con.getRepertoryName()!=null)
            queryWrapper.like("repertory_name",con.getRepertoryName());
        if(con.getRepertorySize()!=null)
            queryWrapper.like("repertory_size",con.getRepertorySize());
        if(con.getRepertoryModel()!=null)
            queryWrapper.like("repertory_model",con.getRepertoryModel());
        if(con.getRepertoryBuyname()!=null)
            queryWrapper.like("repertory_buyname",con.getRepertoryBuyname());
        if(con.getRepertoryType()!=null)
            queryWrapper.like("repertory_type",con.getRepertoryType());
        if(con.getRepertoryAutho()!=null)
            queryWrapper.like("repertory_autho",con.getRepertoryAutho());
        if(con.getRepertoryUse()!=null)
            queryWrapper.like("repertory_use",con.getRepertoryUse());


        List<Information> records = informationDao.selectList(queryWrapper);
        System.out.println(records.size()+"条Information导出文件");
        //List<Information> information = informationDao.selectList(queryWrapper);
        //System.out.println("返回信息查询数据");

        return records;
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
        if(records==null){
            return CommonResult.error();
        }
        return CommonResult.success(records);
    }

    @Override
    public CommonResult deleteInformationById(Information information) {
        int rec = informationDao.deleteById(information.getRepertoryId());
        if(rec==0)
            return CommonResult.error(CommonResultEm.ERROR,"删除失败");
        return CommonResult.success("删除成功");
    }
}
