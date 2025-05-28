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

import io.cloudapp.seata.demo.ResultHolder;
import io.cloudapp.seata.demo.dao.OrderDao;
import io.cloudapp.seata.demo.entity.Order;
import io.cloudapp.seata.demo.service.OrderService;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderService {
    
    private final static Logger logger =
            LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    private OrderDao orderDao;
    
    @Override
    @Transactional
    public boolean prepare(BusinessActionContext context,
                         String productCode,
                         String userId,
                         int count) {
        logger.info("orderService prepare: {}", context.getXid());
        
        BigDecimal price = (BigDecimal) ResultHolder.getResult(context.getXid(), "price");
        if(price == null) {
            return false;
        }
        
        Order order = new Order();
        order.setProductCode(productCode);
        order.setUserId(userId);
        order.setCount(count);
        order.setAmount(price.multiply(new BigDecimal(count)));
        order.setPrice(price);
        order.setTxId(context.getXid());
        orderDao.save(order);
        
        ResultHolder.setResult(context.getXid(), "amount", order.getAmount());
        
        logger.info("Order Service End ... Created order: {} ", order.getId());
        return true;
    }
    
    @Override
    public boolean commit(BusinessActionContext context) {
        logger.info("orderService commit");
        return true;
    }
    
    @Override
    public boolean rollback(BusinessActionContext context) {
        logger.info("orderService rollback");
        String txId = context.getXid();
        Order order = orderDao.findByTxId(txId);
        if(order != null) {
            orderDao.delete(order);
            return true;
        }
        return true;
    }
    
}
