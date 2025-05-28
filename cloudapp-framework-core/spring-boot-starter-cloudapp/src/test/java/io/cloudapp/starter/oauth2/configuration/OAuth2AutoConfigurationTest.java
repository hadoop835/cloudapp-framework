package io.cloudapp.starter.oauth2.configuration;

import io.cloudapp.api.oauth2.AuthorizationService;
import io.cloudapp.api.oauth2.TokenStorageService;
import io.cloudapp.oauth2.verifier.IntrospectionTokenVerifier;
import io.cloudapp.oauth2.verifier.JwtTokenVerifier;
import io.cloudapp.starter.oauth2.properties.OAuth2ClientProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

class OAuth2AutoConfigurationTest {
    
    private OAuth2AutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new OAuth2AutoConfiguration();
    }
    
    @Test
    void testOauth2Component() throws Exception {
        // Setup
        final OAuth2ClientProperties properties = new OAuth2ClientProperties();
        properties.setJwksUrl(new URI("https://example.com/"));
        properties.setIntrospectionUri(new URI("https://example.com/"));
        properties.setEnabled(false);
        
        // Run the test
        final OAuth2Component result = configuration.oauth2Component(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testAuthorizationService() throws Exception {
        // Setup
        final OAuth2ClientProperties oAuth2ClientProperties = new OAuth2ClientProperties();
        oAuth2ClientProperties.setJwksUrl(new URI("https://example.com/"));
        oAuth2ClientProperties.setIntrospectionUri(
                new URI("https://example.com/"));
        oAuth2ClientProperties.setEnabled(false);
        final OAuth2Component component = new OAuth2Component(
                oAuth2ClientProperties);
        
        // Run the test
        final AuthorizationService result = configuration.authorizationService(
                component);
        
        // Verify the results
    }
    
    @Test
    void testStorageToken() {
        // Setup
        // Run the test
        final TokenStorageService result = configuration.storageToken();
        
        // Verify the results
    }
    
    @Test
    void testJwksTokenVerifier() throws Exception {
        // Setup
        final OAuth2ClientProperties properties = new OAuth2ClientProperties();
        properties.setJwksUrl(new URI("https://example.com/"));
        properties.setIntrospectionUri(new URI("https://example.com/"));
        properties.setEnabled(false);
        
        // Run the test
        final JwtTokenVerifier result = configuration.jwksTokenVerifier(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testIntrospectionTokenVerifier() throws Exception {
        // Setup
        final OAuth2ClientProperties properties = new OAuth2ClientProperties();
        properties.setJwksUrl(new URI("https://example.com/"));
        properties.setIntrospectionUri(new URI("https://example.com/"));
        properties.setEnabled(false);
        
        // Run the test
        final IntrospectionTokenVerifier result = configuration.introspectionTokenVerifier(
                properties);
        
        // Verify the results
    }
    
}
