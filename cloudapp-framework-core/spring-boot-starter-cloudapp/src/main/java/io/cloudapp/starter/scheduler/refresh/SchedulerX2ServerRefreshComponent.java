package io.cloudapp.starter.scheduler.refresh;

import  com.aliyun.schedulerx220190430.Client;
import com.aliyun.teaopenapi.models.Config;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.starter.base.RefreshableComponent;
import io.cloudapp.starter.refresh.RefreshableProxyFactory;
import io.cloudapp.starter.scheduler.properties.SchedulerX2ServerProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

public class SchedulerX2ServerRefreshComponent extends RefreshableComponent<
        SchedulerX2ServerProperties, Client> {
    
    public SchedulerX2ServerRefreshComponent(SchedulerX2ServerProperties properties) {
        super(properties);
    }
    
    @Override
    public void postStart() {
        try{
            RefreshableProxyFactory.updateProxyTarget(bean, properties);
        } catch (Exception e) {
            throw new CloudAppException("refresh SchedulerX2 error!", e);
        }
    }
    
    @Override
    public void preStop() {
    
    }
    
    @Override
    public String bindKey() {
        return SchedulerX2ServerProperties.PREFIX;
    }
    
    @Override
    public String getName() {
        return "cloudAppSchedulerX2ServerClient";
    }
    
    @Override
    protected Client createBean(SchedulerX2ServerProperties properties) {
        return RefreshableProxyFactory.create(this::createClient, properties);
    }
    
    @NotNull
    private Client createClient(SchedulerX2ServerProperties properties) {
        try {
            Assert.hasText(properties.getEndpoint(),
                           "SchedulerX2 endpoint must be provided"
            );
            Assert.hasText(properties.getRegionId(),
                           "SchedulerX2 regionId must be provided"
            );
            Assert.hasText(properties.getAccessKeyId(),
                           "SchedulerX2 AccessKeyId must be provided"
            );
            Assert.hasText(properties.getAccessKeySecret(),
                           "SchedulerX2 AccessKeySecret must be provided"
            );
            
            Config config = new Config()
                    .setEndpoint(properties.getEndpoint())
                    .setRegionId(properties.getRegionId())
                    .setAccessKeyId(properties.getAccessKeyId())
                    .setAccessKeySecret(properties.getAccessKeySecret());
            
            return new Client(config);
        } catch (Exception e) {
            throw new CloudAppException("Failed to create SchedulerX2 client", e);
        }
    }
    
}
