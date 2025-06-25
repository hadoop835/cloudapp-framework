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
package com.alibaba.cloudapp.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class SecurityUtil {
    
    private static final Collection<String> KEYS = Arrays.asList(
            "secret", "access", "user", "password"
    );
    
    public static Properties safeMask(Properties properties) {
        return safeMask(properties, KEYS);
    }
    
    public static Properties safeMask(Properties properties,
                                      Collection<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return properties;
        }
        
        Properties copy = new Properties();
        properties.forEach((k, v) -> {
            if (!(k instanceof String) || !(v instanceof String)) {
                copy.put(k, v);
                return;
            }
            
            if (keys.stream().anyMatch(
                    key -> ((String) k).toLowerCase().contains(key))) {
                copy.put(k, mask((String) v));
            } else {
                copy.put(k, v);
            }
        });
        
        return copy;
    }
    
    private static String mask(String v) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length(); i++) {
            if (i < v.length() / 2) {
                sb.append(v.charAt(i));
            } else {
                sb.append('*');
            }
        }
        
        return sb.toString();
    }
    
}
