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

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class JsonUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    public static TreeMap<String, Object> jsonStringToTreeMap(String jsonString) {
        JSONObject object = (JSONObject) JSON.parse(jsonString);
        return formatJsonObj(object);
    }

    public static TreeMap<String, Object> formatJsonObj(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        TreeMap<String, Object> treeMap = new TreeMap<>();
        Set<String> keys = jsonObject.keySet();

        for (String key : keys) {
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                value = formatJsonObj((JSONObject) value);
            }

            if (value instanceof JSONArray) {
                value = formatJsonArray((JSONArray) value);
            }

            treeMap.put(key, value);
        }
        return treeMap;
    }

    public static JSONArray formatJsonArray(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return jsonArray;
        }

        JSONArray ret = new JSONArray();

        for (Object o : jsonArray) {
            if (o instanceof JSONArray) {
                ret.add(formatJsonArray((JSONArray) o));
            } else if (o instanceof JSONObject) {
                ret.add(formatJsonObj((JSONObject) o));
            } else {
                ret.add(o);
            }
        }

        return ret;
    }
    
    public static <T> Object[] toJSONObject(T[] objs) {
        if(objs == null) {
            return null;
        }
        
        if(objs.length == 0) {
            return new JSONObject[0];
        }
        
        Object[] rets = new Object[objs.length];
        for (int i = 0; i < objs.length; i++) {
            rets[i] = toJSONObject(objs[i]);
        }
        return rets;
    }
    
    public static List<?> toJSONObject(Collection<?> objs) {
        if(objs == null) {
            return null;
        }
        
        return objs.stream()
                   .map(JsonUtil::toJSONObject)
                   .collect(Collectors.toList());
    }
    
    public static <K,V> JSONObject toJSONObject(Map<K, V> obj) {
        if(obj == null) {
            return null;
        }
        
        JSONObject json = new JSONObject();
        obj.forEach((k, v) -> json.put(k.toString(), toJSONObject(v)));
        return json;
    }
    
    public static Object toJSONObject(Object obj) {
        if(obj == null) {
            return null;
        }
        
        if (obj instanceof Map) {
            return toJSONObject((Map) obj);
        }
        
        if (obj instanceof Collection) {
            return toJSONObject((Collection) obj);
        }
        
        if (obj.getClass().isArray()) {
            return toJSONObject((Object[]) obj);
        }
        
        if (obj.getClass().isPrimitive() || obj instanceof String
                || obj instanceof Number || obj instanceof Boolean
                || obj.getClass().isEnum()
        ) {
            return obj;
        }
        
        JSONObject json = new JSONObject();
        
        Field[] field = obj.getClass().getDeclaredFields();
        
        for (Field f : field) {
            f.setAccessible(true);
            try {
                json.put(f.getName(), toJSONObject(f.get(obj)));
            } catch (IllegalAccessException e) {
                logger.error("error", e);
            }
        }
        return json;
    }

}