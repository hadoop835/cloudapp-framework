package io.cloudapp.starter.apigateway.configuration;

import io.cloudapp.api.oauth2.AuthorizationService;
import io.cloudapp.api.oauth2.TokenStorageService;
import io.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import io.cloudapp.apigateway.aliyun.properties.JwtProperties;
import io.cloudapp.model.OAuth2Client;
import io.cloudapp.starter.apigateway.properties.ApiGatewayProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

class ApiGatewayOAuth2ConfigurationTest {
    
    private ApiGatewayOAuth2Configuration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new ApiGatewayOAuth2Configuration();
    }
    
    @Test
    void testOauth2Template() {
        // Setup
        final RestTemplate properties = new RestTemplate(
                Collections.singletonList(
                        new ByteArrayHttpMessageConverter()
                ));
        
        // Run the test
        final RestTemplate result = configuration.oauth2Template();
        
        // Verify the results
    }
    
    @Test
    void testAuthorizationService() {
        // Setup
        final ApiGatewayProperties properties = new ApiGatewayProperties();
        final ApiKeyProperties apiKey = new ApiKeyProperties();
        apiKey.setApiKey("apiKey");
        apiKey.setHeaderName("headerName");
        properties.setApiKey(apiKey);
        final JwtProperties jwt = new JwtProperties();
        properties.setJwt(jwt);
        final OAuth2Client oAuth2 = new OAuth2Client();
        properties.setOAuth2(oAuth2);
        
        final RestTemplate restTemplate = new RestTemplate(
                Collections.singletonList(new ByteArrayHttpMessageConverter()));
        
        
        // Run the test
        final AuthorizationService result = configuration.authorizationService(
                properties, restTemplate);
        
        // Verify the results
    }
    
    @Test
    void testTokenStorageService() {
        // Setup
        // Run the test
        final TokenStorageService result = configuration.tokenStorageService();
        
        // Verify the results
    }
    
}
