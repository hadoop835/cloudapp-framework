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

package com.alibaba.cloudapp.seata.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.cloudapp.seata.demo.dao.AccountDao;
import com.alibaba.cloudapp.seata.demo.dao.OrderDao;
import com.alibaba.cloudapp.seata.demo.dao.StockDao;
import com.alibaba.cloudapp.seata.demo.entity.Account;
import com.alibaba.cloudapp.seata.demo.entity.Order;
import com.alibaba.cloudapp.seata.demo.entity.Stock;
import com.alibaba.cloudapp.seata.demo.service.BusinessService;
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
    AccountDao accountDao;
    
    @Autowired
    OrderDao orderDao;
    
    @Autowired
    StockDao stockDao;
    
    @RequestMapping("/buy")
    public String buy(String userId, String productCode, int count) {
        // before
        Account account = accountDao.findByUserId(userId);
        Stock stock = stockDao.findByProductCode(productCode);
        logger.info("======================before buy=================");
        logger.info("account: " + account.toString());
        logger.info("stock: " + stock.toString());
        logger.info("=================================================");
        try {
            
            return businessService.buy(userId, productCode, count);
            
        } catch (RuntimeException e) {
            logger.error("buy failed", e);
            
            return "fail";
            
        } finally {
            account = accountDao.findByUserId(userId);
            stock = stockDao.findByProductCode(productCode);
            Order order = orderDao.findLastOrderByUserIdAndProductCode(userId, productCode);
            logger.info("===================== after buy=================");
            logger.info("account: " + account.toString());
            logger.info("stock: " + stock.toString());
            logger.info("order: " + JSON.toJSONString(order));
            logger.info("=================================================");
        }
    }
    
    @RequestMapping("/")
    public String buy2() {
        return "success";
    }
}
