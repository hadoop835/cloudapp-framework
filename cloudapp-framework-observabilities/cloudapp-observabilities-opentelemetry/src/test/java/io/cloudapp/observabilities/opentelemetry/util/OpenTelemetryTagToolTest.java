package io.cloudapp.observabilities.opentelemetry.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RunWith(MockitoJUnitRunner.class)
public class OpenTelemetryTagToolTest {
    
    @Test
    public void testGetTraceId() {
        assertDoesNotThrow(OpenTelemetryTagTool::getTraceId);
    }
}
