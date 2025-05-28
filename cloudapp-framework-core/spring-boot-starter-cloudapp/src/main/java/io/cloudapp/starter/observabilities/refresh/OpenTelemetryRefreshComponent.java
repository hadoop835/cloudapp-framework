package io.cloudapp.starter.observabilities.refresh;

import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.observabilities.opentelemetry.metric.GlobalOpenTelemetry;
import io.cloudapp.observabilities.opentelemetry.metric.MetricHelperServer;
import io.cloudapp.starter.base.RefreshableComponent;
import io.cloudapp.starter.observabilities.properties.OpenTelemetryProperties;
import io.cloudapp.starter.refresh.RefreshableProxyFactory;

public class OpenTelemetryRefreshComponent extends RefreshableComponent<
        OpenTelemetryProperties, GlobalOpenTelemetry> {
    
    private MetricHelperServer server;
    
    public OpenTelemetryRefreshComponent(OpenTelemetryProperties properties) {
        super(properties);
    }
    
    @Override
    public void postStart() {
        try {
            RefreshableProxyFactory.updateProxyTarget(bean, properties);
            RefreshableProxyFactory.updateProxyTarget(server, properties);
        } catch (Exception e) {
            throw new CloudAppException("refresh OpenTelemetry error!", e);
        }
    }
    
    @Override
    public void preStop() {
    }
    
    @Override
    public String bindKey() {
        return OpenTelemetryProperties.PREFIX;
    }
    
    @Override
    public String getName() {
        return "globalOpenTelemetry";
    }
    
    @Override
    protected GlobalOpenTelemetry createBean(OpenTelemetryProperties properties) {
        server = RefreshableProxyFactory.create(this::createServer, properties);
        return RefreshableProxyFactory.create(this::createOpenTelemetry, properties);
    }
    
    public MetricHelperServer createServer(OpenTelemetryProperties properties) {
        MetricHelperServer metricServer = new MetricHelperServer(
                properties.isEnabledHelperServer(),
                properties.getHelperServerBindAddress(),
                properties.getHelperServerPort()
        );
        metricServer.start();
        return metricServer;
    }
    
    public GlobalOpenTelemetry createOpenTelemetry(OpenTelemetryProperties properties) {
        GlobalOpenTelemetry otel = new GlobalOpenTelemetry(
                properties.getMetricEndpoint(),
                properties.getDefaultServiceName(),
                properties.getInterval(),
                properties.getTimeout(),
                properties.isPrometheusExporter(),
                properties.getPrometheusPort()
        );
        otel.build();
        return otel;
    }
    
    public MetricHelperServer getServer() {
        return server;
    }
    
}
