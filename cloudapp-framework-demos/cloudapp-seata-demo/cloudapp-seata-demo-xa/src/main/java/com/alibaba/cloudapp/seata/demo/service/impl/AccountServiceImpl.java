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

import com.alibaba.cloudapp.seata.demo.dao.AccountDao;
import com.alibaba.cloudapp.seata.demo.entity.Account;
import com.alibaba.cloudapp.seata.demo.service.AccountService;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    
    @Autowired
    private AccountDao accountDao;
    
    @Override
    @Transactional
    public boolean debit(String userId, BigDecimal amount) {
        
        Account account = accountDao.findByUserId(userId);
        String txId = RootContext.getXID();
        
        logger.info("accountService debit, txId: {}", txId);
        
        if(account == null || account.getAmount() == null
                || account.getAmount().compareTo(amount) < 0) {
            throw new RuntimeException("account not enough");
        }

        account.setAmount(account.getAmount().subtract(amount));
        accountDao.updateById(account);

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
            accountDao.insert(account);
        }
    }
}
