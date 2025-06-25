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

package com.alibaba.cloudapp.seata.demo.service.impl;

import com.alibaba.cloudapp.seata.demo.service.AccountService;
import com.alibaba.cloudapp.seata.demo.service.BusinessService;
import com.alibaba.cloudapp.seata.demo.service.OrderService;
import com.alibaba.cloudapp.seata.demo.service.StockService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements BusinessService {
    
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StockService stockService;
    
    @Override
    @GlobalTransactional
    public boolean buy(String userId, String productCode, int count) {
        boolean stock = stockService.prepare(null, productCode, count);
        if (!stock) {
            throw new RuntimeException("StockService failed.");
        }
        boolean order = orderService.prepare(null, userId, productCode, count);
        if(!order) {
            throw new RuntimeException("OrderService failed");
        }
        
        boolean prepare = accountService.prepare(null, userId);
        if (!prepare) {
            throw new RuntimeException("AccountService failed.");
        }
        
        return true;
    }
}
