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

import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.seata.demo.dao.OrderDao;
import com.alibaba.cloudapp.seata.demo.entity.Order;
import com.alibaba.cloudapp.seata.demo.service.OrderService;
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
    public Order create(String productCode, String userId, int count,
                        BigDecimal price) {
        if (price == null) {
            throw new CloudAppException("price is null");
        }
        
        Order order = new Order();
        order.setProductCode(productCode);
        order.setUserId(userId);
        order.setCount(count);
        order.setAmount(price.multiply(new BigDecimal(count)));
        order.setPrice(price);
        orderDao.save(order);
        
        logger.info("Order Service End ... Created order: {} ", order.getId());
        return order;
    }

}
