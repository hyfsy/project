package com.hyf.ssm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hyf.ssm.mapper")
public class ElasticSyncSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSyncSpringbootApplication.class, args);
    }

}
