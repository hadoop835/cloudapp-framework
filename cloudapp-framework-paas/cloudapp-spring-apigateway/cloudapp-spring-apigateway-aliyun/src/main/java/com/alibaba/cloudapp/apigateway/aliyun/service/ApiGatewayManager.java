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

package com.alibaba.cloudapp.apigateway.aliyun.service;

import com.alibaba.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.BasicProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.JwtProperties;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.cloudapp.apigateway.aliyun.ApiGatewayConstant;
import com.alibaba.cloudapp.apigateway.aliyun.AuthTypeEnum;
import com.alibaba.cloudapp.apigateway.aliyun.properties.*;
import com.alibaba.cloudapp.apigateway.aliyun.util.CSBSignatureUtil;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.exeption.CloudAppInvalidAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSB APIGateway 
 */
public class ApiGatewayManager {

    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayManager.class);

    private String accessKey;
    private String secretKey;
    private String gatewayUri;
    private final RestTemplate restTemplate;
    private final CSBSignatureUtil signatureUtil;

    public ApiGatewayManager(String accessKey, String secretKey, String gatewayUri) {
        this(accessKey, secretKey, gatewayUri, new RestTemplate());
    }

    public ApiGatewayManager(String accessKey,
                             String secretKey,
                             String gatewayUri,
                             RestTemplate restTemplate) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.gatewayUri = gatewayUri;
        this.signatureUtil = new CSBSignatureUtil(accessKey, secretKey);
        this.restTemplate = restTemplate;
    }

    public void createApiKeyConsumer(
            String name,
            String gwInstanceId,
            List<String> groups,
            ApiKeyProperties apiKey
    ) throws CloudAppException {
        JSONObject body = new JSONObject();

        body.put("appName", name);
        body.put("authType", AuthTypeEnum.APIKEY.getType());
        body.put("gwInstanceId", gwInstanceId);
        body.put("groups", groups == null ? Collections.emptyList() : groups);
        body.put("key", apiKey.getApiKey());

        createConsumer(body);
    }

    public void createJwtConsumer(
            String name, String gwInstanceId, List<String> groups, JwtProperties jwt
    ) throws CloudAppException {
        JSONObject body = new JSONObject();
        body.put("appName", name);
        body.put("authType", AuthTypeEnum.JWT.getType());
        body.put("gwInstanceId", gwInstanceId);
        body.put("groups", groups == null ? Collections.emptyList() : groups);

        body.put("key", jwt.getKeyId());
        body.put("appSecret", jwt.getSecret());
        body.put("expireTime", jwt.getExpiredSecond() * 1000);
        Map<String, String> payload = new HashMap<>(4);

        payload.put("issuer", jwt.getIssuer());
        payload.put("subject", jwt.getSubject());

        body.put("payload", payload);
        createConsumer(body);
    }

    public void createBasicConsumer(
            String name, String gwInstanceId, List<String> groups, BasicProperties basic
    ) throws CloudAppException {
        JSONObject body = new JSONObject();
        body.put("appName", name);
        body.put("authType", AuthTypeEnum.BASIC.getType());
        body.put("gwInstanceId", gwInstanceId);
        body.put("groups", groups == null ? Collections.emptyList() : groups);

        body.put("key", basic.getUsername());
        body.put("password", basic.getPassword());

        createConsumer(body);
    }

    private void createConsumer(JSONObject body) throws CloudAppException {
        try {
            RequestEntity<String> request = initRequestEntity(HttpMethod.POST,
                    ApiGatewayConstant.CREATE_CONSUMER_URL,
                    null,
                    body,
                    MediaType.APPLICATION_JSON);
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            if(response.getStatusCode().equals(HttpStatus.OK)) {
                if(logger.isDebugEnabled()) {
                    logger.debug("create consumer response: {}", response.getBody());
                }
            } else {
                throw new CloudAppException("CloudApp.CreateConsumerFailed");
            }
        } catch (Exception e) {
            throw new CloudAppException("create consumer failed", e);
        }
    }

    /**
     * Check consumer exists
     * @param name api gateway name
     * @param gwInstanceId api gateway instance id
     * @return true if app exists, false otherwise
     * @throws CloudAppException if check app exists failed
     */
    public boolean checkConsumerExists(String name, String gwInstanceId) throws CloudAppException {
        try {
            Map<String, Object> params = new HashMap<>(8);
            params.put("appName", name);
            params.put("gwInstanceId", gwInstanceId);
            params.put("activeSearchName", "appName");
            params.put("current", 1);
            params.put("size", 20);

            RequestEntity<String> request = initRequestEntity(HttpMethod.POST,
                    ApiGatewayConstant.LIST_CONSUMER_URL,
                    null,
                    params,
                    MediaType.APPLICATION_JSON);
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            if(response.getStatusCode().equals(HttpStatus.OK)) {

                if(logger.isDebugEnabled()) {
                    logger.debug("list consumer response: {}", response.getBody());
                }

                JSONObject result = JSON.parseObject(response.getBody());
                JSONArray dataList = result == null || result.get("data") == null ?
                        new JSONArray() : result.getJSONObject("data").getJSONArray("records");
                if(dataList != null && !dataList.isEmpty()) {

                    return dataList.stream().anyMatch(e -> {
                        JSONObject app = (JSONObject) JSON.toJSON(e);
                        return app.getString("appName").equals(name);
                    });
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("list consumer response status code: {}", response.getStatusCode());
                }
            }
            return false;
        } catch (Exception e) {
            throw new CloudAppException("check app exists failed",
                    "CloudApp.AppAlreadyExisted", e);
        }
    }

    /**
     * Init request, Add signature to request header
     * @param method request method
     * @param path   uri path
     * @param query  query parameters
     * @param body   request body
     * @return   new request entity
     * @throws NoSuchAlgorithmException algorithm invalid
     * @throws InvalidKeyException secret invalid
     */
    public RequestEntity<String> initRequestEntity(HttpMethod method,
                                            String path,
                                            Map<String, String> query,
                                            Map<String, Object> body,
                                                   MediaType type
    ) throws NoSuchAlgorithmException, InvalidKeyException, CloudAppInvalidAccessException {

        if(!StringUtils.hasText(accessKey) || !StringUtils.hasText(secretKey)) {
            throw new CloudAppInvalidAccessException("Invalid access key or secret key!");
        }

        long time = System.currentTimeMillis();
        String sign = signatureUtil.getQuerySign(method.name(), path, query, body, time, type);

        HttpHeaders headers = new HttpHeaders();
        headers.add(ApiGatewayConstant.HEADER_ACCESS_KEY, accessKey);
        headers.add(ApiGatewayConstant.HEADER_CSB_OPENID, ApiGatewayConstant.CSB_OPENAPI);
        headers.add(ApiGatewayConstant.HEADER_REQUEST_TIME, String.valueOf(time));
        headers.add(ApiGatewayConstant.HEADER_SIGN, sign);
        headers.setContentType(type);

        StringBuilder urlBuilder = new StringBuilder(path);

        if (query != null && !query.isEmpty()) {
            urlBuilder.append("?");
            query.forEach((k, v) -> urlBuilder.append(k).append("=").append(v).append("&"));
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        } else {
            logger.info("query is empty");
        }

        URI uri = URI.create(gatewayUri + urlBuilder);

        return new RequestEntity<>(JSON.toJSONString(body), headers, method, uri);
    }
    
    public String getAccessKey() {
        return accessKey;
    }
    
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    
    public String getGatewayUri() {
        return gatewayUri;
    }
    
    public void setGatewayUri(String gatewayUri) {
        this.gatewayUri = gatewayUri;
    }
    
}
