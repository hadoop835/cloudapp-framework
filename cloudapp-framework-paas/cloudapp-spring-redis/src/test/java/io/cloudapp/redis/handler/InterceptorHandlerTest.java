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

package io.cloudapp.redis.handler;

import io.cloudapp.api.cache.interceptors.DecryptInterceptor;
import io.cloudapp.api.cache.interceptors.EncryptInterceptor;
import io.cloudapp.api.cache.interceptors.MonitoringInterceptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.serializer.RedisSerializer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InterceptorHandlerTest {

    @Mock
    private RedisSerializer<String> delegate;

    @Mock
    private EncryptInterceptor encryptInterceptor;

    @Mock
    private DecryptInterceptor decryptInterceptor;

    @Mock
    private MonitoringInterceptor<String> monitoringInterceptor;

    @InjectMocks
    private InterceptorHandler<String> interceptorHandler;

    private final String sampleInput = "sampleInput";
    private final byte[] sampleOutput = "sampleOutput".getBytes();

    @Before
    public void setUp() throws Exception {
        when(delegate.serialize(sampleInput)).thenReturn(sampleOutput);
        when(delegate.deserialize(sampleOutput)).thenReturn(sampleInput);
    }

    @Test
    public void serialize_WithEncryptionAndMonitoring_InterceptorsShouldProcess() {
        when(encryptInterceptor.intercept(sampleOutput)).thenReturn("encrypted".getBytes());
        when(monitoringInterceptor.needMonitoring(any())).thenReturn(true);

        byte[] result = interceptorHandler.serialize(sampleInput);

        verify(monitoringInterceptor).intercept(sampleInput);
        verify(encryptInterceptor).intercept(sampleOutput);
        assertArrayEquals("encrypted".getBytes(), result);
    }

    @Test
    public void serialize_WithoutEncryptionAndMonitoring_DelegateShouldProcess() {
        interceptorHandler = new InterceptorHandler<>(delegate);

        byte[] result = interceptorHandler.serialize(sampleInput);

        verify(delegate).serialize(sampleInput);
        assertArrayEquals(sampleOutput, result);
    }

    @Test
    public void deserialize_WithDecryption_DecryptInterceptorShouldProcess() {
        when(decryptInterceptor.intercept(any())).thenReturn("decrypted".getBytes());
        when(delegate.deserialize(any())).thenReturn(sampleInput);

        String result = interceptorHandler.deserialize(sampleOutput);

        verify(decryptInterceptor).intercept(sampleOutput);
        assertEquals(sampleInput, result);
    }

    @Test
    public void deserialize_WithoutDecryption_DelegateShouldProcess() {
        interceptorHandler = new InterceptorHandler<>(delegate);

        String result = interceptorHandler.deserialize(sampleOutput);

        verify(delegate).deserialize(sampleOutput);
        assertEquals(sampleInput, result);
    }

    @Test
    public void getEncryptInterceptor_ShouldReturnEncryptInterceptor() {
        assertEquals(encryptInterceptor, interceptorHandler.getEncryptInterceptor());
    }

    @Test
    public void setEncryptInterceptor_ShouldSetEncryptInterceptor() {
        EncryptInterceptor newEncryptInterceptor = mock(EncryptInterceptor.class);
        interceptorHandler.setEncryptInterceptor(newEncryptInterceptor);
        assertEquals(newEncryptInterceptor, interceptorHandler.getEncryptInterceptor());
    }

    @Test
    public void getDecryptInterceptor_ShouldReturnDecryptInterceptor() {
        assertEquals(decryptInterceptor, interceptorHandler.getDecryptInterceptor());
    }

    @Test
    public void setDecryptInterceptor_ShouldSetDecryptInterceptor() {
        DecryptInterceptor newDecryptInterceptor = mock(DecryptInterceptor.class);
        interceptorHandler.setDecryptInterceptor(newDecryptInterceptor);
        assertEquals(newDecryptInterceptor, interceptorHandler.getDecryptInterceptor());
    }

    @Test
    public void getMonitoringInterceptor_ShouldReturnMonitoringInterceptor() {
        assertEquals(monitoringInterceptor, interceptorHandler.getMonitoringInterceptor());
    }

    @Test
    public void setMonitoringInterceptor_ShouldSetMonitoringInterceptor() {
        MonitoringInterceptor<String> newMonitoringInterceptor = mock(MonitoringInterceptor.class);
        interceptorHandler.setMonitoringInterceptor(newMonitoringInterceptor);
        assertEquals(newMonitoringInterceptor, interceptorHandler.getMonitoringInterceptor());
    }

    @Test
    public void getDelegate_ShouldReturnDelegate() {
        assertEquals(delegate, interceptorHandler.getDelegate());
    }

    @Test
    public void setDelegate_ShouldSetDelegate() {
        RedisSerializer<String> newDelegate = mock(RedisSerializer.class);
        interceptorHandler.setDelegate(newDelegate);
        assertEquals(newDelegate, interceptorHandler.getDelegate());
    }
}
