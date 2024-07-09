package com.hyf.ssm.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @author baB_hyf
 * @date 2022/04/01
 */
@Configuration
public class CanalConnectorConfiguration {

    private String canalIp     = "localhost";
    private int    canalPort   = 11111;
    private String destination = "example";
    private String canalUserName = "";
    private String canalUserPassword = "";

    @Bean(destroyMethod = "disconnect")
    public CanalConnector canalConnector() {
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(canalIp, canalPort), // canal ip and port
                destination, // canal conf directory name
                canalUserName, // canal instance username
                canalUserPassword // canal instance password
        );

        canalConnector.connect();
        canalConnector.subscribe(".*\\..*");
        // 回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
        canalConnector.rollback();

        return canalConnector;
    }
}
