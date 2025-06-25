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

import com.alibaba.cloudapp.seata.demo.ResultHolder;
import com.alibaba.cloudapp.seata.demo.dao.AccountDao;
import com.alibaba.cloudapp.seata.demo.dao.AccountHistoryDao;
import com.alibaba.cloudapp.seata.demo.entity.Account;
import com.alibaba.cloudapp.seata.demo.entity.AccountHistory;
import com.alibaba.cloudapp.seata.demo.service.AccountService;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private AccountHistoryDao accountHistoryDao;
    
    @Override
    @Transactional
    public boolean prepare(BusinessActionContext context, String userId) {
        
        BigDecimal amount = (BigDecimal) ResultHolder.getResult(context.getXid(), "amount");
        
        boolean result = new Random().nextBoolean();
        Account account = accountDao.findByUserId(userId);
        String txId = context.getXid();
        
        logger.info("accountService prepare, txId: {}", txId);
        if (account != null && account.getAmount().compareTo(amount) > 0) {
            
            account.setAmount(account.getAmount().subtract(amount));
            accountDao.save(account);
            
            AccountHistory history = new AccountHistory();
            history.setUserId(userId);
            history.setUsername(account.getUsername());
            history.setAmount(amount);
            history.setTxId(txId);
            accountHistoryDao.save(history);
            
            return result;
        }
        return false;
    }
    
    @Override
    public boolean commit(BusinessActionContext context) {
        logger.info("accountService commit");
        return true;
    }
    
    @Override
    public boolean rollback(BusinessActionContext context) {
        logger.info("accountService rollback");
        String txId = context.getXid();
        AccountHistory history = accountHistoryDao.findByTxId(txId);
        
        if (history != null) {
            Account account = accountDao.findByUserId(history.getUserId());
            account.setAmount(account.getAmount().add(history.getAmount()));
            accountDao.save(account);
            accountHistoryDao.delete(history);
        }
        return true;
    }
    
    @Override
    public Account findByUserId(String userId) {
        return accountDao.findByUserId(userId);
    }
    
    @PostConstruct
    public void init() {
        Account account = accountDao.findByUserId("U001");
        if(account == null) {
            account = new Account();
            account.setUserId("U001");
            account.setUsername("testUser");
            account.setAmount(new BigDecimal(10000));
            accountDao.save(account);
        }
    }
}
