package com.hanfu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hanfu.mapper")
public class HanfuApplication {
    public static void main(String[] args) {
        SpringApplication.run(HanfuApplication.class, args);
        System.out.println("==================================");
        System.out.println("  汉服租赁系统启动成功！");
        System.out.println("  后端地址: http://localhost:8080");
        System.out.println("==================================");
    }
}
