
package com.alibaba.cloudapp.observabilities.opentelemetry.logging;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OpenTelemetryLog4J2ImplTest {
    
    private LoggerContext loggerContext;
    
    private OpenTelemetryLog4J2Impl openTelemetryLog4J2Impl;

    @Test
    public void testUpdateLoggerEnabled() {
        loggerContext = new LoggerContext("mocked");
        openTelemetryLog4J2Impl = new OpenTelemetryLog4J2Impl(true);
        assertDoesNotThrow(() -> openTelemetryLog4J2Impl.mutateContextPattern(loggerContext));
    }
    
    @Test
    public void testUpdateLoggerDisabled() {
        loggerContext = new LoggerContext("mocked");
        openTelemetryLog4J2Impl = new OpenTelemetryLog4J2Impl(false);
        assertDoesNotThrow(() -> openTelemetryLog4J2Impl.mutateContextPattern(loggerContext));
    }
    
    @Test
    public void testProcessContext() {
        openTelemetryLog4J2Impl = new OpenTelemetryLog4J2Impl(false);
        try {
            LoggerContext context = mock(LoggerContext.class);
            Configuration configuration = mock(Configuration.class);
            when(context.getConfiguration()).thenReturn(configuration);
            when(configuration.getAppenders()).thenReturn(Collections.emptyMap());
            assertDoesNotThrow(
                    () -> openTelemetryLog4J2Impl.processContext(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUpdateLogger() {
    
    }
    
}