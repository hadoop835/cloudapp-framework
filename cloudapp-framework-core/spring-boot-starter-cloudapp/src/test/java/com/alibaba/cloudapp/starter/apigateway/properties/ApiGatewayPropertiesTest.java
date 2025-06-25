package com.alibaba.cloudapp.starter.apigateway.properties;

import com.alibaba.cloudapp.apigateway.aliyun.AuthTypeEnum;
import com.alibaba.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.BasicProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.JwtProperties;
import com.alibaba.cloudapp.model.OAuth2Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiGatewayPropertiesTest {
    
    private ApiGatewayProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new ApiGatewayProperties();
    }
    
    @Test
    void testGetAuthType() {
        assertEquals(AuthTypeEnum.NO_AUTH, properties.getAuthType()
        );
    }
    
    @Test
    void testApiKeyGetterAndSetter() {
        final ApiKeyProperties apiKey = new ApiKeyProperties();
        properties.setApiKey(apiKey);
        assertEquals(apiKey, properties.getApiKey());
    }
    
    @Test
    void testJwtGetterAndSetter() {
        final JwtProperties jwt = new JwtProperties();
        properties.setJwt(jwt);
        assertEquals(jwt, properties.getJwt());
    }
    
    @Test
    void testOAuth2GetterAndSetter() {
        final OAuth2Client oAuth2 = new OAuth2Client();
        properties.setOAuth2(oAuth2);
        assertEquals(oAuth2, properties.getOAuth2());
    }
    
    @Test
    void testBasicGetterAndSetter() {
        final BasicProperties basic = new BasicProperties();
        properties.setBasic(basic);
        assertEquals(basic, properties.getBasic());
    }
    
}
