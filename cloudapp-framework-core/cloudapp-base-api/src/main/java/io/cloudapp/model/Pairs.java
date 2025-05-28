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
package io.cloudapp.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A simple class to hold a list of key-value pairs, example:
 * <pre>{@code
 *     Pairs<String, String> pairs = Pairs.toPairs("key1", "value1")
 *                                          .of("key2", "value2")
 *                                          .of("key3", "value3");
 *     List<Pair<String, String>> list = pairs.toList();
 *     for (Pair<String, String> pair : list) {
 *         System.out.println(pair.getKey() + ":" + pair.getValue());
 *     }
 * }</pre>
 * @author <a href="mailto:alickreborn@gmail.com">alickreborn</a>
 */
public class Pairs<K, V> {

    private final List<Pair<K, ? extends V>> pairs = new LinkedList<>();

    public static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(key, pair.key) && Objects.equals(
                    value, pair.value);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
        
    }

    public Pairs<K, V> of(K key, V value) {
        pairs.add(new Pair<>(key, value));
        return this;
    }

    public List<Pair<K, ? extends V>> toList() {
        return pairs;
    }

    /**
     * Convert to Pairs given key and value, this method is a shortcut to create
     * a Pairs object.
     *
     * @param key   The key to be added.
     * @param value The value to be added.
     * @return A new Pairs object, including the given key-value pair.
     */
    public static <K, V> Pairs<K, V> toPairs(K key, V value) {
        return new Pairs<K, V>().of(key, value);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pairs<?, ?> pairs1 = (Pairs<?, ?>) o;
        if (pairs.size() != pairs1.pairs.size()) {
            return false;
        }
        for (int i = 0; i < pairs.size(); i++) {
            if (!pairs.get(i).equals(pairs1.pairs.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(pairs);
    }
    
}
