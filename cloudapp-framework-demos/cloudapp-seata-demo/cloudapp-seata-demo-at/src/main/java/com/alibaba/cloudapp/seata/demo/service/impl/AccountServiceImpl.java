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
import com.alibaba.cloudapp.seata.demo.entity.Account;
import com.alibaba.cloudapp.seata.demo.service.AccountService;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Resource
    private AccountDao accountDao;

    @Override
    @Transactional
    public void debit(String userId, BigDecimal amount) {
        LOGGER.info("Account Service ... xid: " + RootContext.getXID());
        LOGGER.info("Deducting account uid: {}, amount: {}", userId, amount);

        Account account = accountDao.findByUserId(userId);
        
        if(account == null) {
            throw new CloudAppException("Account not exist");
        }
        
        account.setMoney(account.getMoney().subtract(amount));
        
        if(account.getMoney().compareTo(BigDecimal.ZERO) < 0) {
            throw new CloudAppException("Account balance is not enough");
        }
        
        accountDao.updateById(account);
        
        LOGGER.info("Account Service End ... ");
    }
    
}
