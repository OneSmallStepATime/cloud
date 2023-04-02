package com.lisihong.config;


import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;

@ConfigurationProperties(prefix = "canal")
public record SyncConfig(String host, Integer port, String destination
        , String subscribe) {
    @Bean
    public CanalConnector canalConnector() {
        CanalConnector connector
                = CanalConnectors.newSingleConnector(
                new InetSocketAddress(host, port)
                , destination, "admin", "admin");
        try {
            connector.connect();
            connector.subscribe(subscribe);
            connector.rollback();
            //connector.ack(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connector;
    }
}