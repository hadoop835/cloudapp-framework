package io.cloudapp.observabilities.opentelemetry.metric;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import io.opentelemetry.api.metrics.DoubleGauge;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MetricsTemplateHandlerTest {

    private MetricsTemplateHandler handler;
    public void setup() {
        handler = new MetricsTemplateHandler();
        GlobalOpenTelemetry.SERVICE_NAME = "mocked-service";
        MetricCollectionAspect.Metrics.put("mock.counter",
                                           mock(LongCounter.class));
        MetricCollectionAspect.Metrics.put("mock.gauge",
                                           mock(DoubleGauge.class));
        MetricCollectionAspect.Metrics.put("mock.histogram",
                                           mock(DoubleHistogram.class));
    }
    
    @Test
    public void testHandle() {
        setup();
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = mock(Headers.class);
        when(exchange.getResponseHeaders()).thenReturn( headers);
        try {
            doNothing().when(exchange).sendResponseHeaders(anyInt(), anyLong());
            OutputStream response = mock(OutputStream.class);
            when(exchange.getResponseBody()).thenReturn(response);
            doNothing().when(response).write(any());
            doNothing().when(response).close();
            assertDoesNotThrow(() -> handler.handle(exchange));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
