package com.alibaba.cloudapp.starter.redis.refresh;

import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.starter.base.RefreshableComponent;
import com.alibaba.cloudapp.starter.redis.RedisConnectionFactoryBuilder;
import com.alibaba.cloudapp.starter.redis.connection.ConnectionFactoryUtil;
import com.alibaba.cloudapp.starter.redis.properties.CloudAppRedisProperties;
import com.alibaba.cloudapp.starter.refresh.RefreshableProxyFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

public class RedisRefreshableComponent extends RefreshableComponent<
        CloudAppRedisProperties, RedisConnectionFactory> {
    
    @Override
    public void postStart() {
        try{
            RefreshableProxyFactory.updateProxyTarget(bean, properties);
        } catch (Exception e) {
            throw new CloudAppException("refresh Redis error!", e);
        }
    }
    
    @Override
    public void preStop() {
    
    }
    
    @Override
    public String bindKey() {
        return CloudAppRedisProperties.PREFIX;
    }
    
    public RedisRefreshableComponent(CloudAppRedisProperties properties) {
        super(properties);
    }
    
    @Override
    public String getName() {
        return "cloudAppRedis";
    }
    
    @Override
    protected RedisConnectionFactory createBean(CloudAppRedisProperties properties) {
        return RefreshableProxyFactory.create(
                this::createFactory, properties
        );
    }
    
    public RedisConnectionFactory createFactory(CloudAppRedisProperties properties) {
        try {
            RedisConnectionFactoryBuilder builder = ConnectionFactoryUtil
                    .getRedisConnectionFactoryBuilder(properties.getBase());
            builder.afterPropertiesSet();
            return builder.getConnectionFactory();
        } catch (Exception e) {
            throw new CloudAppException("init redis connection error!", e);
        }
    }
    
}
