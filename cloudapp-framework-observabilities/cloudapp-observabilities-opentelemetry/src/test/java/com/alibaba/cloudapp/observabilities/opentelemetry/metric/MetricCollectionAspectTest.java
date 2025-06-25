package com.alibaba.cloudapp.observabilities.opentelemetry.metric;

import com.alibaba.cloudapp.api.observabilities.InvokeCount;
import com.alibaba.cloudapp.api.observabilities.Metric;
import com.alibaba.cloudapp.api.observabilities.MetricCollection;
import com.alibaba.cloudapp.api.observabilities.MetricType;
import io.opentelemetry.api.metrics.ObservableDoubleMeasurement;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MetricCollectionAspectTest {

    private MetricCollectionAspect aspect;

    private void initAspect() {
        GlobalOpenTelemetry openTelemetry = new GlobalOpenTelemetry(
                "http://localhost:9464",
                "default-service",
                60,
                10,
                true,
                9090);
        openTelemetry.build();
        aspect = new MetricCollectionAspect();
    }
    
    @Test
    public void testGetPointClass_targetNotNull() {
        initAspect();
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getTarget()).thenReturn(new Object());
        Assert.assertEquals(aspect.getPointClass(pjp), Object.class);
    }
    
    @Test
    public void testGetPointClass_targetNull() {
        initAspect();
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        Signature sig = mock(Signature.class);
        when(pjp.getTarget()).thenReturn(null);
        when(pjp.getSignature()).thenReturn(sig);
        when(sig.getDeclaringType()).thenReturn(Object.class);
        Assert.assertEquals(aspect.getPointClass(pjp), Object.class);
    }
    
    @Test
    public void testGetMethods() {
        initAspect();
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getTarget()).thenReturn(new Object());
        Assert.assertEquals(aspect.getMethods(pjp), Object.class.getMethods());
    }
    
    @Test
    public void testAop() {
        initAspect();
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getTarget()).thenReturn(new UnitTestMetricCollection());
        
        try {
            when(pjp.proceed()).thenReturn(null);
            aspect.aop(pjp);
        } catch (Throwable e) {
        }
    }
    
    @Test
    public void testInvokeCountHandler() {
        initAspect();
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        UnitTestMetricCollection mockedClass = new UnitTestMetricCollection();
        MethodSignature sig = mock(MethodSignature.class);
        when(pjp.getSignature()).thenReturn(sig);
        when(sig.getName()).thenReturn("invoke");
        when(sig.getParameterTypes()).thenReturn(new Class[]{String.class, String.class});
        when(pjp.getTarget()).thenReturn(mockedClass);
        
        try {
            when(pjp.proceed()).thenReturn(null);
            aspect.invokeCountHandler(pjp);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testFieldHandler() {
        initAspect();
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        UnitTestMetricCollection mockedClass = new UnitTestMetricCollection();
        mockedClass.counter = 1L;
        mockedClass.gauge = 2.0;
        mockedClass.histogram = 3.0;
        mockedClass.asyncCounter = 10L;
        MethodSignature sig = mock(MethodSignature.class);
        when(pjp.getSignature()).thenReturn(sig);
        when(pjp.getTarget()).thenReturn(mockedClass);
        when(pjp.getArgs()).thenReturn(new Object[]{2L});
        try {
            when(pjp.proceed()).thenReturn(null);
            when(sig.getName()).thenReturn("setCounter");
            aspect.aop(pjp);
            aspect.filedHandler(pjp);
            when(sig.getName()).thenReturn("setGauge");
            when(pjp.getArgs()).thenReturn(new Object[]{3.0});
            aspect.filedHandler(pjp);
            when(sig.getName()).thenReturn("setHistogram");
            when(pjp.getArgs()).thenReturn(new Object[]{4.0});
            aspect.filedHandler(pjp);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testProcessGaugeCallBack() {
        initAspect();
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        UnitTestMetricCollection mockedClass = new UnitTestMetricCollection();
        mockedClass.asyncCounter = 1L;
        when(pjp.getTarget()).thenReturn(mockedClass);
        try {
            Field field = mockedClass.getClass().getField("asyncCounter");
            String metricName = "mocked.async.counter";
            ObservableDoubleMeasurement measurement = mock(ObservableDoubleMeasurement.class);
            doNothing().when(measurement).record(1L);
            aspect.processGaugeCallback(pjp, field, metricName, measurement);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    @Data
    @MetricCollection(serviceName = "mocked")
    private static class UnitTestMetricCollection {
        @Metric(name="mocked.counter", attributes="interface=/sync")
        private Long counter;
        
        @Metric(name="mocked.async.counter", attributes="interface=/async", async = true)
        public Long asyncCounter;
        
        @Metric(name="mocked.histogram", type= MetricType.HISTOGRAM)
        private Double histogram;
        
        @Metric(name="mocked.gauge", type=MetricType.GAUGE)
        private Double gauge;
        
        @InvokeCount(name="sample.sync.invoke.counter",
                attributes = "args_1=args[0];args_2=args[1]")
        public void invoke(String tag1, String tag2) {
        }
    }
}
