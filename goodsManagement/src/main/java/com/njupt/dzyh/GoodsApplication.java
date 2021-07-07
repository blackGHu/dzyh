package com.njupt.dzyh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 一句话功能描述.
 * 项目名称:  物品管理启动类
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.njupt")
//@ComponentScan(basePackages = "com.njupt.dzyh")
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }
}
