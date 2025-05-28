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

package io.cloudapp.seata.demo.service.impl;

import io.cloudapp.seata.demo.DemoAsyncCallback;
import io.cloudapp.seata.demo.entity.Stock;
import io.cloudapp.seata.demo.service.BusinessService;
import io.cloudapp.seata.demo.service.StockService;
import io.seata.core.context.RootContext;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.statelang.domain.StateMachineInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class BusinessServiceImpl implements BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);
    
    private static final String MACHINE_NAME = "SagaDemoMachine";
    private static final String TENANT_ID = "";

    @Resource
    private StockService stockService;
    @Autowired
    private StateMachineEngine stateMachineEngine;

    private final Random random = new Random();

    @Override
    public String buy(String userId, String productCode, int count) {
        String businessKey = String.valueOf(System.currentTimeMillis());
        
        Stock stock = stockService.findByProductCode(productCode);
        
        if(stock == null) {
            throw new RuntimeException("product: " + productCode + " not exist");
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("productCode", productCode);
        params.put("count", count);
        params.put("amount",  stock.getPrice().multiply(new BigDecimal(count)));
        params.put("businessKey", businessKey);
        
        DemoAsyncCallback callback = new DemoAsyncCallback();
        
        StateMachineInstance instance = stateMachineEngine.startWithBusinessKeyAsync(
                MACHINE_NAME, TENANT_ID, businessKey, params, callback);
        
        LOGGER.info("purchase begin ... xid: " + RootContext.getXID());
        
        callback.waitingForFinish(instance);
        
        LOGGER.info("instance status: {} ", instance.getStatus());
        
        return businessKey;
    }

}
