/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.cloudapp.api.gateway.authentication;

import io.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class APIKeyAuthorizerTest {

    @Mock
    private HttpRequest request;
    @Mock
    private HttpHeaders headers;

    private APIKeyAuthorizer authorizer;

    @Before
    public void setUp() {
        authorizer = new APIKeyAuthorizer("testApiKey");
        authorizer = new APIKeyAuthorizer("testApiKey", null);
        when(request.getHeaders()).thenReturn(headers);
    }

    @Test
    public void applyAuthorization_APIKeySet_ShouldAddHeader() throws CloudAppException {
        // Act
        authorizer.applyAuthorization(request);

        // Assert
        verify(request.getHeaders()).add(authorizer.getHeaderName(), "testApiKey");
        
    }

    @Test
    public void applyAuthorization_APIKeyNotSet_ShouldThrowException() {
        // Arrange
        authorizer = new APIKeyAuthorizer(null);

        // Act & Assert
        CloudAppException exception = assertThrows(CloudAppException.class,
                () -> authorizer.applyAuthorization(request));
        assertEquals("API key is not set", exception.getMessage());
    }

    @Test
    public void applyAuthorization_APIKeyEmpty_ShouldThrowException() {
        // Arrange
        authorizer = new APIKeyAuthorizer("");

        // Act & Assert
        CloudAppException exception = assertThrows(CloudAppException.class,
                () -> authorizer.applyAuthorization(request));
        assertEquals("API key is not set", exception.getMessage());
    }

    @Test
    public void applyAuthorization_CustomHeader_ShouldAddHeader() throws CloudAppException {
        // Arrange
        String customHeader = "Authorization";
        authorizer = new APIKeyAuthorizer("testApiKey", customHeader);

        // Act
        authorizer.applyAuthorization(request);

        // Assert
        verify(request.getHeaders()).add(customHeader, "testApiKey");
    }
}
