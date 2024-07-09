package com.hyf.ssm.sync.direct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyf.ssm.canal.CanalDataHandler;
import com.hyf.ssm.mapper.HelloMapper;
import com.hyf.ssm.pojo.Hello;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author baB_hyf
 * @date 2022/04/01
 */
// @Component
public class HelloDataHandler implements CanalDataHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @Resource
    private HelloMapper helloMapper;

    @Override
    public boolean interest(String schema, String table, String eventType) {
        return "learn".equalsIgnoreCase(schema) && "hello".equalsIgnoreCase(table);
    }

    @Override
    public void process(Map<String, String> before, Map<String, String> after, String eventType) {
        if ("insert".equalsIgnoreCase(eventType)) {
            Hello hello = mapper.convertValue(after, Hello.class);
            helloMapper.add(hello);
        }
        else if ("update".equalsIgnoreCase(eventType)) {
            Hello hello = mapper.convertValue(after, Hello.class);
            helloMapper.update(hello);
        }
        else if ("delete".equalsIgnoreCase(eventType)) {
            Hello hello = mapper.convertValue(before, Hello.class);
            helloMapper.delete(hello.getId());
        }
    }
}
