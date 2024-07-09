package com.hyf.ddos;

import com.hyf.ddos.config.DDOSConfig;
import com.hyf.ddos.config.Init;
import com.hyf.ddos.log.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

/**
 * ddos script cannot work
 * <p>
 * recommend to use ddos syn
 *
 * @author baB_hyf
 * @date 2021/09/20
 */
@SpringBootApplication
public class DDOSProviderApplication {

    public static void main(String[] args) {
        // 初始化加载及持久化
        Init.reload();
        DDOSConfig.persistence();
        Properties properties = DDOSConfig.get();
        Log.info("init config: " + properties.toString());
        SpringApplication.run(DDOSProviderApplication.class, args);
    }
}
