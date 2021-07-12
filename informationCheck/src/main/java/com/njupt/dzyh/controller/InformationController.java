package com.njupt.dzyh.controller;


import com.njupt.dzyh.dao.InformationDao;
import com.njupt.dzyh.domain.Goods;
import com.njupt.dzyh.domain.Information;
import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.service.InformationService;
import com.njupt.dzyh.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    /**
     * 查询库存信息
     * @return
     */
    @RequestMapping("/selectByCondition")
    public CommonResult selectByCondition(@RequestParam("name") String name, @RequestParam("size") String size, @RequestParam("model") String model){
        List<Information> informationList = informationService.selectByCondition(name,size,model);
        if(informationList.size()==0) return CommonResult.error(CommonResultEm.ERROR);

        return CommonResult.success(informationList);
    }


}
