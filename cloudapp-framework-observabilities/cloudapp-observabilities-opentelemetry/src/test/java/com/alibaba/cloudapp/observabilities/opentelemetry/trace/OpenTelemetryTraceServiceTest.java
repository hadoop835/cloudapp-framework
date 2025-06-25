package com.alibaba.cloudapp.observabilities.opentelemetry.trace;

import io.opentelemetry.context.Scope;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RunWith(MockitoJUnitRunner.class)
public class OpenTelemetryTraceServiceTest {
    
    @InjectMocks
    private OpenTelemetryTraceService traceService;
    
    @Test
    public void testGetBaggageUserData() {
        Assert.assertEquals(Collections.emptyMap(),
                            traceService.getBaggageUserData());
    }
    
    @Test
    public void getBaggageUserDataValue_found_and_notFound() {
        Map<String, String> immutableLabels =
                Collections.unmodifiableMap(new HashMap<String, String>() {{
                    put("test_baggage_key", "test_baggage_value");
                }});
        try (Scope scope = traceService.withBaggageUserData(immutableLabels)) {
            Assert.assertEquals(traceService.getBaggageUserDataValue(
                    "test_baggage_key"), "test_baggage_value");
            Assert.assertNull(traceService.getBaggageUserDataValue(
                    "test_baggage_key_not_exists"));
        }
    }
    
    @Test
    public void testPreStop() {
        assertDoesNotThrow(() -> traceService.preStop());
    }
    
    @Test
    public void testPostStart() {
        assertDoesNotThrow(() -> traceService.postStart());
    }
}
