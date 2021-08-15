package com.njupt.dzyh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njupt.dzyh.dao.CategoryDao;
import com.njupt.dzyh.domain.Category;
import com.njupt.dzyh.domain.dto.GenerateExcel;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.CategoryService;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;




//     ---------查询操作--------------
    /**
     * 查询所有物品
     * @return
     */
    public CommonResult selectAll() {
        System.out.println("hello--- ");
        List<Category> categoryList =  categoryDao.selectList(null);
        if(0 == categoryList.size()){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(categoryList);
    }

    /**
     * 根据ID查询物品
     * @param categoryId
     * @return
     */
    public CommonResult selectById(Integer categoryId) {
        Category category = categoryDao.selectById(categoryId);
        if(CommonUtil.isNull(category)){
            return CommonResult.error(CommonResultEm.ERROR);
        }
        return CommonResult.success(category);
    }




    public CommonResult selectByConditions(Map<String, Object> map) {
        List<Category> result = categoryDao.selectByMap(map);
        if(0 == result.size()){
            return CommonResult.error();
        }
        return CommonResult.success(result);
    }


//     ---------插入操作--------------

    /**
     * 物品信息的录入
     * @param category
     * @return
     */
    public CommonResult insert(Category category) {
        if(CommonUtil.isNull(category)){
            return CommonResult.error();
        }else{
            QueryWrapper<Category> wrapper = new QueryWrapper<>();
            wrapper.eq("category_name",category.getCategoryName());
            int count = categoryDao.selectCount(wrapper);
            if(count == 1){
                return CommonResult.error(CommonResultEm.ALREADY_EXIST);
            }else{
                Category newCategory = new Category();
                //  暂定 自己编写
                newCategory.setCategoryName(category.getCategoryName());

                int rec = categoryDao.insert(newCategory);
                if (rec == 1)
                    return CommonResult.success();
                else
                    return CommonResult.error();
            }
        }

    }


    /**
     * 批量插入
     * @param categoryList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult insertBatch(List<Category> categoryList){
        if(0 == categoryList.size()){
            return CommonResult.error();
        }
        Integer rec = categoryDao.insertBatchSomeColumn(categoryList);
        if(rec == 0)
            return CommonResult.error();
        else
            return CommonResult.success();
    }



//     ---------删除操作--------------

    /**
     * 根据ID删除物品
     * @param categoryId
     * @return
     */
    public CommonResult deletById(Integer categoryId) {
        Category category = categoryDao.selectById(categoryId);
        if(CommonUtil.isNull(category)){
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }
        int rec = categoryDao.deleteById(categoryId);
        if(rec == 1){
            return CommonResult.success();
        }else
            return CommonResult.error();
    }

//     ---------更新操作--------------

    /**
     * 更新物品
     * @param category
     * @return
     */
    public CommonResult update(Category category) {
        if (CommonUtil.isNull(category)) {
            return CommonResult.error();
        } else{
            Category newCategory = categoryDao.selectById(category.getCategoryId());
            if (CommonUtil.isNull(newCategory)) {
                return CommonResult.error(CommonResultEm.NOT_EXIST);
            } else {
                //  暂定 自己编写
                newCategory.setCategoryId(category.getCategoryId())
                        .setCategoryName(category.getCategoryName());
                int rec = categoryDao.updateById(newCategory);
                if (rec == 1)
                    return CommonResult.success();
                else
                    return CommonResult.error();
            }

        }
    }


    @Transactional(rollbackFor = Exception.class)
    public CommonResult generateExcel(List<Category> list,String outUrl,String fileName) throws IOException, ParseException {
        // 1、首先根据查询查出所需信息（查询全部、条件查询）
        // 2、然后将结果封装成List<T>后 再封装成GenerateExcel实体
        // 3、调用service.generateExcel（。。。）
        List<Category> goodsList = list;
        System.out.println("[generateExcel]  goodsList---\t" + goodsList);
        // goodsList 中的 createTime 和 updateTime 正常，后面转成JSON就不正常了
        // 需添加注解 @JSONField(format = "yyyy-MM-dd HH:mm:ss")  或者 将数据转换成json对象时，使用JSON.toJSON
        if (0 == goodsList.size()) {
            return CommonResult.error();
        }
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(goodsList));
        System.out.println("[generateExcel]  jsonArray---\t" + jsonArray.toJSONString());

        //        "../excelTest/","test.xls"
        CommonUtil.JsonToExcel(jsonArray, outUrl, fileName);
        return CommonResult.success();
    }



}
