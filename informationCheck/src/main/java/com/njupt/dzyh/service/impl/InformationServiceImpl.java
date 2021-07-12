package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njupt.dzyh.dao.InformationDao;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.Information;
import com.njupt.dzyh.service.InformationService;

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
    public int insert(Goods user) {
        //插入或
        return 0;
    }

    @Override
    public int subtract(Goods goods) {
        return 0;
    }

    public List<Information> selectByCondition(String name, String size, String model) {
        QueryWrapper<Information> queryWrapper = new QueryWrapper<>();

        queryWrapper.like("repertory_name",name)
                    .or()
                    .like("repertory_size",size)
                    .or()
                    .like("repertory_model",model);
        List<Information> information = informationDao.selectList(queryWrapper);
        //System.out.println("返回信息查询数据");
        return information;
    }
}
