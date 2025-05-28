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

package io.cloudapp.seata.demo.controller;

import io.cloudapp.seata.demo.entity.Account;
import io.cloudapp.seata.demo.entity.Stock;
import io.cloudapp.seata.demo.service.AccountService;
import io.cloudapp.seata.demo.service.BusinessService;
import io.cloudapp.seata.demo.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {
    
    private static final Logger logger =
            LoggerFactory.getLogger(BusinessController.class);
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    StockService stockService;
    
    @RequestMapping("/buy")
    public boolean buy(String userId, String productCode, int count) {
        // before
        Account account = accountService.findByUserId(userId);
        Stock stock = stockService.findByProductCode(productCode);
        logger.info("======================before buy=================");
        logger.info("account: " + account.toString());
        logger.info("stock: " + stock.toString());
        logger.info("=================================================");
        try {
            
            return businessService.buy(userId, productCode, count);
            
        } catch (RuntimeException e) {
            logger.error("buy failed", e);
            
            return false;
            
        } finally {
            account = accountService.findByUserId(userId);
            stock = stockService.findByProductCode(productCode);
            logger.info("===================== after buy=================");
            logger.info("==!!!! rollback maybe execute after return!!!!! ==");
            logger.info("account: " + account.toString());
            logger.info("stock: " + stock.toString());
            logger.info("=================================================");
        }
    }
    
    @RequestMapping("/")
    public String buy2() {
        return "success";
    }
}
