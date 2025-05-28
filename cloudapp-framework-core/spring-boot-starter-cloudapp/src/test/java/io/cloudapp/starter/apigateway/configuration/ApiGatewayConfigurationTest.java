package io.cloudapp.starter.apigateway.configuration;

import io.cloudapp.api.gateway.Authorized;
import io.cloudapp.api.gateway.GatewayService;
import io.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import io.cloudapp.apigateway.aliyun.properties.JwtProperties;
import io.cloudapp.apigateway.aliyun.service.ApiGatewayManager;
import io.cloudapp.apigateway.aliyun.service.AuthorizedService;
import io.cloudapp.starter.apigateway.properties.ApiGatewayManagerProperties;
import io.cloudapp.starter.apigateway.properties.ApiGatewayProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApiGatewayConfigurationTest {
    
    private ApiGatewayConfiguration apiGatewayConfigurationUnderTest;
    
    @BeforeEach
    void setUp() {
        apiGatewayConfigurationUnderTest = new ApiGatewayConfiguration();
    }
    
    @Test
    void testApiGatewayManagerRefreshComponent() {
        // Setup
        final ApiGatewayManagerProperties properties = new ApiGatewayManagerProperties();
        properties.setAccessKey("accessKey");
        properties.setSecretKey("secretKey");
        properties.setGatewayUri("gatewayUri");
        
        // Run the test
        final ApiGatewayManagerRefreshComponent result = apiGatewayConfigurationUnderTest.apiGatewayManagerRefreshComponent(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testApiGatewayService() {
        // Setup
        final ApiGatewayManagerProperties apiGatewayManagerProperties = new ApiGatewayManagerProperties();
        apiGatewayManagerProperties.setAccessKey("accessKey");
        apiGatewayManagerProperties.setSecretKey("secretKey");
        apiGatewayManagerProperties.setGatewayUri("gatewayUri");
        final ApiGatewayManagerRefreshComponent managerRefreshComponent = new ApiGatewayManagerRefreshComponent(
                apiGatewayManagerProperties);
        
        // Run the test
        final ApiGatewayManager result = apiGatewayConfigurationUnderTest.apiGatewayService(
                managerRefreshComponent);
        
        // Verify the results
    }
    
    @Test
    void testAuthorizedRefreshComponent() {
        // Setup
        final ApiGatewayProperties properties = new ApiGatewayProperties();
        final ApiKeyProperties apiKey = new ApiKeyProperties();
        apiKey.setApiKey("apiKey");
        apiKey.setHeaderName("headerName");
        properties.setApiKey(apiKey);
        final JwtProperties jwt = new JwtProperties();
        jwt.setKeyId("keyId");
        properties.setJwt(jwt);
        
        // Run the test
        final AuthorizedRefreshComponent result = apiGatewayConfigurationUnderTest.authorizedRefreshComponent(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testAuthorized() {
        // Setup
        final ApiGatewayProperties apiGatewayProperties = new ApiGatewayProperties();
        final ApiKeyProperties apiKey = new ApiKeyProperties();
        apiKey.setApiKey("apiKey");
        apiKey.setHeaderName("headerName");
        apiGatewayProperties.setApiKey(apiKey);
        final JwtProperties jwt = new JwtProperties();
        jwt.setKeyId("keyId");
        apiGatewayProperties.setJwt(jwt);
        final AuthorizedRefreshComponent authorizedRefreshComponent = new AuthorizedRefreshComponent(
                apiGatewayProperties);
        
        // Run the test
        final AuthorizedService result = apiGatewayConfigurationUnderTest.authorized(
                authorizedRefreshComponent);
        
        // Verify the results
    }
    
    @Test
    void testGatewayService() {
        // Setup
        final Authorized authorized = null;
        
        // Run the test
        final GatewayService result = apiGatewayConfigurationUnderTest.gatewayService(
                authorized);
        
        // Verify the results
    }
    
}
