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

package com.alibaba.cloudapp.starter.seata.configuration;

import com.alibaba.cloudapp.seata.CloudAppGlobalTransactionScanner;
import com.alibaba.cloudapp.seata.GlobalTransactionalInterceptorProxy;
import io.seata.common.loader.EnhancedServiceLoader;
import io.seata.spring.annotation.GlobalTransactionScanner;
import io.seata.spring.annotation.ScannerChecker;
import io.seata.spring.boot.autoconfigure.properties.SeataProperties;
import io.seata.tm.api.FailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.List;

@Configuration
@ConditionalOnClass({
        CloudAppGlobalTransactionScanner.class,
        GlobalTransactionScanner.class
})
@ConditionalOnProperty(
        prefix = "io.cloudapp.seata",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class CloudAppSeataAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "io.cloudapp.seata")
    public SeataProperties seataProperties() {
        return new SeataProperties();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public GlobalTransactionalInterceptorProxy transactionalInterceptorProxyImpl() {
        return new GlobalTransactionalInterceptorProxy();
    }
    
    @Bean
    @DependsOn({"springApplicationContextProvider", "failureHandler"})
    @ConditionalOnMissingBean({GlobalTransactionScanner.class})
    public static GlobalTransactionScanner globalTransactionScanner(
            SeataProperties seataProperties,
            FailureHandler failureHandler,
            ConfigurableListableBeanFactory beanFactory,
            @Autowired(required = false) List<ScannerChecker> scannerCheckers) {
        
        GlobalTransactionScanner.setBeanFactory(beanFactory);
        GlobalTransactionScanner.addScannerCheckers(
                EnhancedServiceLoader.loadAll(ScannerChecker.class));
        GlobalTransactionScanner.addScannerCheckers(scannerCheckers);
        GlobalTransactionScanner.addScannablePackages(
                seataProperties.getScanPackages());
        GlobalTransactionScanner.addScannerExcludeBeanNames(
                seataProperties.getExcludesForScanning());
        GlobalTransactionScanner.setAccessKey(seataProperties.getAccessKey());
        GlobalTransactionScanner.setSecretKey(seataProperties.getSecretKey());
        return new CloudAppGlobalTransactionScanner(seataProperties.getApplicationId(),
                                            seataProperties.getTxServiceGroup(),
                                            failureHandler
        );
    }
    
}