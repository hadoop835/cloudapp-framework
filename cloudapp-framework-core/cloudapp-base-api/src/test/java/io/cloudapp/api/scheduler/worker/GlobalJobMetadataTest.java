package io.cloudapp.api.scheduler.worker;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GlobalJobMetadataTest {
    
    private GlobalJobMetadata jobMetadata;
    
    @Before
    public void setUp() throws Exception {
        jobMetadata = new GlobalJobMetadata();
    }
    
    @Test
    public void testBeanNameGetterAndSetter() {
        final String beanName = "beanName";
        jobMetadata.setBeanName(beanName);
        assertEquals(beanName, jobMetadata.getBeanName());
    }
    
    @Test
    public void testValueNameGetterAndSetter() {
        final String valueName = "valueName";
        jobMetadata.setValueName(valueName);
        assertEquals(valueName, jobMetadata.getValueName());
    }
    
    @Test
    public void testProcessMethodGetterAndSetter() {
        final Method processMethod = null;
        jobMetadata.setProcessMethod(processMethod);
        assertEquals(processMethod,
                     jobMetadata.getProcessMethod()
        );
    }
    
    @Test
    public void testPreProcessMethodGetterAndSetter() {
        final Method preProcessMethod = null;
        jobMetadata.setPreProcessMethod(preProcessMethod);
        assertEquals(preProcessMethod,
                     jobMetadata.getPreProcessMethod()
        );
    }
    
    @Test
    public void testPostProcessMethodGetterAndSetter() {
        final Method postProcessMethod = null;
        jobMetadata.setPostProcessMethod(postProcessMethod);
        assertEquals(postProcessMethod,
                     jobMetadata.getPostProcessMethod()
        );
    }
    
    @Test
    public void testCronGetterAndSetter() {
        final String cron = "cron";
        jobMetadata.setCron(cron);
        assertEquals(cron, jobMetadata.getCron());
    }
    
    @Test
    public void testFixedDelayGetterAndSetter() {
        final Long fixedDelay = 0L;
        jobMetadata.setFixedDelay(fixedDelay);
        assertEquals(fixedDelay, jobMetadata.getFixedDelay());
    }
    
    @Test
    public void testFixedRateGetterAndSetter() {
        final Long fixedRate = 0L;
        jobMetadata.setFixedRate(fixedRate);
        assertEquals(fixedRate, jobMetadata.getFixedRate());
    }
    
    @Test
    public void testExecuteModeGetterAndSetter() {
        final String executeMode = "executeMode";
        jobMetadata.setExecuteMode(executeMode);
        assertEquals(executeMode, jobMetadata.getExecuteMode());
    }
    
    @Test
    public void testDescriptionGetterAndSetter() {
        final String description = "description";
        jobMetadata.setDescription(description);
        assertEquals(description, jobMetadata.getDescription());
    }
    
    @Test
    public void testAutoCreateJobGetterAndSetter() {
        final boolean autoCreateJob = false;
        jobMetadata.setAutoCreateJob(autoCreateJob);
        assertFalse(jobMetadata.isAutoCreateJob());
    }
    
    @Test
    public void testAutoDeleteJobGetterAndSetter() {
        final boolean autoDeleteJob = false;
        jobMetadata.setAutoDeleteJob(autoDeleteJob);
        assertFalse(jobMetadata.isAutoDeleteJob());
    }
    
    @Test
    public void testShardingParamsGetterAndSetter() {
        final String shardingParams = "shardingParams";
        jobMetadata.setShardingParams(shardingParams);
        assertEquals(shardingParams,
                     jobMetadata.getShardingParams()
        );
    }
    
}
