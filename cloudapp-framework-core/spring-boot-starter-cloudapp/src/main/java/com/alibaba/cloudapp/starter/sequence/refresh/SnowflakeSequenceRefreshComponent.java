package com.alibaba.cloudapp.starter.sequence.refresh;

import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.sequence.Constants;
import com.alibaba.cloudapp.sequence.service.SnowflakeSequenceGenerator;
import com.alibaba.cloudapp.starter.base.RefreshableComponent;
import com.alibaba.cloudapp.starter.refresh.RefreshableProxyFactory;
import com.alibaba.cloudapp.starter.sequence.properties.CloudAppSequenceProperties;

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
