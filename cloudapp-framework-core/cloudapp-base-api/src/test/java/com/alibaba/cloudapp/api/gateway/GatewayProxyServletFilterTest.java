package com.alibaba.cloudapp.api.gateway;

import com.alibaba.cloudapp.api.gateway.authentication.Authorizer;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.mockito.Mockito.mock;

public class GatewayProxyServletFilterTest {
    
    private GatewayProxyServletFilter servletFilter;
    
    @Before
    public void setUp() throws Exception {
        servletFilter = new GatewayProxyServletFilter() {
            
            @Override
            public void proxy(ServletRequest request, ServletResponse response)
                    throws CloudAppException {
                
            }
            
            @Override
            public Authorizer getAuthorizer() throws CloudAppException {
                return null;
            }
            
            @Override
            public void init(FilterConfig filterConfig) {}
            
            @Override
            public void destroy() {}
        };
    }
    
    @Test
    public void testDoFilter() throws Exception {
        // Setup
        final ServletRequest mockRequest = mock(ServletRequest.class);
        final ServletResponse mockResponse = mock(ServletResponse.class);
        
        // Run the test
        servletFilter.doFilter(mockRequest, mockResponse,null);
        
        // Verify the results
    }
    
    
}
