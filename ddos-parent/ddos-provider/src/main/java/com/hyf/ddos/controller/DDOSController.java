package com.hyf.ddos.controller;

import com.hyf.ddos.concurrent.InstructionFactory;
import com.hyf.ddos.concurrent.DDOSControl;
import com.hyf.ddos.config.DDOSConfig;
import com.hyf.ddos.config.Init;
import com.hyf.ddos.constants.PathConstants;
import com.hyf.ddos.log.Log;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author baB_hyf
 * @date 2021/09/20
 */
@RestController
public class DDOSController {

    @RequestMapping(PathConstants.URL_CONFIG)
    public String config(@RequestParam Map<String, String> params) {
        DDOSConfig.putAll(params); // 更新配置
        boolean persistence = DDOSConfig.persistence(); // 持久化
        Init.reload(); // 重新加载

        Log.info("update config: {}", params);

        return persistence ? "updated" : "failed";
    }

    @RequestMapping(PathConstants.URL_INVOKE)
    public String invoke() {

        DDOSControl.stop();
        DDOSControl.invoke(InstructionFactory.getInstruction());

        Log.info("invoked new runnable");

        return "invoked";
    }
}
