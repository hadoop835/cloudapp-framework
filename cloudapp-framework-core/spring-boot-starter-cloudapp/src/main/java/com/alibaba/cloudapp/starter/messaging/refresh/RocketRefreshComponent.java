package com.alibaba.cloudapp.starter.messaging.refresh;

import com.alibaba.cloudapp.starter.base.RefreshableComponent;
import com.alibaba.cloudapp.starter.messaging.factory.CloudAppRocketBeanFactory;
import com.alibaba.cloudapp.starter.messaging.properties.CloudAppRocketProperties;

public class RocketRefreshComponent extends RefreshableComponent<
        CloudAppRocketProperties, CloudAppRocketBeanFactory> {
    
    public RocketRefreshComponent(CloudAppRocketProperties properties,
                                  CloudAppRocketBeanFactory beanFactory) {
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
        return CloudAppRocketProperties.PREFIX;
    }
    
    @Override
    public String getName() {
        return "cloudAppRocket";
    }
    
    @Override
    protected CloudAppRocketBeanFactory createBean(CloudAppRocketProperties properties) {
        return bean;
    }
    
}
