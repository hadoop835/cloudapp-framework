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
import com.alibaba.cloudapp.seata.demo.dao.StockDao;
import com.alibaba.cloudapp.seata.demo.entity.Stock;
import com.alibaba.cloudapp.seata.demo.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class StockServiceImpl implements StockService {
    
    private final static Logger logger =
            LoggerFactory.getLogger(StockServiceImpl.class);
    
    @Autowired
    private StockDao stockDao;
    
    @Override
    @Transactional
    public Stock debit(String productCode, int count) {
        
        
        Stock stock = stockDao.findByProductCode(productCode);
        if (stock == null || stock.getQuantity() < count) {
            throw new CloudAppException("product: " + productCode + " stock " +
                                                "is not enough");
        }
        stock.setQuantity(stock.getQuantity() - count);
        stockDao.save(stock);
        
        return stock;
    }
    
    @Override
    public Stock findByProductCode(String productCode) {
        return stockDao.findByProductCode(productCode);
    }
    
    @PostConstruct
    public void init() {
        Stock stock = stockDao.findByProductCode("P001");
        if (stock == null) {
            stock = new Stock();
            stock.setProductCode("P001");
            stock.setProductName("product-001");
            stock.setQuantity(100);
            stock.setPrice(new BigDecimal(100));
            stockDao.save(stock);
        }
    }
    
}
