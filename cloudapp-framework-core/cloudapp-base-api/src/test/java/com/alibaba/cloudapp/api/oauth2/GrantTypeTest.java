package com.alibaba.cloudapp.api.oauth2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GrantTypeTest {
    
    @Test
    public void testGetGrantType() {
        assertEquals("authorization_code", GrantType.AUTHORIZATION_CODE.getGrantType());
        assertEquals("client_credentials", GrantType.CLIENT_CREDENTIALS.getGrantType());
        assertEquals("password", GrantType.PASSWORD.getGrantType());
        assertEquals("implicit", GrantType.IMPLICIT.getGrantType());
        assertEquals("refresh_token", GrantType.REFRESH_TOKEN.getGrantType());
    }
    
    @Test
    public void testGetInstance() {
        assertEquals(GrantType.AUTHORIZATION_CODE,
                     GrantType.getInstance("authorization_code")
        );
    }
    
}
