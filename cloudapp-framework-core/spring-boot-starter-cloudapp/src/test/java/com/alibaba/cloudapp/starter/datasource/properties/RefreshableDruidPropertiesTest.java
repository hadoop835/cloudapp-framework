package com.alibaba.cloudapp.starter.datasource.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RefreshableDruidPropertiesTest {
    
    private RefreshableDruidProperties refreshableDruidPropertiesUnderTest;
    
    @BeforeEach
    void setUp() {
        refreshableDruidPropertiesUnderTest = new RefreshableDruidProperties();
    }
    
    @Test
    void testManageVersionGetterAndSetter() {
        final String manageVersion = "manageVersion";
        refreshableDruidPropertiesUnderTest.setManageVersion(manageVersion);
        assertEquals(manageVersion,
                     refreshableDruidPropertiesUnderTest.getManageVersion()
        );
    }
    
    @Test
    void testMaxRefreshWaitSecondsGetterAndSetter() {
        final int maxRefreshWaitSeconds = 0;
        refreshableDruidPropertiesUnderTest.setMaxRefreshWaitSeconds(
                maxRefreshWaitSeconds);
        assertEquals(maxRefreshWaitSeconds,
                     refreshableDruidPropertiesUnderTest.getMaxRefreshWaitSeconds()
        );
    }
    
}
