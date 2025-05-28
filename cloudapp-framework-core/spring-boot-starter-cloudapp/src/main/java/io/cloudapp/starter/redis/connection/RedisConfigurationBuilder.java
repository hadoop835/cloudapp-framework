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

package io.cloudapp.starter.redis.connection;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RedisConfigurationBuilder {

    private static final String TYPE_ERROR_PATTERN =
            "Redis configuration type must be one of [%s]";

    private int database;

    private String url;

    private String host;

    private String username;

    private String password;

    private int port;

    private boolean ssl;

    private RedisProperties.Sentinel sentinel;

    private RedisProperties.Cluster cluster;

    private RedisConfigurationBuilder() {}

    public static RedisConfigurationBuilder builder() {
        return new RedisConfigurationBuilder();
    }

    public int getDatabase() {
        return database;
    }

    public RedisConfigurationBuilder database(int database) {
        this.database = database;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RedisConfigurationBuilder url(String url) {
        this.url = url;
        return this;
    }

    public String getHost() {
        return host;
    }

    public RedisConfigurationBuilder host(String host) {
        this.host = host;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public RedisConfigurationBuilder username(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RedisConfigurationBuilder password(String password) {
        this.password = password;
        return this;
    }

    public int getPort() {
        return port;
    }

    public RedisConfigurationBuilder port(int port) {
        this.port = port;
        return this;
    }

    public boolean isSsl() {
        return ssl;
    }

    public RedisConfigurationBuilder ssl(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    public RedisProperties.Sentinel getSentinel() {
        return sentinel;
    }

    public RedisConfigurationBuilder sentinel(RedisProperties.Sentinel sentinel) {
        this.sentinel = sentinel;
        return this;
    }

    public RedisProperties.Cluster getCluster() {
        return cluster;
    }

    public RedisConfigurationBuilder cluster(RedisProperties.Cluster cluster) {
        if(cluster != null ) {
            this.cluster = cluster;
        }
        return this;
    }

    public boolean isCluster() {
        return cluster != null;
    }

    public boolean isSentinel() {
        return sentinel != null;
    }

    public RedisClusterConfiguration buildCluster() {
        if (isCluster()) {
            RedisClusterConfiguration configuration =
                    new RedisClusterConfiguration(cluster.getNodes());
            if(cluster.getMaxRedirects() != null) {
                configuration.setMaxRedirects(cluster.getMaxRedirects());
            }

            configuration.setPassword(password);
            configuration.setUsername(username);

            return configuration;
        }
        else {
            throw new IllegalArgumentException(String.format(TYPE_ERROR_PATTERN, "cluster"));
        }
    }

    public RedisStandaloneConfiguration buildStandalone() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        configuration.setHostName(host);
        configuration.setPassword(password);
        configuration.setPort(port);
        configuration.setUsername(username);
        configuration.setDatabase(database);

        return configuration;
    }

    public RedisSentinelConfiguration buildSentinel() {
        if(isSentinel()) {
            Set<String> nodes = sentinel.getNodes() == null ?
                    Collections.EMPTY_SET : new HashSet<>(sentinel.getNodes());

            RedisSentinelConfiguration configuration = new RedisSentinelConfiguration(sentinel.getMaster(), nodes);

            configuration.setDatabase(database);
            configuration.setPassword(password);
            configuration.setSentinelPassword(sentinel.getPassword());
            configuration.setSentinelUsername(sentinel.getUsername());
            configuration.setUsername(username);

            return configuration;
        }
        else {
            throw new IllegalArgumentException(String.format(TYPE_ERROR_PATTERN, "sentinel"));
        }
    }

}
