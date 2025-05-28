package io.cloudapp.starter.sequence.properties;

import io.cloudapp.sequence.Constants;
import io.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@ConfigurationProperties(prefix = Constants.SEQUENCE_CONFIG_ROOT,
        ignoreInvalidFields = true)
public class CloudAppSequenceProperties extends RefreshableProperties {
    
    private SnowflakeProperties snowflake;
    private RedisSequenceProperties redis;
    
    public SnowflakeProperties getSnowflake() {
        return snowflake;
    }
    
    public void setSnowflake(SnowflakeProperties snowflake) {
        this.snowflake = snowflake;
    }
    
    public RedisSequenceProperties getRedis() {
        return redis;
    }
    
    public void setRedis(RedisSequenceProperties redis) {
        this.redis = redis;
    }
    
    @PostConstruct
    public void afterConstruct()
    {
        if (getRefreshable() != null && ! getRefreshable()) {
            // disabled.
            return;
        }
        
        publishEvent(Constants.SEQUENCE_CONFIG_REDIS_ROOT, this);
        publishEvent(Constants.SEQUENCE_CONFIG_SNOWFLAKE_ROOT, this);
    }
    
}
