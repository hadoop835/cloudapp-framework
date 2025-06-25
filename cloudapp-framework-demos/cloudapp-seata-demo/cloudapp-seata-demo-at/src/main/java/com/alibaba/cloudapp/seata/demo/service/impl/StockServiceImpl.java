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

import com.alibaba.cloudapp.seata.demo.dao.StockDao;
import com.alibaba.cloudapp.seata.demo.entity.Stock;
import com.alibaba.cloudapp.seata.demo.service.StockService;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * The type Stock service.
 *
 * @author jimin.jm @alibaba-inc.com
 */
@Service
public class StockServiceImpl implements StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    @Resource
    private StockDao stockDao;

    @Override
    public void debit(String productCode, int count) {
        LOGGER.info("Stock Service Begin ... xid: " + RootContext.getXID());
        LOGGER.info("Deducting inventory stock, product: {}, count: {}",
                    productCode, count
        );
        
        Stock stock = stockDao.findByProductCode(productCode);
        
        if(stock == null) {
            throw new RuntimeException("product: " + productCode + " not exist");
        }

        stock.setQuantity(stock.getQuantity() - count);
        
        if(stock.getQuantity() < 0) {
            throw new RuntimeException("product: " + productCode + " stock is not enough");
        }
        
        stockDao.updateById(stock);
        
        LOGGER.info("Stock Service End ... ");
    }

}
