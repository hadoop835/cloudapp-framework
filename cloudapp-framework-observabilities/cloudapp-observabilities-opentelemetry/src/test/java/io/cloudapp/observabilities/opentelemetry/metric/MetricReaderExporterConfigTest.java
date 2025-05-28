package io.cloudapp.observabilities.opentelemetry.metric;

import io.opentelemetry.exporter.prometheus.PrometheusHttpServer;
import io.opentelemetry.exporter.prometheus.PrometheusHttpServerBuilder;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetricReaderExporterConfigTest {
    
    public GlobalOpenTelemetry initOtel() {
        GlobalOpenTelemetry openTelemetry = new GlobalOpenTelemetry(
                "http://localhost:9464",
                "default-service",
                60,
                10,
                true,
                9090);
        openTelemetry.build();
        return openTelemetry;
    }
    
    @Test
    public void testOtlpHttpMetricExporter() {
        initOtel();
        assertDoesNotThrow(() -> MetricReaderExporterConfig.otlpHttpMetricExporter(
                "http://localhost:4318", 5));
    }
    
    @Test
    public void testPeriodicMetricReader() {
        initOtel();
        assertDoesNotThrow(() -> MetricReaderExporterConfig.periodicMetricReader(
                "http://localhost:4318", 300, 5));
    }
    
    @Test
    public void testPrometheusMetricReader() {
        initOtel();
        try (MockedStatic<PrometheusHttpServer> mockedStatic =
                     mockStatic(PrometheusHttpServer.class)) {
            PrometheusHttpServerBuilder builder =
                    mock(PrometheusHttpServerBuilder.class);
            mockedStatic.when(() -> PrometheusHttpServer.builder()).thenReturn(builder);
            when(builder.setHost(anyString())).thenReturn(builder);
            when(builder.setPort(anyInt())).thenReturn(builder);
            when(builder.setOtelScopeEnabled(anyBoolean())).thenReturn(builder);
            when(builder.setAllowedResourceAttributesFilter(any())).thenReturn(builder);
            when(builder.build()).thenReturn(null);
            assertDoesNotThrow(
                    () -> MetricReaderExporterConfig.prometheusMetricReader(
                            "0.0.0.0", 9090));
        }
    }
}
