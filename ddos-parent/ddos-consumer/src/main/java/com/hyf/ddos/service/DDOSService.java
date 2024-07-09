package com.hyf.ddos.service;

import com.hyf.ddos.constants.PathConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author baB_hyf
 * @date 2021/09/20
 */
public interface DDOSService {

    @RequestMapping(PathConstants.URL_CONFIG)
    String config(@RequestParam("params") Map<String, String> params);

    @RequestMapping(PathConstants.URL_INVOKE)
    String invoke();
}
