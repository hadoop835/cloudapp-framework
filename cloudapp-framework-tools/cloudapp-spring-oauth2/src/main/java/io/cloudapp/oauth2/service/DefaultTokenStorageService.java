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

package io.cloudapp.oauth2.service;

import io.cloudapp.api.gateway.model.OAuthToken;
import io.cloudapp.api.oauth2.TokenStorageService;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultTokenStorageService implements TokenStorageService {
    
    private static final Map<String, OAuthToken> TOKEN_MAP = Collections
            .synchronizedMap(new HashMap<>());
    @Override
    public void saveToken(String key, OAuthToken token) {
        if(StringUtils.hasText(key)) {
            TOKEN_MAP.put(key, token);
        }
    }
    
    @Override
    public OAuthToken getToken(String key) {
        return key == null ? null : TOKEN_MAP.get(key);
    }
    
}
