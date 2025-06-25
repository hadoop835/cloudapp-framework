package com.alibaba.cloudapp.observabilities.opentelemetry.metric;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetricHelperServerTest {
    
    private MetricHelperServer server;
    
    @BeforeEach
    public void setup() {
        server = new MetricHelperServer(true, "localhost", 8080);
    }
    
    @Test
    public void testStartServer() {
        try (MockedStatic<HttpServer> mockedStatic =
                     mockStatic(HttpServer.class)) {
            HttpServer httpServer = mock(HttpServer.class);
            mockedStatic.when(() -> HttpServer.create(any(), anyInt()))
                        .thenReturn(httpServer);
            when(httpServer.createContext(anyString(), any())).thenReturn(null);
            doNothing().when(httpServer).start();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
