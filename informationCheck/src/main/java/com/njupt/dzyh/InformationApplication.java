package com.njupt.dzyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.njupt")
public class InformationApplication {
    public static void main(String[] args) {
        SpringApplication.run(InformationApplication.class,args);
    }
}
