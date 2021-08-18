package com.njupt.dzyh.controller;


import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.Information;
import com.njupt.dzyh.domain.SelectResult;
import com.njupt.dzyh.domain.UnderStock;
import com.njupt.dzyh.enums.CommonResultEm;

import com.njupt.dzyh.otherFunctions.DownLoad;

import com.njupt.dzyh.otherFunctions.ListToExcelInfo;
import com.njupt.dzyh.service.InformationService;
import com.njupt.dzyh.service.UnderStockService;
import com.njupt.dzyh.utils.CommonResult;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class InformationController {

    @Autowired
    private InformationService informationService;

    @Autowired
    private UnderStockService underStockService;


    @Value("${resource}")
    String resource;

    /**
     * test
     * @return
     *
     * 本段controller没用，仅做测试
     */
    @RequestMapping("/testAdd")
    public CommonResult testAdd(@RequestBody Goods goods){

        return informationService.add(goods);
    }

    /**
     * test
     * @return
     *
     * 本段controller没用，仅做测试
     */
    @RequestMapping("/testSub")
    public CommonResult testSub(@RequestBody Goods goods){

        return informationService.subtract(goods);
    }






    /**
     * 按条件查询库存信息
     * @return
     */
    //@RequiresPermissions("selectByCondition")

    @RequestMapping("/selectByCondition/{current}/{size}")
    public CommonResult selectByCondition(@PathVariable("current") int current,@PathVariable("size") int pageSize,@RequestBody Information con){

        return informationService.selectByCondition(con,current,pageSize);
    }

    /**
     * 分页查询库存信息
     * @return
     */
    @RequestMapping("/selectByPage/{current}/{size}")
    public CommonResult selectByPage(@PathVariable("current") int current,@PathVariable("size") int pageSize) {

        return informationService.selectAllInfoByPage(current, pageSize);
    }


    /**
     * 返回库存不足记录
     * @return
     */
    @RequestMapping("/getUnderStockByCondition/{current}/{size}")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    //@RequiresPermissions("getUnderStockByCondition")
    public CommonResult getUnderStockByCondition(@RequestBody UnderStock underStock,@PathVariable("current") int current,@PathVariable("size") int pageSize) {
        SelectResult selectResult = underStockService.getByCondition(underStock,current,pageSize);
        List<UnderStock> list = (List<UnderStock>) selectResult.getList();
        if(list.size()>0)
            return CommonResult.success(new SelectResult(selectResult.getTotal(),list));
        else
            return CommonResult.error(CommonResultEm.ERROR,"未查询到记录");
    }

    /**
     * 已读库存不足记录,重点是返回记录的Id
     * @return
     */
    @RequestMapping("/readStatus")
    //@RequiresPermissions("readStatus")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
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
    //@RequiresPermissions("readAllStatus")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult readAllStatus() {
        System.out.println(resource);
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
    //@RequiresPermissions("underStockFile")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public void underStockFile(@RequestBody UnderStock underStock, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<UnderStock> list = underStockService.getByCondition(underStock);
        if(list!=null){
            ListToExcelInfo.underStockToExcel(resource,list);
            DownLoad.downloadFile(resource,"underStock.xlsx",request,response);
        }

    }

    @RequestMapping("/deleteUnderStock")
    //@RequiresPermissions("deleteUnderStock")
    @RequiresRoles(value = {"超级管理员"}, logical = Logical.OR)
    public CommonResult deleteUnderStock(@RequestBody UnderStock underStock){
        int rec = underStockService.delete(underStock);
        if(rec == 1){
            return CommonResult.error(CommonResultEm.ERROR,"记录不存在");
        }
        else if(rec==-1)
            return CommonResult.error(CommonResultEm.ERROR,"删除失败");
        else
            return CommonResult.success("删除成功");
    }

    @RequestMapping("/selectAllByCondition/{current}/{size}")
    //@RequiresPermissions("selectAllByCondition")
    @RequiresRoles(value = {"学生","教师","管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult selectAllByCondition(@RequestBody Information information,@PathVariable("current") int current,@PathVariable("size") int pageSize){
        return informationService.selectAllByCondition(information,current,pageSize);
    }

    @RequestMapping("/updateInformation")
    //@RequiresPermissions("updateInformation")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public CommonResult updateInformation(@RequestBody Information information){
        //根据id去查
        return informationService.updateInformation(information);
    }

    @RequestMapping("/deleteInformationById")
    //@RequiresPermissions("deleteInformation")
    @RequiresRoles(value = {"超级管理员"}, logical = Logical.OR)
    public CommonResult deleteInformation(@RequestBody Information information){
        return informationService.deleteInformationById(information);
    }

    @RequestMapping("/informationFile")
    //@RequiresPermissions("informationFile")
    @RequiresRoles(value = {"管理员","超级管理员"}, logical = Logical.OR)
    public void informationFile(@RequestBody Information information,HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Information> list = informationService.selectAllByCondition(information);
        if(list!=null){
            ListToExcelInfo.informationToExcel(resource,list);
            DownLoad.downloadFile(resource,"information.xlsx",request,response);
        }
    }


}
