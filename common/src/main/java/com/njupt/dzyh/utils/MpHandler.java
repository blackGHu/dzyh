package com.njupt.dzyh.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * MybatisPlus 处理器
 * 处理：自动填充
 */
@Component
@Slf4j
public class MpHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
            this.setFieldValByName("createTime",new Date(),metaObject);
            this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("version",1,metaObject);
        this.setFieldValByName("deleted",0,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }

}
