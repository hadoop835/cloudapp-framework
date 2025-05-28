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

package io.cloudapp.config.aliyun;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.exeption.CloudAppInvalidArgumentsException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static io.cloudapp.config.aliyun.AliyunConfigConstants.CLOUD_APP_NACOS_GET_CONFIG_ERROR;
import static io.cloudapp.config.aliyun.AliyunConfigConstants.CLOUD_APP_NACOS_WATCH_ERROR;
import static io.cloudapp.config.aliyun.AliyunConfigConstants.DEFAULT_GROUP;
import static io.cloudapp.config.aliyun.AliyunConfigConstants.DEFAULT_TIMEOUT;

@RunWith(MockitoJUnitRunner.class)
public class NacosConfigReadServiceTest {
    
    @Mock
    private ConfigService configService;
    
    private NacosConfigReadService nacosConfigReadService;
    
    @Before
    public void before() {
        nacosConfigReadService = new NacosConfigReadService(configService);
    }
    
    @Test
    public void getDelegatingClient() {
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void getConfig_should_throw_IllegalArgumentException_when_configName_is_null() {
        nacosConfigReadService.getConfig(null);
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void getConfig_should_throw_IllegalArgumentException_when_configName_is_blank() {
        nacosConfigReadService.getConfig(" ");
    }
    
    @Test
    public void getConfig_by_full_name() throws NacosException {
        String dataId = "/groupa/datab";
        String expectedResult = "content";
        Mockito.when(
                       configService.getConfig("datab", "groupa",
                                               DEFAULT_TIMEOUT
                       ))
               .thenReturn(expectedResult);
        String result = nacosConfigReadService.getConfig(dataId);
        Assert.assertEquals(expectedResult, result);
        Mockito.verify(configService, Mockito.times(1))
               .getConfig("datab", "groupa",
                          DEFAULT_TIMEOUT
               );
    }
    
    @Test
    public void getConfig_by_simple_name() throws NacosException {
        String dataId = "datab";
        String expectedResult = "content";
        Mockito.when(
                       configService.getConfig("datab", DEFAULT_GROUP,
                                               DEFAULT_TIMEOUT
                       ))
               .thenReturn(expectedResult);
        String result = nacosConfigReadService.getConfig(dataId);
        Assert.assertEquals(expectedResult, result);
        Mockito.verify(configService, Mockito.times(1))
               .getConfig("datab", DEFAULT_GROUP,
                          DEFAULT_TIMEOUT
               );
    }
    
    @Test
    public void getConfig_when_happen_NacosException() throws NacosException {
        String dataId = "datab";
        Mockito.when(
                       configService.getConfig(dataId, DEFAULT_GROUP,
                                               DEFAULT_TIMEOUT
                       ))
               .thenThrow(new NacosException(NacosException.BAD_GATEWAY,
                                             "ServiceUnavailable"
               ));
        try {
            nacosConfigReadService.getConfig(dataId);
            Assert.fail("must throw CloudAppException");
        } catch (CloudAppException e) {
            Assert.assertEquals(
                    CLOUD_APP_NACOS_GET_CONFIG_ERROR,
                    e.getCode()
            );
        }
    }
    
    @Test
    public void getAndListen() throws NacosException {
        AtomicReference<Listener> listenerAtomicReference =
                new AtomicReference<>();
        String configName = "/groupa/datab";
        List<String> result = new ArrayList<>();
        nacosConfigReadService.getAndListen(configName, v -> {
            result.add((String) v);
        });
        Mockito.verify(configService, Mockito.times(1))
               .addListener(Mockito.eq("datab"), Mockito.eq(
                       "groupa"), ArgumentMatchers.argThat(listener -> {
                           listenerAtomicReference.set(listener);
                           return true;
               }));
        listenerAtomicReference.get().receiveConfigInfo("x");
        Assert.assertEquals(Arrays.asList("x"), result);
    }
    
    @Test
    public void getAndListen_when_happen_NacosException()
            throws NacosException {
        String dataId = "datab";
        Mockito.doThrow(new NacosException(NacosException.BAD_GATEWAY,
                                           "ServiceUnavailable"
               )).when(configService)
               .addListener(Mockito.eq(dataId),
                            Mockito.eq(DEFAULT_GROUP),
                            Mockito.any(Listener.class)
               );
        try {
            nacosConfigReadService.getAndListen(dataId, v -> {
            });
            Assert.fail("must throw CloudAppException");
        } catch (CloudAppException e) {
            Assert.assertEquals(
                    CLOUD_APP_NACOS_WATCH_ERROR,
                    e.getCode()
            );
        }
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void getAndListen_should_throw_IllegalArgumentException_when_configName_is_null() {
        nacosConfigReadService.getAndListen(null, v -> {
        
        });
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void getAndListen_should_throw_IllegalArgumentException_when_configName_is_blank() {
        nacosConfigReadService.getAndListen(" ", v -> {
        
        });
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void getAndListen_should_throw_IllegalArgumentException_when_listener_is_null() {
        nacosConfigReadService.getAndListen("datab", null);
    }
    
    @Test
    public void testGetConfigToTargetObject() throws NacosException {
        String dataId = "/groupa/datab";
        String expectedResult = "{\"name\":\"abc\"}";
        Mockito.when(
                       configService.getConfig("datab", "groupa",
                                               DEFAULT_TIMEOUT
                       ))
               .thenReturn(expectedResult);
        Foo result = (Foo) nacosConfigReadService.getConfig(dataId, Foo.class);
        Assert.assertEquals("abc", result.name);
    }
    
    @Test
    public void testGetConfigToTargetString() throws NacosException {
        String dataId = "/groupa/datab";
        String expectedResult = "{\"name\":\"abc\"}";
        Mockito.when(
                       configService.getConfig("datab", "groupa",
                                               DEFAULT_TIMEOUT
                       ))
               .thenReturn(expectedResult);
        String result = (String) nacosConfigReadService.getConfig(dataId,
                                                                  String.class
        );
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void getConfigToTarget_when_happen_NacosException() throws NacosException {
        String dataId = "datab";
        Mockito.when(
                       configService.getConfig(dataId, DEFAULT_GROUP,
                                               DEFAULT_TIMEOUT
                       ))
               .thenThrow(new NacosException(NacosException.BAD_GATEWAY,
                                             "ServiceUnavailable"
               ));
        try {
            nacosConfigReadService.getConfig(dataId,Foo.class);
            Assert.fail("must throw CloudAppException");
        } catch (CloudAppException e) {
            Assert.assertEquals(
                    CLOUD_APP_NACOS_GET_CONFIG_ERROR,
                    e.getCode()
            );
        }
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void getConfigTargetObject_should_throw_IllegalArgumentException_when_configName_is_null() {
        nacosConfigReadService.getConfig(null, String.class);
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void getConfigTargetObject_should_throw_IllegalArgumentException_when_configName_is_blank() {
        nacosConfigReadService.getConfig(" ", String.class);
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void getConfigTargetObject_should_throw_IllegalArgumentException_when_cls_is_null() {
        nacosConfigReadService.getConfig("datab", null);
    }
    
    class Foo {
        String name;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
    }
    
}