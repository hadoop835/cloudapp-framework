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

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonUtilTest {
    
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private Object[] objArray;
    private Collection<Object> objCollection;
    private Map<String, Object> objMap;
    private Object obj;
    
    @Before
    public void setUp() {
        jsonArray = new JSONArray();
        jsonObject = new JSONObject();
        objArray = new Object[]{};
        objCollection = new ArrayList<>();
        objMap = new TreeMap<>();
        obj = new Object();
    }
    
    @Test
    public void testJsonStringToTreeMap() {
        // Setup
        // Run the test
        final TreeMap<String, Object> result = JsonUtil.jsonStringToTreeMap(
                "{\"a\":\"b\"}");
        
        // Verify the results
    }
    
    @Test
    public void testFormatJsonObj() {
        // Setup
        final JSONObject jsonObject = new JSONObject();
        
        // Run the test
        final TreeMap<String, Object> result = JsonUtil.formatJsonObj(
                jsonObject);
        
        // Verify the results
    }
    
    @Test
    public void testFormatJsonArray() {
        // Setup
        final JSONArray jsonArray = new JSONArray(0);
        final JSONArray expectedResult = new JSONArray(0);
        
        // Run the test
        final JSONArray result = JsonUtil.formatJsonArray(jsonArray);
        
        // Verify the results
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void formatJsonArray_NullArray_ReturnsNull() {
        JSONArray obj = null;
        JSONArray result = JsonUtil.formatJsonArray(obj);
        assertNull(result);
    }
    
    @Test
    public void formatJsonArray_EmptyArray_ReturnsEmptyArray() {
        JSONArray result = JsonUtil.formatJsonArray(jsonArray);
        assertEquals(jsonArray, result);
    }
    
    @Test
    public void formatJsonArray_ArrayWithPrimitives_ReturnsSameArray() {
        jsonArray.add("value");
        jsonArray.add(123);
        jsonArray.add(true);
        
        JSONArray result = JsonUtil.formatJsonArray(jsonArray);
        assertEquals(jsonArray, result);
    }
    
    @Test
    public void formatJsonArray_ArrayWithNestedStructures_FormatsCorrectly() {
        jsonArray.add(jsonObject);
        jsonArray.add(new JSONArray());
        
        JSONArray result = JsonUtil.formatJsonArray(jsonArray);
        assertEquals(jsonArray, result);
    }
    
    @Test
    public void toJSONObject_Array_NullArray_ReturnsNull() {
        Object[] objs = null;
        Object[] result = JsonUtil.toJSONObject(objs);
        assertNull(result);
    }
    
    @Test
    public void toJSONObject_Array_EmptyArray_ReturnsEmptyArray() {
        Object[] result = JsonUtil.toJSONObject(objArray);
        assertEquals(objArray, result);
    }
    
    @Test
    public void toJSONObject_Array_WithPrimitives_ReturnsSameArray() {
        objArray = new Object[]{"value", 123, true};
        
        Object[] result = JsonUtil.toJSONObject(objArray);
        assertEquals(objArray, result);
    }
    
    @Test
    public void toJSONObject_Array_WithComplexObjects_FormatsCorrectly() {
        objArray = new Object[]{obj, objArray};
        
        Object[] result = JsonUtil.toJSONObject(objArray);
        
        assertEquals(new JSONObject(), result[0]);
        assert Arrays.equals(new JSONObject[]{}, (Object[])result[1]);
    }
    
    @Test
    public void toJSONObject_Collection_NullCollection_ReturnsNull() {
        List<String> list = null;
        List<?> result = JsonUtil.toJSONObject(list);
        assertNull(result);
    }
    
    @Test
    public void toJSONObject_Collection_EmptyCollection_ReturnsEmptyList() {
        List<?> result = JsonUtil.toJSONObject(objCollection);
        assertEquals(new ArrayList<>(), result);
    }
    
    @Test
    public void toJSONObject_Collection_WithPrimitives_ReturnsSameList() {
        objCollection.add("value");
        objCollection.add(123);
        objCollection.add(true);
        
        List<?> result = JsonUtil.toJSONObject(objCollection);
        assertEquals(objCollection, result);
    }
    
    @Test
    public void toJSONObject_Collection_WithComplexObjects_FormatsCorrectly() {
        objCollection.add(obj);
        objCollection.add(objArray);
        
        List<?> result = JsonUtil.toJSONObject(objCollection);
        
        assertEquals(new JSONObject(), result.get(0));
        assert Arrays.equals(new JSONObject[]{}, (Object[])result.get(1));
    }
    
    @Test
    public void toJSONObject_Map_NullMap_ReturnsNull() {
        Map<String, String> map  = null;
        JSONObject result = JsonUtil.toJSONObject(map);
        assertNull(result);
    }
    
    @Test
    public void toJSONObject_Map_EmptyMap_ReturnsEmptyJSONObject() {
        JSONObject result = JsonUtil.toJSONObject(objMap);
        assertEquals(new JSONObject(), result);
    }
    
    @Test
    public void toJSONObject_Map_WithPrimitives_ReturnsSameJSONObject() {
        objMap.put("key", "value");
        objMap.put("number", 123);
        objMap.put("bool", true);
        
        JSONObject result = JsonUtil.toJSONObject(objMap);
        assertEquals(objMap, result);
    }
    
    @Test
    public void toJSONObject_Map_WithComplexObjects_FormatsCorrectly() {
        objMap.put("jsonObject", obj);
        objMap.put("jsonArray", new String[0]);
        
        JSONObject result = JsonUtil.toJSONObject(objMap);
        assertEquals(
                "{\"jsonArray\":[],\"jsonObject\":{}}",
                result.toJSONString()
        );
    }
    
    @Test
    public void toJSONObject_Object_NullObject_ReturnsNull() {
        Object obj = null;
        Object result = JsonUtil.toJSONObject(obj);
        assertNull(result);
    }
    
    @Test
    public void toJSONObject_Object_PrimitiveObject_ReturnsSameObject() {
        Object result = JsonUtil.toJSONObject("value");
        assertEquals("value", result);
    }
    
    @Test
    public void toJSONObject_Object_ComplexObject_FormatsCorrectly() {
        Object result = JsonUtil.toJSONObject(jsonObject);
        assertEquals(jsonObject, result);
    }
}
