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

package com.alibaba.cloudapp.datasource.druid;


import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ConnectionRememberSetFilterTest {

    @Mock
    private FilterChain filterChain;

    @Mock
    private DruidDataSource dataSource;

    @Mock
    private DruidPooledConnection connection;

    @InjectMocks
    private ConnectionRememberSetFilter connectionRememberSetFilter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void dataSource_getConnection_ShouldAddConnectionToActiveConnections() throws SQLException {
        when(filterChain.dataSource_connect(dataSource, 1000L)).thenReturn(connection);

        DruidPooledConnection result = connectionRememberSetFilter.dataSource_getConnection(filterChain, dataSource, 1000L);

        assertEquals(connection, result);
        assertTrue(connectionRememberSetFilter.getActiveConnections().contains(connection));
    }

    @Test
    public void dataSource_releaseConnection_ShouldRemoveConnectionFromActiveConnections() throws SQLException {
        when(filterChain.dataSource_connect(dataSource, 1000L)).thenReturn(connection);
        connectionRememberSetFilter.dataSource_getConnection(filterChain, dataSource, 1000L);

        connectionRememberSetFilter.dataSource_releaseConnection(filterChain, connection);

        assertTrue(connectionRememberSetFilter.getActiveConnections().isEmpty());
    }

    @Test
    public void getActiveConnections_ShouldReturnCorrectActiveConnections() throws SQLException {
        when(filterChain.dataSource_connect(dataSource, 1000L)).thenReturn(connection);
        connectionRememberSetFilter.dataSource_getConnection(filterChain, dataSource, 1000L);

        Collection<DruidPooledConnection> activeConnections = connectionRememberSetFilter.getActiveConnections();

        assertEquals(1, activeConnections.size());
        assertTrue(activeConnections.contains(connection));
    }
}
