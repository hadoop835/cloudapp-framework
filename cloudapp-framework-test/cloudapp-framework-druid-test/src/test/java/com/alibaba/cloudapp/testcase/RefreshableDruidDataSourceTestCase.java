/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloudapp.testcase;

import com.alibaba.cloudapp.demo.app.DemoApplication;
import com.alibaba.cloudapp.datasource.druid.RefreshableDruidDataSourceWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author alickreborn
 */
@SpringBootTest(classes = DemoApplication.class)
public class RefreshableDruidDataSourceTestCase {
    @Resource
    private RefreshableDruidDataSourceWrapper refreshableDataSource;

    @Test
    public void testDataSourceExists() throws Exception {
        assertThat(refreshableDataSource).isNotNull();
    }

    @Test
    public void testDataSourcePropertiesOverridden() throws Exception {
//        assertThat(refreshableDataSource.getUrl()).isEqualTo("jdbc:h2:file:./demo-db;NON_KEYWORDS=user");
//        assertThat(refreshableDataSource.getInitialSize()).isEqualTo(2);
//        assertThat(refreshableDataSource.getMaxActive()).isEqualTo(30);
//        assertThat(refreshableDataSource.getMinIdle()).isEqualTo(2);
//        assertThat(refreshableDataSource.getMaxWait()).isEqualTo(1234);
//        assertThat(refreshableDataSource.isPoolPreparedStatements()).isTrue();
//        //assertThat(dataSource.getMaxOpenPreparedStatements()).isEqualTo(5); //Duplicated with following
//        assertThat(refreshableDataSource.getMaxPoolPreparedStatementPerConnectionSize()).isEqualTo(5);
//        assertThat(refreshableDataSource.getValidationQuery()).isEqualTo("select 1");
//        assertThat(refreshableDataSource.getValidationQueryTimeout()).isEqualTo(1);
//        assertThat(refreshableDataSource.isTestWhileIdle()).isTrue();
//        assertThat(refreshableDataSource.isTestOnBorrow()).isTrue();
//        assertThat(refreshableDataSource.isTestOnReturn()).isTrue();
//        assertThat(refreshableDataSource.getTimeBetweenEvictionRunsMillis()).isEqualTo(10000);
//        assertThat(refreshableDataSource.getMinEvictableIdleTimeMillis()).isEqualTo(30001);
//        assertThat(refreshableDataSource.isAsyncCloseConnectionEnable()).isEqualTo(true);
    }
}
