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

import io.cloudapp.seata.demo.dao.StockDao;
import io.cloudapp.seata.demo.dao.StockHistoryDao;
import io.cloudapp.seata.demo.entity.Stock;
import io.cloudapp.seata.demo.entity.StockHistory;
import io.cloudapp.seata.demo.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;

@Service("stockService")
public class StockServiceImpl implements StockService {

    private static final Logger logger =
            LoggerFactory.getLogger(StockService.class);

    @Resource
    private StockDao stockDao;
    @Resource
    private StockHistoryDao stockHistoryDao;

    @Override
    public boolean debit(String businessKey, String productCode, int count) {
        
        Stock stock = stockDao.findByProductCode(productCode);
        
        if(stock == null) {
            throw new RuntimeException("product: " + productCode + " not exist");
        }

        stock.setQuantity(stock.getQuantity() - count);
        
        if(stock.getQuantity() < 0) {
            throw new RuntimeException("product: " + productCode + " stock is not enough");
        }
        
        stockDao.save(stock);
        
        StockHistory stockHistory = new StockHistory();
        stockHistory.setProductCode(stock.getProductCode());
        stockHistory.setProductName(stock.getProductName());
        stockHistory.setCount(count);
        stockHistory.setPrice(stock.getPrice());
        stockHistory.setBusinessKey(businessKey);
        stockHistoryDao.save(stockHistory);
        
        return true;
    }
    
    @Override
    public boolean compensate(String businessKey) {
        StockHistory stockHistory = stockHistoryDao.findByBusinessKey(businessKey);
        if(stockHistory != null) {
            Stock stock = stockDao.findByProductCode(stockHistory.getProductCode());
            stock.setQuantity(stock.getQuantity() + stockHistory.getCount());
            stockDao.save(stock);
        }
        return true;
    }
    
    @Override
    public Stock findByProductCode(String productCode) {
        return stockDao.findByProductCode(productCode);
    }
    
    @PostConstruct
    public void init() {
        Stock stock = stockDao.findByProductCode("P001");
        if(stock == null) {
            stock = new Stock();
            stock.setProductCode("P001");
            stock.setProductName("product-001");
            stock.setQuantity(100);
            stock.setPrice(new BigDecimal(100));
            stockDao.save(stock);
        }
    }
}
