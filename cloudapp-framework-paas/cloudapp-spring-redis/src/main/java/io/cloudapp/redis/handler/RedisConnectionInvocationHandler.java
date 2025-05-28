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

package io.cloudapp.redis.handler;

import io.cloudapp.api.cache.interceptors.CacheDiskInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RedisConnectionInvocationHandler implements InvocationHandler {
    
    private static final Logger log =
            LoggerFactory.getLogger(RedisConnectionInvocationHandler.class);
    
    private final RedisConnection delegate;
    private final CacheDiskInterceptor cacheDiskInterceptor;
    
    private static final List<String> SKIP_METHODS = Arrays.asList(
            "execute", "exists", "type", "keys", "dump",
            "ttl", "pTtl", "echo", "sort", "scan", "randomKey",
            "encodingOf", "idletime", "refcount", "get", "getRange",
            "mGet", "getBit", "bitCount", "strLen", "bitPos",
            "lRange", "lIndex", "bgSave", "sIsMember", "lPos", "lLen", "lIndex",
            "sMIsMember", "sMembers", "dbSize", "flushDb", "sCard", "sDiff",
            "sInter", "sUnion", "sRandMember", "sScan", "flushAll", "xLen",
            "keyCommands", "gte", "gt", "lte", "lt", "getMin", "getMax",
            "zRandMember", "zRandMemberWithScore", "zRandMemberWithScore",
            "zRank", "zRevRank", "zRange", "zRangeWithScores", "zRangeByScore",
            "zRangeByScoreWithScores", "zRevRange", "zRevRangeWithScores",
            "zRevRangeByScore", "zRevRangeByScoreWithScores", "zCount",
            "zLexCount", "zCard", "zScore", "zMScore", "zDiff", "zUnion",
            "zDiffWithScores", "", "zInterWithScores", "zScan",
            "zRangeByLex", "zRevRangeByLex", "zUnionWithScores",
            "zInterWithScores", "zInter", "hGet", "hMGet", "hExists",
            "hGetAll", "hLen", "hVals", "hRandField", "hRandFieldWithValues",
            "hKeys", "hRandField", "hRandFieldWithValues", "hScan", "hStrLen"
            , "watch", "ping", "xAck", "xInfo", "xInfoGroups",
            "xInfoConsumers", "xLen", "xPending", "xRange", "xRead",
            "xReadGroup", "xRevRange", "geoDist", "geoHash", "geoPos",
            "geoRadius", "geoRadiusByMember", "geoSearch", "geoSearchStore",
            "pfCount", "commands", "zSetCommands", "pfAdd", "pfMerge",
            "hashCommands", "geoCommands", "hyperLogLogCommands",
            "listCommands", "setCommands", "scriptingCommands",
            "serverCommands", "streamCommands", "stringCommands"
    );
    
    
    public RedisConnectionInvocationHandler(
            RedisConnection delegate,
            CacheDiskInterceptor cacheDiskInterceptor
    ) {
        this.delegate = delegate;
        this.cacheDiskInterceptor = cacheDiskInterceptor;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Class<?> cls = method.getDeclaringClass();
        
        Object value = method.invoke(delegate, args);
        
        if (SKIP_METHODS.contains(method.getName())) {
            return value;
        }
        
        // Skip the data acquisition method to avoid infinite loop of the
        // same RedisTemplate
        Method interfaceMethod = getInterfaceMethod(
                method, RedisConnection.class);
        if (interfaceMethod != null) {
            handleRedisConnection(interfaceMethod, args);
        } else if ((interfaceMethod = getInterfaceMethod(
                method, RedisClusterConnection.class)) != null) {
            handleClusterRedisConnection(interfaceMethod, args);
        } else if ((interfaceMethod = getInterfaceMethod(
                method, StringRedisConnection.class)) != null) {
            handleStringRedisConnection(interfaceMethod, args);
        }
        
        return value;
    }
    
    private void handleRedisConnection(Method method, Object[] args) {
        log.debug("handle connection method: {}", method.getName());
        List<byte[]> keys = new ArrayList<>();
        for (int i = 0; i < method.getParameters().length; i++) {
            String parameterName = method.getParameters()[i].getName();
            switch (parameterName) {
                case "key":
                case "destinationKey":
                case "dstKey":
                case "dest":
                    keys.add((byte[]) args[i]);
                case "keys":
                    if (args[i] instanceof byte[][]) {
                        keys.addAll(Arrays.stream((byte[][]) args[i]).collect(
                                Collectors.toList()));
                    } else {
                        keys.add((byte[]) args[i]);
                    }
                    break;
            }
        }
        if (!keys.isEmpty()) {
            cacheDiskInterceptor.notifyChanged(keys);
        }
    }
    
    private void handleClusterRedisConnection(Method method, Object[] args) {
        log.debug("handle cluster connection method: {}", method.getName());
    }
    
    private void handleStringRedisConnection(Method method, Object[] args) {
        log.debug("handle string connection method: {}", method.getName());
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < method.getParameters().length; i++) {
            String parameterName = method.getParameters()[i].getName();
            switch (parameterName) {
                case "key":
                case "destinationKey":
                case "dstKey":
                case "dest":
                    keys.add((String) args[i]);
                case "keys":
                    if (args[i] instanceof String[]) {
                        keys.addAll(Arrays.stream((String[]) args[i])
                                          .collect(Collectors.toList()));
                    } else {
                        keys.add((String) args[i]);
                    }
            }
        }
        if (!keys.isEmpty()) {
            cacheDiskInterceptor.notifyChanged(
                    keys.stream().map(String::getBytes)
                        .collect(Collectors.toList()));
        }
    }
    
    private Method getInterfaceMethod(Method method, Class<?> cls) {
        try {
            return cls.getMethod(
                    method.getName(), method.getParameterTypes()
            );
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
}
