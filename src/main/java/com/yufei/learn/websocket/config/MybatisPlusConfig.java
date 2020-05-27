package com.yufei.learn.websocket.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yufei.wang
 * @date 2020/05/24 23:49
 */
@Configuration
@MapperScan(basePackages = {"com.yufei.learn.websocket.mapper"})
public class MybatisPlusConfig {

}
