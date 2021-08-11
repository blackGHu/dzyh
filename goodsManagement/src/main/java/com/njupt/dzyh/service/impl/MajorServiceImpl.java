package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njupt.dzyh.dao.CollegeDao;
import com.njupt.dzyh.dao.MajorDao;
import com.njupt.dzyh.domain.College;
import com.njupt.dzyh.domain.Major;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.CollegeService;
import com.njupt.dzyh.service.MajorService;
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
 *
 * 注意：ID设置为自增还没有进行操作
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorDao, Major> implements MajorService {

    @Autowired
    private MajorDao majorDao;




//     ---------查询操作--------------
    /**
     * 查询所有物品
     * @return
     */
    public CommonResult selectAll() {
        System.out.println("hello--- ");
        List<Major> majorList =  majorDao.selectList(null);
        if(0 == majorList.size()){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(majorList);
    }

    /**
     * 根据ID查询物品
     * @param majorId
     * @return
     */
    public CommonResult selectById(int majorId) {
        Major major = majorDao.selectById(majorId);
        if(CommonUtil.isNull(major)){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(major);
    }




    public CommonResult selectByConditions(Map<String, Object> map) {
        List<Major> result = majorDao.selectByMap(map);
        if(0 == result.size()){
            return CommonResult.error();
        }
        return CommonResult.success(result);
    }


//     ---------插入操作--------------

    /**
     * 物品信息的录入
     * @param major
     * @return
     */
    public CommonResult insert(Major major) {
        if(CommonUtil.isNull(major)){
            return CommonResult.error();
        }else {
            QueryWrapper<Major> wrapper = new QueryWrapper<>();
            wrapper.eq("major_name", major.getMajorName());
            int count = majorDao.selectCount(wrapper);
            if (count == 1) {
                return CommonResult.error(CommonResultEm.ALREADY_EXIST);
            } else {
                Major newMajor = new Major();
                //  暂定 自己编写
                newMajor.setMajorName(major.getMajorName());

                int rec = majorDao.insert(newMajor);
                if (rec == 1)
                    return CommonResult.success();
                else
                    return CommonResult.error();
            }
        }

    }


    /**
     * 批量插入
     * @param majorList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult insertBatch(List<Major> majorList){
        if(0 == majorList.size()){
            return CommonResult.error();
        }
        Integer rec = majorDao.insertBatchSomeColumn(majorList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }



//     ---------删除操作--------------

    /**
     * 根据ID删除物品
     * @param majorId
     * @return
     */
    public CommonResult deletById(int majorId) {
        Major major = majorDao.selectById(majorId);
        if(CommonUtil.isNull(major)){
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }
        int rec = majorDao.deleteById(majorId);
        if(rec == 1){
            return CommonResult.success();
        }else
            return CommonResult.error();
    }

//     ---------更新操作--------------

    /**
     * 更新物品
     * @param major
     * @return
     */
    public CommonResult update(Major major) {
        /*
        条件构造器作为参数进行更新
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("goods_name","xsc");

            User user = new User();
            user.setAge(18);

            Integer rows = userMapper.update(user, updateWrapper);

         */
        if (CommonUtil.isNull(major)) {
            return CommonResult.error();
        } else{
            Major newMajor = majorDao.selectById(major.getMajorId());
            if (CommonUtil.isNull(newMajor)) {
                return CommonResult.error(CommonResultEm.NOT_EXIST);
            } else {
                //  暂定 自己编写
                newMajor.setMajorId(major.getMajorId())
                        .setMajorName(major.getMajorName());
                int rec = majorDao.updateById(newMajor);
                if (rec == 1)
                    return CommonResult.success();
                else
                    return CommonResult.error();
            }

        }
    }




}
