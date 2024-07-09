package com.hyf.ddos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author baB_hyf
 * @date 2021/09/20
 */
@SpringBootApplication
public class DDOSConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DDOSConsumerApplication.class, args);
    }
}
