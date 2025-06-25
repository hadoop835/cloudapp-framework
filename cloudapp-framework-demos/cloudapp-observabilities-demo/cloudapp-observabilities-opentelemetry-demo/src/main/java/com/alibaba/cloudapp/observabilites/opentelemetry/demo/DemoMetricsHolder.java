package com.alibaba.cloudapp.observabilites.opentelemetry.demo;

import com.alibaba.cloudapp.api.observabilities.MetricCollection;
import com.alibaba.cloudapp.api.observabilities.Metric;
import com.alibaba.cloudapp.api.observabilities.MetricType;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@MetricCollection(serviceName = "cloudapp-demo")
public class DemoMetricsHolder {
    @Metric(name="sample.sync.counter", attributes = "interface=/sync")
    public long myCounter;
    
    @Metric(name="sample.async.counter", async = true, attributes =
            "interface=/async")
    public long myAsyncCounter;
    
    @Metric(name="sample.sync.gauge", type = MetricType.GAUGE)
    public double myGauge;
    
    @Metric(name="sample.async.gauge", type = MetricType.GAUGE, async = true)
    public double myAsyncGauge;
    
    @Metric(name="sample.sync.histogram", type = MetricType.HISTOGRAM,
            buckets = {10.0, 50.0, 100.0})
    public double myHistogram;
}
