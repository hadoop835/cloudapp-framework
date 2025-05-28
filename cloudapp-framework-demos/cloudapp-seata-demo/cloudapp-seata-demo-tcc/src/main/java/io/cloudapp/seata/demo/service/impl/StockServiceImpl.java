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
import io.cloudapp.seata.demo.dao.StockDao;
import io.cloudapp.seata.demo.dao.StockHistoryDao;
import io.cloudapp.seata.demo.entity.Stock;
import io.cloudapp.seata.demo.entity.StockHistory;
import io.cloudapp.seata.demo.service.StockService;
import io.seata.rm.tcc.api.BusinessActionContext;
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
    @Autowired
    private StockHistoryDao stockHistoryDao;
    
    @Override
    @Transactional
    public boolean prepare(BusinessActionContext context, String productCode,
                         int count) {
        
        if(context == null) {
            throw new RuntimeException("context is null");
        }
        
        logger.info("stockService prepare: {}", context.getXid());
        
        Stock stock = stockDao.findByProductCode(productCode);
        if (stock == null || stock.getQuantity() < count) {
            return false;
        }
        stock.setQuantity(stock.getQuantity() - count);
        stockDao.save(stock);
        
        StockHistory stockHistory = new StockHistory();
        stockHistory.setProductCode(stock.getProductCode());
        stockHistory.setProductName(stock.getProductName());
        stockHistory.setQuantity(count);
        stockHistory.setPrice(stock.getPrice());
        stockHistory.setTxId(context.getXid());
        stockHistoryDao.save(stockHistory);
        
        ResultHolder.setResult(context.getXid(), "price", stock.getPrice());
        
        return true;
    }
    
    @Override
    public boolean commit(BusinessActionContext context) {
        logger.info("stockService commit");
        ResultHolder.removeResult(context.getXid());
        return true;
    }
    
    @Override
    public boolean rollback(BusinessActionContext context) {
        logger.info("accountService rollback");
        ResultHolder.removeResult(context.getXid());
        
        StockHistory history = stockHistoryDao.findByTxId(context.getXid());
        if (history != null) {
            Stock stock = stockDao.findByProductCode(history.getProductCode());
            int count = history.getQuantity();
            stock.setQuantity(stock.getQuantity() + count);
            stockDao.save(stock);
            stockHistoryDao.delete(history);
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
