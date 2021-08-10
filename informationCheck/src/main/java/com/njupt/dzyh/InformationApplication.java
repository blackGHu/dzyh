package com.njupt.dzyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = "com.njupt")
@EnableTransactionManagement(proxyTargetClass = true)
public class InformationApplication {
    public static void main(String[] args) {
        SpringApplication.run(InformationApplication.class,args);
    }
}
