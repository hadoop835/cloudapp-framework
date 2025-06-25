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
import com.alibaba.cloudapp.seata.demo.dao.AccountDao;
import com.alibaba.cloudapp.seata.demo.dao.BillHistoryDao;
import com.alibaba.cloudapp.seata.demo.entity.Account;
import com.alibaba.cloudapp.seata.demo.entity.BillHistory;
import com.alibaba.cloudapp.seata.demo.service.AccountService;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Resource
    private AccountDao accountDao;
    @Resource
    private BillHistoryDao billHistoryDao;
    
    private final Random random = new Random();

    @Override
    public boolean debit(String businessKey, String userId, BigDecimal amount) {
        logger.info("Account Service ... xid: " + RootContext.getXID());
        logger.info("Deducting account uid: {}, amount: {}", userId, amount);

        Account account = accountDao.findByUserId(userId);
        
        if(account == null) {
            throw new CloudAppException("Account not exist");
        }
        
        account.setAmount(account.getAmount().subtract(amount));
        
        if(account.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new CloudAppException("Account balance is not enough");
        }
        
        accountDao.save(account);
        
        BillHistory billHistory = new BillHistory();
        billHistory.setUserId(userId);
        billHistory.setBusinessKey(businessKey);
        billHistory.setAmount(amount);
        billHistory.setUsername(account.getUsername());
        billHistory.setBillDate(LocalDateTime.now());
        billHistoryDao.save(billHistory);

        if(true) {
            throw new RuntimeException("random exception!");
        }
        
        return true;
    }
    
    
    @Override
    public boolean compensate(String businessKey) {
        BillHistory billHistory = billHistoryDao.findByBusinessKey(businessKey);
        if(billHistory != null) {
            logger.info("compensate billHistory: {}", billHistory.getId());
            Account account = accountDao.findByUserId(billHistory.getUserId());
            account.setAmount(account.getAmount().add(billHistory.getAmount()));
            accountDao.save(account);
        }
        return true;
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
