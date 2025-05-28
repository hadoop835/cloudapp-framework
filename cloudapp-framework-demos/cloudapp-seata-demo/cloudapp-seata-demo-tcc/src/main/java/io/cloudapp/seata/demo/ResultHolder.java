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

package io.cloudapp.seata.demo;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultHolder {
    
    private static final Map<String, Map<String, Object>> map =
            new ConcurrentHashMap<>();
    
    /**
     * save result
     * @param xid transaction id
     * @param key key
     * @param value value
     */
    public static void setResult(String xid, String key, Object value) {
        if (map.get(xid) == null) {
            synchronized (map) {
                if (map.get(xid) == null) {
                    Map<String, Object> results = new ConcurrentHashMap<>();
                    map.put(xid, results);
                }
            }
        }
        
        map.get(xid).put(key, value);
    }
    
    /**
     * get result value by key
     * @param xid transaction id
     * @param key key
     * @return result
     */
    public static Object getResult(String xid, String key) {
        Map<String, Object> results = map.getOrDefault(
                xid, Collections.emptyMap());
        
        return results.get(key);
    }
    
    public static void removeResult(String xid, String key) {
        Map<String, Object> results = map.get(xid);
        if (results != null) {
            results.remove(key);
        }
    }
    
    public static void removeResult(String xid) {
        map.remove(xid);
    }
    
}
