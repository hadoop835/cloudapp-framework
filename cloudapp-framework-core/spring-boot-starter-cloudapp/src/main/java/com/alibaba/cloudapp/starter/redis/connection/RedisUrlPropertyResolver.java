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

package com.alibaba.cloudapp.starter.redis.connection;

import com.alibaba.cloudapp.sequence.exception.IllegalRedisUrlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RedisUrlPropertyResolver {

    private static final Logger logger = LoggerFactory.getLogger(RedisUrlPropertyResolver.class);

    private final Optional<String> schema;

    private Optional<String> username = Optional.empty();

    private Optional<String> password = Optional.empty();

    private final Optional<String> host;

    private Optional<Integer> port = Optional.empty();

    private Optional<Duration> timeout = Optional.empty();

    private Optional<Integer> database = Optional.empty();

    private final boolean useSsl;

    /**
     *
     * Redis Standalone<br/>
     * redis://[:password@]host[:port][/database][?[timeout=timeout[d|h|m|s|ms|us|ns]]
     * [&database=database]]<br/>
     * <br/>
     * Redis Standalone (SSL)<br/>
     * rediss://[:password@]host[:port][/database][?[timeout=timeout[d|h|m|s|ms|us|ns]]
     * [&database=database]]<br/>
     *
     * @param url redis url
     */
    public RedisUrlPropertyResolver(String url) {
        try {
            URI uri = new URI(url);

            if(!"redis".equals(uri.getScheme()) && !"rediss".equals(uri.getScheme())) {
                throw new IllegalRedisUrlException("not support scheme " + uri.getScheme());
            }

            this.schema = Optional.of(uri.getScheme());
            this.useSsl = "rediss".equals(uri.getScheme());

            if (uri.getUserInfo() != null) {
                initUserInfo(uri.getUserInfo());
            }

            host = Optional.of(uri.getHost());
            if(uri.getPort() > 0 && url.contains(":" + uri.getPort())) {
                port = Optional.of(uri.getPort());
            }

            Map<String, String> params = initParameters(uri.getQuery());

            if(params.containsKey("timeout") && params.get("timeout") != null) {
                String timeDurationString = params.get("timeout").toUpperCase();
                timeDurationString = timeDurationString.startsWith("PT") ?
                        timeDurationString : "PT" + timeDurationString;
                timeout = Optional.of(Duration.parse(timeDurationString));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("timeout is not set, use default timeout");
                }
            }

            if(params.containsKey("database") && params.get("database") != null) {
                database = Optional.of(Integer.valueOf(params.get("database")));
            } else if(StringUtils.hasText(uri.getPath())) {
                String db = uri.getPath().substring(1);
                database = Optional.of(Integer.valueOf(db));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("database is not set, use default database");
                }
            }

        }
        catch (URISyntaxException ex) {
            throw new IllegalRedisUrlException(url, ex);
        }
    }

    private Map<String, String> initParameters(String query) {
        if(!StringUtils.hasText(query)) {
            return Collections.emptyMap();
        }

        String[] kvs = query.split("[&|=]");

        int size = (kvs.length + 1) / 2;
        Map<String, String> map = new HashMap<>(size);

        int index = 0;
        for (; index < kvs.length - 1; index ++ ) {
            map.put(kvs[index], kvs[++ index]);
        }

        if(index < kvs.length) {
            map.put(kvs[index], "");
        }

        return map;
    }

    private void initUserInfo(String userInfo) {
        int index = userInfo.indexOf(':');
        if (index >= 0) {
            this.username = Optional.of(userInfo.substring(0, index));
            this.password = Optional.of(userInfo.substring(index + 1));
        }
        else {
            this.password = Optional.of(userInfo);
        }
    }

    public void assignment(RedisProperties properties) {
        properties.setSsl(this.isUseSsl());
        database.ifPresent(properties::setDatabase);
        username.ifPresent(properties::setUsername);
        password.ifPresent(properties::setPassword);
        host.ifPresent(properties::setHost);
        port.ifPresent(properties::setPort);
        timeout.ifPresent(properties::setTimeout);
    }

    public Optional<String> getSchema() {
        return schema;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getPassword() {
        return password;
    }

    public Optional<String> getHost() {
        return host;
    }

    public Optional<Integer> getPort() {
        return port;
    }

    public Optional<Duration> getTimeout() {
        return timeout;
    }

    public Optional<Integer> getDatabase() {
        return database;
    }

    public boolean isUseSsl() {
        return useSsl;
    }
}
