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

package io.cloudapp.datasource.druid;


import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import io.cloudapp.exeption.CloudAppTimeoutException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RefreshableDruidDataSourceWrapperTest {
    
    @Mock
    private DruidDataSource druidDataSource;
    
    @Mock
    private DataSourceProperties basicProperties;
    
    @Mock
    private ReadWriteLock mockReadWriteLock;
    
    @Mock
    private Lock lock;
    
    @Mock
    private DruidConnectionHolder holder;
    
    @Mock
    private Connection connection;
    
    @Mock
    private Socket socket;
    
    
    @Mock
    private DruidPooledConnection mockConnection;
    
    @Mock
    private DruidDataSourceWrapper wrapper;
    
    @Spy
    @InjectMocks
    private RefreshableDruidDataSourceWrapper refreshableWrapper;
    
    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        
        when(basicProperties.determineUsername()).thenReturn("root");
        when(basicProperties.determinePassword()).thenReturn("root");
        when(basicProperties.determineUrl()).thenReturn(
                "url=jdbc:mysql://rm-wz93hp7350v59pag3qo.mysql.rds.aliyuncs.com:3306/demo?useSSL\\=false&serverTimezone\\=UTC");
        when(basicProperties.getDriverClassName()).thenReturn(
                "com.mysql.jdbc.Driver");
    }
    
    public void setPrivateField(Object object, String fieldName, boolean value)
            throws NoSuchFieldException, IllegalAccessException {
        Field refreshingField = RefreshableDruidDataSourceWrapper.class.getDeclaredField(
                fieldName);
        refreshingField.setAccessible(true);
        
        refreshingField.set(object, value);
    }
    
    private Object getPrivateField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = RefreshableDruidDataSourceWrapper.class.getDeclaredField(
                fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
    
    private void tryBrutalKill()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method tryBrutalKillMethod = RefreshableDruidDataSourceWrapper.class.getDeclaredMethod(
                "tryBrutalKill");
        tryBrutalKillMethod.setAccessible(true);
        tryBrutalKillMethod.invoke(refreshableWrapper);
    }
    
    private void tryGracefullShutdown()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method tryGracefullShutdownMethod = RefreshableDruidDataSourceWrapper.class.getDeclaredMethod(
                "tryGracefullShutdown");
        tryGracefullShutdownMethod.setAccessible(true);
        tryGracefullShutdownMethod.invoke(refreshableWrapper);
    }
    
    private void tryDiscardConnection()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method tryDiscardConnectionMethod = RefreshableDruidDataSourceWrapper.class.getDeclaredMethod(
                "tryDiscardConnection", DruidConnectionHolder.class);
        tryDiscardConnectionMethod.setAccessible(true);
        tryDiscardConnectionMethod.invoke(refreshableWrapper, holder);
    }
    
    private void gracefullRestart()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method gracefullRestartMethod = RefreshableDruidDataSourceWrapper.class.getDeclaredMethod(
                "gracefullRestart");
        gracefullRestartMethod.setAccessible(true);
        gracefullRestartMethod.invoke(refreshableWrapper);
    }
    
    @Test
    public void test_RefreshableDruidDataSourceWrapper() {
        refreshableWrapper = new RefreshableDruidDataSourceWrapper();
        List<String> filterClassNames = refreshableWrapper.getFilterClassNames();
        assertTrue(filterClassNames.contains(
                ConnectionRememberSetFilter.class.getName()));
    }
    
    @Test
    public void resetProperties_PropertiesResetSuccessfully() throws Exception {
        Method resetPropertiesMethod = RefreshableDruidDataSourceWrapper.class.getDeclaredMethod(
                "resetProperties");
        resetPropertiesMethod.setAccessible(true);
        resetPropertiesMethod.invoke(refreshableWrapper);
        
        assertEquals("root", refreshableWrapper.getUsername());
        assertEquals("root", refreshableWrapper.getPassword());
        assertEquals(
                "url=jdbc:mysql://rm-wz93hp7350v59pag3qo.mysql.rds.aliyuncs.com:3306/demo?useSSL\\=false&serverTimezone\\=UTC",
                refreshableWrapper.getUrl()
        );
        assertEquals("com.mysql.jdbc.Driver",
                     refreshableWrapper.getDriverClassName()
        );
    }
    
    @Test(expected = CloudAppTimeoutException.class)
    public void getConnectionDirect_RefreshingTrue_LockInterrupted_ThrowsException()
            throws Exception {
        setPrivateField(refreshableWrapper, "refreshing", true);
        when(lock.tryLock(anyLong(), any())).thenThrow(
                new InterruptedException());
        
        refreshableWrapper.getConnectionDirect(1000);
    }
    
    @Test(expected = CloudAppTimeoutException.class)
    public void getConnectionDirect_RefreshingTrue_LockTimeout_ThrowsException()
            throws Exception {
        setPrivateField(refreshableWrapper, "refreshing", true);
        when(lock.tryLock(anyLong(), any())).thenReturn(false);
        
        refreshableWrapper.getConnectionDirect(1000);
    }
    
    @Test
    public void getMaxRefreshWaitMillis_DefaultValue_ReturnsDefaultValue() {
        long expectedMaxRefreshWaitMillis = 1000;
        long actualMaxRefreshWaitMillis = refreshableWrapper.getMaxRefreshWaitMillis();
        assertEquals(expectedMaxRefreshWaitMillis, actualMaxRefreshWaitMillis);
    }
    
    @Test
    public void setMaxRefreshWaitMillis_ValidInput_SetsCorrectly() {
        long newMaxRefreshWaitMillis = 5000;
        refreshableWrapper.setMaxRefreshWaitMillis(newMaxRefreshWaitMillis);
        assertEquals(newMaxRefreshWaitMillis,
                     refreshableWrapper.getMaxRefreshWaitMillis()
        );
    }
    
    @Test
    public void tryDiscardConnection_SuccessfulDiscard_LogsInfo()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        tryDiscardConnection();
        
        verify(refreshableWrapper).discardConnection(holder);
    }
    
    @Test(expected = Throwable.class)
    public void tryDiscardConnection_ExceptionThrown_LogsWarning()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        when(druidDataSource.discardConnection(holder)).thenThrow(
                new SQLException("Test exception"));
        
        tryDiscardConnection();
        
    }
    
    @Test
    public void tryBrutalKill_NoActiveConnections_LogsInfoAndReturns()
            throws Exception {
        tryBrutalKill();
        
        verify(druidDataSource, never()).getConnectionDirect(anyLong());
    }
    
    @Test
    public void tryBrutalKill_ActiveCountIsOne()
            throws Exception {
        when(refreshableWrapper.getActiveCount()).thenReturn(1);
        
        tryBrutalKill();
        
    }
    
    @Test
    public void tryGracefullShutdown_ActiveConnectionsZero_ExitsEarly()
            throws Exception {
        when(refreshableWrapper.getActiveCount()).thenReturn(0);
        
        tryGracefullShutdown();

        verify(druidDataSource, never()).getConnectionDirect(anyLong());
    }
    
    @Test
    public void tryGracefullShutdown_ActiveConnectionsRemain_ExitsAfterMaxWait()
            throws Exception {
        when(refreshableWrapper.getActiveCount()).thenReturn(1);
        
        tryGracefullShutdown();

        verify(refreshableWrapper, times(1)).close();
    }
    
    @Test
    public void gracefullRestart_SuccessfulGracefulShutdownAndRestart()
            throws Exception {
        doNothing().when(refreshableWrapper).init();
        
        gracefullRestart();
        
        verify(refreshableWrapper, times(1)).init();
    }
    
    @Test
    public void gracefullRestart_ExceptionDuringRestart() throws Exception {
        doNothing().when(refreshableWrapper).init();
        
        gracefullRestart();
        
        verify(refreshableWrapper, times(1)).init();
    }
    
    @Test
    public void destroy_RefreshingTrue_NoAction() throws Exception {
        setPrivateField(refreshableWrapper, "refreshing", true);
        
        refreshableWrapper.destroy();
        
        assertTrue((boolean) getPrivateField(refreshableWrapper, "refreshing"));
    }
    
    @Test
    public void destroy_RefreshingFalse_LockAcquiredAndReleased()
            throws Exception {
        setPrivateField(refreshableWrapper, "refreshing", false);
        
        refreshableWrapper.destroy();
        
        assertTrue((boolean) getPrivateField(refreshableWrapper, "refreshing"));
    }
    
    
    @Test
    public void restart_GracefullRestartCalled() throws Exception {
        refreshableWrapper.restart();
        
        verifyNoInteractions(lock);
    }
    
    
    @Test
    public void afterPropertiesSet_DEFAULT_REFRESH_WAIT_MILLIS() throws Exception {
        doNothing().when(refreshableWrapper).init();
        
        refreshableWrapper.setMaxRefreshWaitMillis(0);
        
        refreshableWrapper.afterPropertiesSet();
        
        assertEquals(1000,
                     refreshableWrapper.getMaxRefreshWaitMillis());
        
    }
    
    @Test
    public void afterPropertiesSet_callRestart() throws Exception {
        doNothing().when(refreshableWrapper).restart();
        setPrivateField(refreshableWrapper, "refreshing", true);
        refreshableWrapper.afterPropertiesSet();
        verify(refreshableWrapper, times(1)).restart();
    }
    
    
}
