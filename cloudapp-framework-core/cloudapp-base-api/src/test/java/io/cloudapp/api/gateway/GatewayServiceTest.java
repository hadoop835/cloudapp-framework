package io.cloudapp.api.gateway;

import io.cloudapp.api.gateway.authentication.Authorizer;
import io.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GatewayServiceTest {
    
    private GatewayService gatewayService;
    
    private RestTemplate restTemplate;
    private AsyncRestTemplate asyncRestTemplate;
    private Authorizer authorizer;
    
    @Before
    public void setUp() throws Exception {
        restTemplate = mock(RestTemplate.class);
        asyncRestTemplate = mock(AsyncRestTemplate.class);
        authorizer = mock(Authorizer.class);
        
        gatewayService = new GatewayService() {
            
            @Override
            public RestTemplate getRestTemplate() {
                return restTemplate;
            }
            
            @Override
            public AsyncRestTemplate getAsyncRestTemplate() {
                return asyncRestTemplate;
            }
            
            @Override
            public Authorizer getAuthorizer() throws CloudAppException {
                return authorizer;
            }
        };
    }
    
    @Test
    public void testGet() {
        assertNull(
                gatewayService.get("url", Serializable.class)
        );
    }
    
    @Test
    public void testAsyncGet() {
        // Setup
        // Run the test
        final Future<Serializable> result = gatewayService.asyncGet(
                "url", Serializable.class);
        
        // Verify the results
    }
    
    @Test
    public void testAsyncGet_ThrowsCloudAppException() {
        // Setup
        when(asyncRestTemplate.execute(
                anyString(), any(), any(), any())
        ).thenThrow(CloudAppException.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> gatewayService.asyncGet("url",
                                                   Serializable.class
                     )
        );
    }
    
    @Test
    public void testPost() {
        assertNull(gatewayService.post("url", "body",
                                       Serializable.class
        ));
    }
    
    @Test
    public void testAsyncPost() {
        ListenableFuture<ResponseEntity> listenableFuture =
                mock(ListenableFuture.class);
        // Setup
        doReturn(listenableFuture).when(asyncRestTemplate)
                                  .postForEntity(anyString(), any(), any());
        
        CompletableFuture completableFuture =
                mock(CompletableFuture.class);
        
        when(listenableFuture.completable()).thenReturn(completableFuture);
        
        // Run the test
        final Future<Serializable> result = gatewayService.asyncPost(
                "url", "body", Serializable.class);
        
        // Verify the results
    }
    
    @Test
    public void testPostJson() {
        assertNull(
                gatewayService.postJson("url", "jsonBody",
                                        Serializable.class
                )
        );
    }
    
    @Test
    public void testAsyncPostJson() {
        // Setup
        ListenableFuture<ResponseEntity> listenableFuture =
                mock(ListenableFuture.class);
        // Setup
        doReturn(listenableFuture).when(asyncRestTemplate)
                                  .postForEntity(anyString(), any(), any());
        
        CompletableFuture completableFuture =
                mock(CompletableFuture.class);
        
        when(listenableFuture.completable()).thenReturn(completableFuture);
        
        // Run the test
        final Future<Serializable> result = gatewayService.asyncPostJson(
                "url", "jsonBody", Serializable.class);
        
        // Verify the results
    }
    
    @Test
    public void testRequest() throws Exception {
        // Setup
        final RequestEntity<String> request = new RequestEntity<>(
                "body",
                HttpMethod.GET,
                new URI("https://example.com/")
        );
        
        // Run the test
        final ResponseEntity<String> result = gatewayService.request(
                request, String.class);
        
        // Verify the results
        assertNull(result);
    }
    
    @Test
    public void testRequest_ThrowsCloudAppException() throws Exception {
        // Setup
        final RequestEntity<String> request = new RequestEntity<>(
                "body",
                HttpMethod.GET,
                new URI("https://example.com/")
        );
        
    }
    
    @Test
    public void testAsyncRequest() throws Exception {
        // Setup
        final RequestEntity<?> request = new RequestEntity<>(
                null,
                HttpMethod.GET,
                new URI("https://example.com/")
        );
        
        ListenableFuture<ResponseEntity> listenableFuture =
                mock(ListenableFuture.class);
        // Setup
        doReturn(listenableFuture).when(asyncRestTemplate)
                                  .postForEntity(any(), any(), any());
        
        CompletableFuture completableFuture =
                mock(CompletableFuture.class);
        
        when(listenableFuture.completable()).thenReturn(completableFuture);
        
        // Run the test
        final Future<String> result = gatewayService.asyncRequest(
                request, String.class);
        
        // Verify the results
    }
    
}
