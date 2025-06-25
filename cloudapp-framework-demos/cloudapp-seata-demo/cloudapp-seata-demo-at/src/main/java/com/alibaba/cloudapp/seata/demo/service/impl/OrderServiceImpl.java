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

import com.alibaba.cloudapp.seata.demo.dao.OrderDao;
import com.alibaba.cloudapp.seata.demo.dao.StockDao;
import com.alibaba.cloudapp.seata.demo.entity.Order;
import com.alibaba.cloudapp.seata.demo.entity.Stock;
import com.alibaba.cloudapp.seata.demo.service.OrderService;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * The type Order service.
 *
 * @author jimin.jm @alibaba-inc.com
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Resource
    private StockDao stockDao;
    @Resource
    private OrderDao orderDao;

    @Override
    @Transactional
    public Order create(String userId, String productCode, int count) {
        LOGGER.info("Order Service Begin ... xid: " + RootContext.getXID());

        Stock stock = stockDao.findByProductCode(productCode);
        
        if(stock == null) {
            throw new RuntimeException("product:" + productCode + " not exist");
        }

        BigDecimal orderMoney = new BigDecimal(count).multiply(stock.getPrice());
        
        LOGGER.info("create order(userId: {}, productCode: {}, count:{})", userId, productCode, count);
        
        Order order = new Order();
        order.setUserId(userId);
        order.setProductCode(productCode);
        order.setCount(count);
        order.setAmount(orderMoney);
        
        orderDao.insert(order);

        LOGGER.info("Order Service End ... Created order: {} ", order.getId());
        return order;
    }

}
