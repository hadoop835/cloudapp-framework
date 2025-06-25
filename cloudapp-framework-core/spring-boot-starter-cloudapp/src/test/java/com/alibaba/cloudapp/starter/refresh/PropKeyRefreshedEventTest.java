package com.alibaba.cloudapp.starter.refresh;

import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PropKeyRefreshedEventTest {
    
    @Mock
    private RefreshableProperties mockProperties;
    
    private PropKeyRefreshedEvent keyRefreshedEvent;
    
    @BeforeEach
    void setUp() {
        keyRefreshedEvent = new PropKeyRefreshedEvent("source",
                                                      mockProperties);
    }
    
    @Test
    void testGetRefreshedKey() {
        assertEquals("source",
                     keyRefreshedEvent.getRefreshedKey()
        );
    }
    
    @Test
    void testGetProperties() {
        assertEquals(mockProperties,
                     keyRefreshedEvent.getProperties()
        );
    }
    
}
