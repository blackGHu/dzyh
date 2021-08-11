package com.njupt.dzyh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njupt.dzyh.dao.CategoryDao;
import com.njupt.dzyh.dao.CollegeDao;
import com.njupt.dzyh.domain.Category;
import com.njupt.dzyh.domain.College;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.CategoryService;
import com.njupt.dzyh.service.CollegeService;
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
public class CollegeServiceImpl extends ServiceImpl<CollegeDao, College> implements CollegeService {

    @Autowired
    private CollegeDao collegeDao;




//     ---------查询操作--------------
    /**
     * 查询所有物品
     * @return
     */
    public CommonResult selectAll() {
        System.out.println("hello--- ");
        List<College> collegeList =  collegeDao.selectList(null);
        if(0 == collegeList.size()){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(collegeList);
    }

    /**
     * 根据ID查询物品
     * @param collegeId
     * @return
     */
    public CommonResult selectById(int collegeId) {
        College college = collegeDao.selectById(collegeId);
        if(CommonUtil.isNull(college)){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(college);
    }




    public CommonResult selectByConditions(Map<String, Object> map) {
        List<College> result = collegeDao.selectByMap(map);
        if(0 == result.size()){
            return CommonResult.error();
        }
        return CommonResult.success(result);
    }


//     ---------插入操作--------------

    /**
     * 物品信息的录入
     * @param college
     * @return
     */
    public CommonResult insert(College college) {
        if(CommonUtil.isNull(college)){
            return CommonResult.error();
        }else {
            QueryWrapper<College> wrapper = new QueryWrapper<>();
            wrapper.eq("college_name", college.getCollegeName());
            int count = collegeDao.selectCount(wrapper);
            if (count == 1) {
                return CommonResult.error(CommonResultEm.ALREADY_EXIST);
            } else {
                College newCollege = new College();
                //  暂定 自己编写
                newCollege.setCollegeName(college.getCollegeName());

                int rec = collegeDao.insert(newCollege);
                if (rec == 1)
                    return CommonResult.success();
                else
                    return CommonResult.error();
            }
        }

    }


    /**
     * 批量插入
     * @param collegeList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult insertBatch(List<College> collegeList){
        if(0 == collegeList.size()){
            return CommonResult.error();
        }
        Integer rec = collegeDao.insertBatchSomeColumn(collegeList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }



//     ---------删除操作--------------

    /**
     * 根据ID删除物品
     * @param collegeId
     * @return
     */
    public CommonResult deletById(int collegeId) {
        College college = collegeDao.selectById(collegeId);
        if(CommonUtil.isNull(college)){
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }
        int rec = collegeDao.deleteById(collegeId);
        if(rec == 1){
            return CommonResult.success();
        }else
            return CommonResult.error();
    }

//     ---------更新操作--------------

    /**
     * 更新物品
     * @param college
     * @return
     */
    public CommonResult update(College college) {
        /*
        条件构造器作为参数进行更新
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("goods_name","xsc");

            User user = new User();
            user.setAge(18);

            Integer rows = userMapper.update(user, updateWrapper);

         */
        if (CommonUtil.isNull(college)) {
            return CommonResult.error();
        } else{
            College newCollege = collegeDao.selectById(college.getCollegeId());
            if (CommonUtil.isNull(newCollege)) {
                return CommonResult.error(CommonResultEm.NOT_EXIST);
            } else {
                //  暂定 自己编写
                newCollege.setCollegeId(college.getCollegeId())
                        .setCollegeName(college.getCollegeName());
                int rec = collegeDao.updateById(newCollege);
                if (rec == 1)
                    return CommonResult.success();
                else
                    return CommonResult.error();
            }

        }
    }




}
