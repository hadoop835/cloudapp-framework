package io.cloudapp.starter.sequence.refresh;

import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.sequence.Constants;
import io.cloudapp.starter.base.RefreshableComponent;
import io.cloudapp.starter.redis.RedisConnectionFactoryBuilder;
import io.cloudapp.starter.redis.connection.ConnectionFactoryUtil;
import io.cloudapp.starter.refresh.RefreshableProxyFactory;
import io.cloudapp.starter.sequence.properties.CloudAppSequenceProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;

public class RedisSequenceRefreshComponent extends RefreshableComponent<
        CloudAppSequenceProperties, RedisConnectionFactory> {
    
    public RedisSequenceRefreshComponent(CloudAppSequenceProperties properties) {
        super(properties);
    }
    
    @Override
    public void postStart() {
        try {
            RefreshableProxyFactory.updateProxyTarget(
                    bean,
                    properties
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void preStop() {
    
    }
    
    @Override
    public String bindKey() {
        return Constants.SEQUENCE_CONFIG_REDIS_ROOT;
    }
    
    @Override
    public String getName() {
        return "sequenceRedisConnection";
    }
    
    @Override
    protected RedisConnectionFactory createBean(
            CloudAppSequenceProperties properties
    ) {
        return RefreshableProxyFactory.create(
                this::getConnectionFactory,
                properties
        );
    }
    
    private RedisConnectionFactory getConnectionFactory(
            CloudAppSequenceProperties properties
    ) {
        try {
            RedisConnectionFactoryBuilder builder = ConnectionFactoryUtil
                    .getRedisConnectionFactoryBuilder(properties.getRedis());
            builder.afterPropertiesSet();
            return builder.getConnectionFactory();
        } catch (Exception e) {
            throw new CloudAppException("init redis connection error!", e);
        }
    }
}
