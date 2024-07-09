package com.hyf.async.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Data
@Component
@ConfigurationProperties("feishu.async-task.alarm")
public class AlarmProperties {

    private String api;
    private String appId;
    private String appSecret;
}
