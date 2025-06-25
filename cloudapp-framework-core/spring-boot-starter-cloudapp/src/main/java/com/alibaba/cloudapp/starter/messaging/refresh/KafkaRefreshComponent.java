package com.alibaba.cloudapp.starter.messaging.refresh;

import com.alibaba.cloudapp.starter.base.RefreshableComponent;
import com.alibaba.cloudapp.starter.messaging.factory.CloudAppKafkaBeanFactory;
import com.alibaba.cloudapp.starter.messaging.properties.CloudAppKafkaProperties;

public class KafkaRefreshComponent extends RefreshableComponent<
        CloudAppKafkaProperties, CloudAppKafkaBeanFactory> {
    
    public KafkaRefreshComponent(CloudAppKafkaProperties properties,
                                 CloudAppKafkaBeanFactory beanFactory) {
        super(properties, beanFactory);
    }
    
    @Override
    public void postStart() {
        bean.refresh(properties);
    }
    
    @Override
    public void preStop() {
    
    }
    
    @Override
    public String bindKey() {
        return CloudAppKafkaProperties.PREFIX;
    }
    
    @Override
    public String getName() {
        return "cloudAppKafka";
    }
    
    
    @Override
    protected CloudAppKafkaBeanFactory createBean(CloudAppKafkaProperties properties) {
        return bean;
    }
    
}
