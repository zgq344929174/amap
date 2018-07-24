package com.zgq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by z on 2018/6/24.
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
