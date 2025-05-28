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

import io.cloudapp.seata.demo.entity.Order;
import io.cloudapp.seata.demo.service.AccountService;
import io.cloudapp.seata.demo.service.BusinessService;
import io.cloudapp.seata.demo.service.OrderService;
import io.cloudapp.seata.demo.service.StockService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

@Service
public class BusinessServiceImpl implements BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);

    @Resource
    private StockService stockService;
    @Resource
    private OrderService orderService;
    @Resource
    private AccountService accountService;

    private final Random random = new Random();

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "cloudapp-seata-at")
    public void buy(String userId, String productCode, int count) {
        LOGGER.info("purchase begin ... xid: " + RootContext.getXID());
        
        stockService.debit(productCode, count);
        Order order = orderService.create(userId, productCode, count);
        accountService.debit(userId, order.getAmount());
        
        if (random.nextBoolean()) {
            throw new RuntimeException("random exception!");
        }
    }

}
