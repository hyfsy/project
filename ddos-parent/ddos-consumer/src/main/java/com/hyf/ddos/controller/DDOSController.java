package com.hyf.ddos.controller;

import com.hyf.ddos.constants.PathConstants;
import com.hyf.ddos.constants.ServiceConstants;
import com.hyf.ddos.service.DDOSService;
import com.hyf.ddos.service.InvokeBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author baB_hyf
 * @date 2021/09/20
 */
@RestController
public class DDOSController {

    @Resource
    private DiscoveryClient discoveryClient;

    @RequestMapping(PathConstants.URL_CONFIG)
    public String config(@RequestParam Map<String, String> params) {
        invokeAll(DDOSService.class, s -> s.config(params));
        return "success";
    }

    @RequestMapping(PathConstants.URL_INVOKE)
    public String invoke() {
        invokeAll(DDOSService.class, DDOSService::invoke);
        return "success";
    }

    private <T> void invokeAll(Class<T> clazz, Consumer<T> consumer) {
        List<ServiceInstance> instanceList = discoveryClient.getInstances(ServiceConstants.PROVIDER_SERVICE_NAME);
        if (instanceList != null) {
            for (ServiceInstance serviceInstance : instanceList) {
                String url = serviceInstance.getUri().toString();
                T serviceBean = InvokeBuilder.build(clazz, url);
                consumer.accept(serviceBean);
            }
        }
    }
}
