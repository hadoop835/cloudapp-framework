package com.alibaba.cloudapp.model;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class OAuth2ClientTest {
    
    private OAuth2Client client;
    
    @Before
    public void setUp() throws Exception {
        client = new OAuth2Client();
    }
    
    @Test
    public void testClientIdGetterAndSetter() {
        final String clientId = "clientId";
        client.setClientId(clientId);
        assertEquals(clientId, client.getClientId());
    }
    
    @Test
    public void testClientSecretGetterAndSetter() {
        final String clientSecret = "clientSecret";
        client.setClientSecret(clientSecret);
        assertEquals(clientSecret, client.getClientSecret());
    }
    
    @Test
    public void testScopesGetterAndSetter() {
        final List<String> scopes = Collections.singletonList("value");
        client.setScopes(scopes);
        assertEquals(scopes, client.getScopes());
    }
    
    @Test
    public void testRedirectUriGetterAndSetter() {
        final String redirectUri = "redirectUri";
        client.setRedirectUri(redirectUri);
        assertEquals(redirectUri, client.getRedirectUri());
    }
    
    @Test
    public void testTokenUriGetterAndSetter() throws Exception {
        final URI tokenUri = new URI("https://example.com/");
        client.setTokenUri(tokenUri);
        assertEquals(tokenUri, client.getTokenUri());
    }
    
    @Test
    public void testGrantTypesGetterAndSetter() {
        final List<String> grantTypes = Collections.singletonList("value");
        client.setGrantTypes(grantTypes);
        assertEquals(grantTypes, client.getGrantTypes());
    }
    
    @Test
    public void testAuthorizationUriGetterAndSetter() throws Exception {
        final URI authorizationUri = new URI("https://example.com/");
        client.setAuthorizationUri(authorizationUri);
        assertEquals(authorizationUri,
                     client.getAuthorizationUri()
        );
    }
    
    @Test
    public void testJwksUrlGetterAndSetter() throws Exception {
        final URI jwksUrl = new URI("https://example.com/");
        client.setJwksUrl(jwksUrl);
        assertEquals(jwksUrl, client.getJwksUrl());
    }
    
    @Test
    public void testIntrospectionUriGetterAndSetter() throws Exception {
        final URI introspectionUri = new URI("https://example.com/");
        client.setIntrospectionUri(introspectionUri);
        assertEquals(introspectionUri,
                     client.getIntrospectionUri()
        );
    }
    
    @Test
    public void testEnablePkceGetterAndSetter() {
        final boolean enablePkce = false;
        client.setEnablePkce(enablePkce);
        assertFalse(client.isEnablePkce());
    }
    
}
