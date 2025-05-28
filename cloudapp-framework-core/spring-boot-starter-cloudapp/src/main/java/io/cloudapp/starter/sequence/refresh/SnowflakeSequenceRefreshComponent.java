package io.cloudapp.starter.sequence.refresh;

import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.sequence.Constants;
import io.cloudapp.sequence.service.SnowflakeSequenceGenerator;
import io.cloudapp.starter.base.RefreshableComponent;
import io.cloudapp.starter.refresh.RefreshableProxyFactory;
import io.cloudapp.starter.sequence.properties.CloudAppSequenceProperties;

public class SnowflakeSequenceRefreshComponent extends RefreshableComponent<
        CloudAppSequenceProperties, SnowflakeSequenceGenerator> {
    public SnowflakeSequenceRefreshComponent(CloudAppSequenceProperties properties) {
        super(properties);
    }
    
    @Override
    public void postStart() {
        try {
            RefreshableProxyFactory.updateProxyTarget(bean, properties);
        } catch (Exception e) {
            throw new CloudAppException("refresh SnowflakeSequence error!", e);
        }
    }
    
    @Override
    public void preStop() {
    
    }
    
    @Override
    public String bindKey() {
        return Constants.SEQUENCE_CONFIG_SNOWFLAKE_ROOT;
    }
    
    @Override
    public String getName() {
        return "snowflakeSequence";
    }
    
    @Override
    protected SnowflakeSequenceGenerator createBean(CloudAppSequenceProperties properties) {
        return RefreshableProxyFactory.create(
                properties1 -> new SnowflakeSequenceGenerator(
                        properties1.getSnowflake().getWorkerId(),
                        properties1.getSnowflake().getWorkerIdBits(),
                        properties1.getSnowflake().getSequenceBits()
                ),
                properties
        );
    }
    
}
