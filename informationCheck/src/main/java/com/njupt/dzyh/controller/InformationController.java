package com.njupt.dzyh.controller;


import com.njupt.dzyh.dao.InformationDao;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.Information;
import com.njupt.dzyh.domain.UnderStock;
import com.njupt.dzyh.enums.CommonResultEm;

import com.njupt.dzyh.otherFunctions.DownLoad;
import com.njupt.dzyh.otherFunctions.ListToExcel;
import com.njupt.dzyh.service.InformationService;
import com.njupt.dzyh.service.impl.UnderStockServiceImpl;
import com.njupt.dzyh.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   tjk
 * 创建时间:
 */
@RestController
@RequestMapping("dzyh/information")
@Transactional
public class InformationController {

    @Autowired
    private InformationService informationService;

    @Autowired
    private UnderStockServiceImpl underStockService;


    @Value("${resource}")
    private String resource;

    /**
     * test
     * @return
     *
     * 本段controller没用，仅做测试
     */
    @RequestMapping("/testAdd")
    public CommonResult testAdd(@RequestParam("model") String model,@RequestParam("type") int type,@RequestParam("number") int number,@RequestParam("name") String name,@RequestParam("size") String size){
        Goods goods = new Goods();
        goods.setGoodsName(name)
                .setGoodsSize(size)
                .setGoodsModel(model)
                .setGoodsNumbers(number)
                .setCategoryId(type);
        return informationService.add(goods);
    }

    /**
     * test
     * @return
     *
     * 本段controller没用，仅做测试
     */
    @RequestMapping("/testSub")
    public CommonResult testSub(@RequestParam("model") String model,@RequestParam("type") int type,@RequestParam("number") int number,@RequestParam("name") String name,@RequestParam("size") String size,@RequestParam("userId") String userId){
        Goods goods = new Goods();
        String msg;
        goods.setGoodsName(name)
                .setGoodsSize(size)
                .setGoodsModel(model)
                .setGoodsNumbers(number)
                .setUserId(userId)
                .setCategoryId(type);

        return informationService.subtract(goods);
    }






    /**
     * 按条件查询库存信息
     * @return
     */
    @RequestMapping("/selectByCondition/{current}/{size}")
    public CommonResult selectByCondition(@PathVariable("current") int current,@PathVariable("size") int pageSize,@RequestBody Information con){

        return informationService.selectByCondition(con,current,pageSize+1);
    }

    /**
     * 分页查询库存信息
     * @return
     */
    @RequestMapping("/selectByPage/{current}/{size}")
    public CommonResult selectByPage(@PathVariable("current") int current,@PathVariable("size") int pageSize) {

        return informationService.selectAllInfoByPage(current, pageSize+1);
    }


    /**
     * 返回库存不足记录
     * @return
     */
    @RequestMapping("/getUnderStockByCondition/{current}/{size}")
    public CommonResult getUnderStockByCondition(@RequestBody UnderStock underStock,@PathVariable("current") int current,@PathVariable("size") int pageSize) {
        List<UnderStock> list = underStockService.getByCondition(underStock,current,pageSize+1);
        if(list.size()>0)
            return CommonResult.success(list);
        else
            return CommonResult.error(CommonResultEm.ERROR,"未查询到记录");
    }

    /**
     * 已读库存不足记录,重点是返回记录的Id
     * @return
     */
    @RequestMapping("/readStatus")
    public CommonResult readStatus(@RequestBody UnderStock underStock) {
        if(underStock.getUsId()==0) return CommonResult.error(CommonResultEm.ERROR,"记录Id为空");
        int rec = underStockService.setStatus(underStock);
        if(rec==0)
            return CommonResult.success("已读成功");
        else
            return CommonResult.error(CommonResultEm.ERROR,"已读失败");
    }

    /**
     * 一键已读库存不足记录
     * @return
     */
    @RequestMapping("/readAllStatus")
    public CommonResult readAllStatus() {

        int rec = underStockService.setAllStatus();
        if(rec==0)
            return CommonResult.success("一键已读成功");
        else
            return CommonResult.error(CommonResultEm.ERROR,"一键已读失败");
    }

    /**
     * 导出库存不足记录
     * @return
     */
    @RequestMapping("/underStockFile")
    public void underStockFile(@RequestBody UnderStock underStock, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<UnderStock> list = underStockService.getByCondition(underStock);
        if(list!=null){
            ListToExcel.underStockToExcel(resource,list);
            DownLoad.downloadFile(resource,"underStock.xlsx",request,response);
        }

    }

    @RequestMapping("/deleteUnderStock")
    public CommonResult deleteUnderStock(@RequestBody UnderStock underStock){
        int rec = underStockService.delete(underStock);
        if(rec == 1){
            return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        }
        else if(rec==-1)
            return CommonResult.error(CommonResultEm.ERROR,"删除失败");
        else
            return CommonResult.success("删除成功");
    }


}
