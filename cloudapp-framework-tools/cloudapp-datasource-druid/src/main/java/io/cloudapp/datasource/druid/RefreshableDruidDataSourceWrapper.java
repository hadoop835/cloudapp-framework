/*
 * Copyright 1999-2024 Alibaba Group Holding Ltd.
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
package io.cloudapp.datasource.druid;

import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import io.cloudapp.exeption.CloudAppTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RefreshableDruidDataSourceWrapper extends DruidDataSourceWrapper
        implements InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(RefreshableDruidDataSourceWrapper.class);
    public static final int SLEEP_MILLIS = 100;

    @Autowired
    private DataSourceProperties basicProperties;

    private static final long DEFAULT_REFRESH_WAIT_MILLIS = 1000;

    private long maxRefreshWaitMillis = DEFAULT_REFRESH_WAIT_MILLIS;

    private ReadWriteLock refershLock = new ReentrantReadWriteLock();

    private Lock readLock = refershLock.readLock();

    private Lock writeLock = refershLock.writeLock();

    private volatile boolean refreshing = false;

    private static final Field connectionsField;

    private static final Field activeCountField;

    static {
        try {
            connectionsField = DruidDataSource.class.getDeclaredField("connections");
            activeCountField = DruidDataSource.class.getDeclaredField("activeCount");

            connectionsField.setAccessible(true);
            activeCountField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionRememberSetFilter filter = new ConnectionRememberSetFilter();


    public RefreshableDruidDataSourceWrapper() {
        super();

        autoAddFilters(Collections.singletonList(filter));
    }

    public long getMaxRefreshWaitMillis() {
        return maxRefreshWaitMillis;
    }

    public void setMaxRefreshWaitMillis(long maxRefreshWaitMillis) {
        this.maxRefreshWaitMillis = maxRefreshWaitMillis;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (maxRefreshWaitMillis <= 0) {
            maxRefreshWaitMillis = DEFAULT_REFRESH_WAIT_MILLIS;
        }

        if (!refreshing) {
            super.afterPropertiesSet();
            return;
        }

        restart();
    }

    @Override
    public void restart() {
        Runnable runnable = () -> this.gracefullRestart();
        new Thread(runnable, "DataSource-Restart-Runner").start();
    }

    private void gracefullRestart() {
        writeLock.lock();
        try {
            tryGracefullShutdown();
        } catch (Throwable t) {
            log.warn("Close connection error, but we still need to reset the connection, " +
                    "may cause unknown issue", t);
        }

        try {
            resetProperties();

            super.restart();

            init();

        } catch (Throwable e) {
            log.error("Error while restart datasource.", e);

            try {
                init();
            } catch (Throwable ex) {
                log.error("Error while init datasource after restart failed.", ex);
            }
        } finally {
            refreshing = false;
            writeLock.unlock();
        }
    }

    private void resetProperties() throws IllegalAccessException {
        inited = false;
        // activeCountField.set(this, 0);

        super.setUsername(basicProperties.determineUsername());
        super.setPassword(basicProperties.determinePassword());
        super.setUrl(basicProperties.determineUrl());
        super.setDriverClassName(basicProperties.getDriverClassName());
    }

    private void tryGracefullShutdown() throws InterruptedException {

        long maxLoop = maxRefreshWaitMillis / SLEEP_MILLIS;
        log.warn("Waiting to brutal kill underlying connections after {} millis at a max.",
                maxRefreshWaitMillis);

        for (int i = 0; i < maxLoop; i++) {
            if (getActiveCount() <= 0) {
                break;
            }

            Thread.sleep(SLEEP_MILLIS);
        }

        tryBrutalKill();

        // brutal force close
        log.warn("Try to brutal kill underlying connections.");
        this.close();
        log.warn("Done with brutal kill underlying connections.");
    }

    private void tryBrutalKill() {
        if (getActiveCount() <= 0) {
            log.info("Great ! we don't need to break any connections.");
            return;
        }


        log.warn("Try to kill connections one by one.");

        DruidConnectionHolder[] connections = null;
        try {
            connections = (DruidConnectionHolder[]) connectionsField.get(this);
        } catch (IllegalAccessException e) {
            log.error("Failed to get underlying connections throw reflection: {}", e.getMessage());
        }

        if (connections == null || connections.length == 0) {
            log.info("Found no connections to discard.");
            return;
        }

        Arrays.stream(connections)
                .filter(c -> c != null)
                .forEach( this::tryDiscardConnection);

        filter.getActiveConnections()
                .stream()
                .forEach((conn) -> this.tryDiscardConnection(conn.getConnectionHolder()));
    }

    private void tryDiscardConnection(DruidConnectionHolder holder) {
        try {
            log.info("Discard Connection: {}", holder);
            discardConnection(holder);
        } catch (Throwable e) {
            log.warn("Brutal discard connection error: {}", e.getMessage());
        }
    }

    @Override
    public DruidPooledConnection getConnectionDirect(long maxWaitMillis) throws SQLException {

        if (!refreshing) {
            return super.getConnectionDirect(maxWaitMillis);
        }

        long maxWait = maxWaitMillis > 0 ? maxWaitMillis : maxRefreshWaitMillis;
        try {
            if (readLock.tryLock(maxWait, TimeUnit.MILLISECONDS)) {
                try {
                    return super.getConnectionDirect(maxWaitMillis);
                } finally {
                    readLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while get connection.", e);
            throw new CloudAppTimeoutException("CloudApp.DruidPoolTimeout", e);
        }

        if (!refreshing) {
            return super.getConnectionDirect(maxWaitMillis);
        }

        String msg = String.format("Get connection timeout after %s milliseconds", maxWait);
        throw new CloudAppTimeoutException(msg, "CloudApp.DruidPoolTimeout");
    }

    @Override
    public void destroy() throws Exception {
        if (refreshing) {
            return;
        }

        writeLock.lock();
        try {
            refreshing = true;
        } finally {
            writeLock.unlock();
        }

    }
}
