package com.alibaba.cloudapp.microservice.aliyun.demo;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfig {
    
    @Bean
    public IRule ribbonRule() {
        return new RoundRobinRule();
    }
    
    @Bean
    public IPing ping() {
        PingUrl pingUrl = new PingUrl(false, "/healthCheck");
        pingUrl.setExpectedContent("true");
        return pingUrl;
    }
}
