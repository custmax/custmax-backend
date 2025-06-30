package com.custmax.officialsite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.custmax.officialsite.mapper")
public class OfficialsiteApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfficialsiteApplication.class, args);
    }
}
