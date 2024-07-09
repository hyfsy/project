package com.hyf.async.alarm;

import com.hyf.async.core.AsyncTask;
import com.hyf.async.properties.AlarmProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class FeishuAlarm {

    @Autowired
    private AlarmProperties properties;

    public void alarm(AsyncTask task) {
        // curl -X POST -H "Content-Type: application/json" -d '{"msg_type":"text","content":{"text":"request example"}}' https://open.feishu.cn/open-apis/bot/v2/hook/8a50a120-7535-4064-84dd-9accdff8aa81
        String api = properties.getApi();
        String appId = properties.getAppId();
        String appSecret = properties.getAppSecret();
        try {
            Runtime.getRuntime().exec("curl -X POST -H \"Content-Type: application/json\" \\\n"
                + "\t-d '{\"msg_type\":\"text\",\"content\":{\"text\":\"" + task.toString() + "\"}}' \\\n" + api);
        } catch (IOException e) {
            log.error("Feishu async task alarm failed", e);
            throw new RuntimeException(e);
        }
    }
}
