package com.cctv;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@MapperScan("com.cctv.dao")
public class reggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(reggieApplication.class,args);
        log.info("项目启动中....");
    }
}
