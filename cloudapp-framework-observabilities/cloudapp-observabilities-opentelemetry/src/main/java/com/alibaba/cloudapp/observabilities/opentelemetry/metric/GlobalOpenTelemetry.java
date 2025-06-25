package com.alibaba.cloudapp.observabilities.opentelemetry.metric;

import com.alibaba.cloudapp.util.NetUtil;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.internal.StringUtils;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.resources.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GlobalOpenTelemetry {
    
    // metric collections' exporter endpoint
    private final String metricEndpoint;
    private final Integer interval;
    private final Integer timeout;
    
    /**
     * application default service name, which will be attached to metric
     * as an attribute
     */
    private final String defaultServiceName;
    public static String SERVICE_NAME;
    
    private Boolean prometheusExporter;
    private Integer prometheusPort;
    
    public static OpenTelemetry openTelemetry;
    
    public static String HOST_IP_ATTRIBUTE_KEY = "host_ip";
    
    public static OpenTelemetry getOpenTelemetry() {
        if (openTelemetry == null) {
            throw new RuntimeException("OpenTelemetry is not initialized");
        }
        return openTelemetry;
    }
    
    public GlobalOpenTelemetry(String metricEndpoint,
                               String defaultServiceName,
                               Integer interval,
                               Integer timeout,
                               Boolean prometheusExporter,
                               Integer prometheusPort) {
        this.metricEndpoint = metricEndpoint;
        this.defaultServiceName = defaultServiceName;
        SERVICE_NAME = defaultServiceName;
        this.interval = interval;
        this.timeout = timeout;
        this.prometheusExporter = prometheusExporter;
        this.prometheusPort = prometheusPort;
    }
    
    public void build() {
        // if already initialized, do nothing
        if (openTelemetry != null) {
            return;
        }
        if (StringUtils.isNullOrEmpty(metricEndpoint) && !prometheusExporter) {
            log.error("Exporter config not set, metricEndpoint need set or " +
                              "prometheusExporter need enabled");
            return;
        }
        
        log.info("Initializing global openTelemetry.");
        try {
            Resource resource = Resource
                    .getDefault().toBuilder()
                    .put(HOST_IP_ATTRIBUTE_KEY, NetUtil.LOCAL_IP)
                    .build();
            
            SdkMeterProviderBuilder sdkMeterProviderBuilder = SdkMeterProvider
                    .builder()
                    .setResource(resource);
            // register default period metric exporter
            if (!StringUtils.isNullOrEmpty(metricEndpoint)) {
                log.info("OpenTelemetry period exporter start, endpoint: {}",
                         metricEndpoint);
                sdkMeterProviderBuilder.registerMetricReader(
                        MetricReaderExporterConfig.periodicMetricReader(
                                metricEndpoint, timeout, interval));
            }
            // register prometheus metric exporter if enabled
            if (prometheusExporter) {
                log.info("Prometheus metrics exporter listen at [:{}]", prometheusPort);
                sdkMeterProviderBuilder.registerMetricReader(
                        MetricReaderExporterConfig.prometheusMetricReader(
                                "0.0.0.0", prometheusPort));
            }
            openTelemetry = OpenTelemetrySdk.builder()
                                            .setMeterProvider(
                                                    sdkMeterProviderBuilder.build())
                                            .build();
        } catch (Exception e) {
            log.error("Failed to initialize OpenTelemetry", e);
        }
        log.info("Finish initialized global openTelemetry.");
    }
}
