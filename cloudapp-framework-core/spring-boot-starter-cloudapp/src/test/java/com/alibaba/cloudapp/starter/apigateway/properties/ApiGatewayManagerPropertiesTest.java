package com.alibaba.cloudapp.starter.apigateway.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiGatewayManagerPropertiesTest {
    
    private ApiGatewayManagerProperties apiGatewayManagerPropertiesUnderTest;
    
    @BeforeEach
    void setUp() {
        apiGatewayManagerPropertiesUnderTest = new ApiGatewayManagerProperties();
    }
    
    @Test
    void testAccessKeyGetterAndSetter() {
        final String accessKey = "accessKey";
        apiGatewayManagerPropertiesUnderTest.setAccessKey(accessKey);
        assertEquals(accessKey,
                     apiGatewayManagerPropertiesUnderTest.getAccessKey()
        );
    }
    
    @Test
    void testSecretKeyGetterAndSetter() {
        final String secretKey = "secretKey";
        apiGatewayManagerPropertiesUnderTest.setSecretKey(secretKey);
        assertEquals(secretKey,
                     apiGatewayManagerPropertiesUnderTest.getSecretKey()
        );
    }
    
    @Test
    void testGatewayUriGetterAndSetter() {
        final String gatewayUri = "gatewayUri";
        apiGatewayManagerPropertiesUnderTest.setGatewayUri(gatewayUri);
        assertEquals(gatewayUri,
                     apiGatewayManagerPropertiesUnderTest.getGatewayUri()
        );
    }
    
}
