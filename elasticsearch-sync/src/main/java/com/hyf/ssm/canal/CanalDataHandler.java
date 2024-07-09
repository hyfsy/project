package com.hyf.ssm.canal;

import java.util.Map;

/**
 * @author baB_hyf
 * @date 2022/04/01
 */
public interface CanalDataHandler {

    boolean interest(String schema, String table, String eventType);

    void process(Map<String, String> before, Map<String, String> after, String eventType);
}
