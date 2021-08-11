package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.njupt.dzyh.dao.UnderStockDao;
import com.njupt.dzyh.domain.*;
import com.njupt.dzyh.domain.roles.UserInfo;
import com.njupt.dzyh.service.UnderStockService;
import com.njupt.dzyh.service.UserTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UnderStockServiceImpl implements UnderStockService {

    @Autowired
    private UnderStockDao underStockDao;

    @Autowired
    private UserTempService userTempService;

    @Override
    public int insert(UnderStock underStock) {

        UserInfo userInfo  = userTempService.getUserInfoByUserId(underStock.getUserId());
        if(userInfo==null||userInfo.getUserName()==null) return -1;
        underStock.setUserId(userInfo.getUserName());
        int rec = underStockDao.insert(underStock);
        return rec;
    }

    @Override
    public List<UnderStock> getByCondition(UnderStock underStock) {
        QueryWrapper<UnderStock> queryWrapper = new QueryWrapper<>();
        String name=underStock.getUsName(),
                goodSize = underStock.getUsSize(),
                model = underStock.getUsModel();
        String typeId = underStock.getUsType();

        Date startDate = underStock.getCreateTime();
        Date endDate = underStock.getUpdateTime();


        queryWrapper.like("us_name",name)

                .like("us_size",goodSize)

                .like("us_model",model)
                .orderByAsc("create_time","read_status");
//        if(typeId>0)
            queryWrapper.like("us_type",typeId);
        if(startDate!=null)
            queryWrapper.ge("create_time",startDate);
        if(endDate != null)
            queryWrapper.le("create_time",endDate);

        List<UnderStock> records = underStockDao.selectList(queryWrapper);
        System.out.println("分页查询 end");
        //List<Information> information = informationDao.selectList(queryWrapper);
        //System.out.println("返回信息查询数据");

        return records;
    }

    @Override
    public SelectResult getByCondition(UnderStock underStock, int current, int size) {
        QueryWrapper<UnderStock> queryWrapper = new QueryWrapper<>();
        String name=underStock.getUsName(),
                goodSize = underStock.getUsSize(),
                model = underStock.getUsModel();

        String typeId = underStock.getUsType();
        Date startDate = underStock.getCreateTime();
        Date endDate = underStock.getUpdateTime();


        System.out.println("分页查询 begin");
        if(current<=0){
            current = 1;
        }
        if(size<=0){
            size = 4;
        }

        queryWrapper.like("us_name",name)

                .like("us_size",goodSize)

                .like("us_model",model)
                .orderByAsc("create_time","read_status");
//        if(typeId>0)
            queryWrapper.like("us_type",typeId);
        if(startDate!=null)
            queryWrapper.ge("create_time",startDate);
        if(endDate != null)
            queryWrapper.le("create_time",endDate);
        Page<UnderStock> page = new Page<>(current,size);
        underStockDao.selectPage(page,queryWrapper);
        List<UnderStock> records = page.getRecords();
        System.out.println("分页查询 end");
        //List<Information> information = informationDao.selectList(queryWrapper);
        //System.out.println("返回信息查询数据");

        return new SelectResult(page.getTotal(), records);
    }

    @Override
    public int setStatus(UnderStock underStock) {
        UnderStock temp = underStockDao.selectById(underStock.getUsId());
        if(temp==null) return -1;
        temp.setReadStatus(1);

        int rec = underStockDao.updateById(temp);
        if(rec!=1) return -1;
        return 0;
    }

    @Override
    public int setAllStatus() {
        QueryWrapper<UnderStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("read_status",0);
        List<UnderStock> list = underStockDao.selectList(queryWrapper);
        if(list.size()>0){
            for(UnderStock underStock : list){
                underStock.setReadStatus(1);
                underStockDao.updateById(underStock);
            }
        }
        return 0;
    }

    @Override
    public int delete(UnderStock underStock) {
        QueryWrapper<UnderStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("us_size",underStock.getUsSize())
                .eq("us_model",underStock.getUsModel());
        List<UnderStock> list = underStockDao.selectList(queryWrapper);
        if(list.size()==0) return 1;
        UnderStock temp = list.get(0);
        int rec = underStockDao.deleteById(temp.getUsId());
        if(rec==0) return -1;
        return 0;
    }

}
