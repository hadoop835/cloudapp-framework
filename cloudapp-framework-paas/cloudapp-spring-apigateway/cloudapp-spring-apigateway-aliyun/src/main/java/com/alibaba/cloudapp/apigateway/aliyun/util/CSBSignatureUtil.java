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

package com.alibaba.cloudapp.apigateway.aliyun.util;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.cloudapp.util.JsonUtil;
import org.springframework.http.MediaType;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CSBSignatureUtil {

    private String accessKey;
    private String secretKey;

    public CSBSignatureUtil(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getQuerySign(String method, String path,
                               Map<String, String> query,
                               Map<String, Object> body,
                               long timestamp,
                               MediaType type)
            throws NoSuchAlgorithmException, InvalidKeyException {

        String formatMsg = formatQuery(method, path, query, body, timestamp, type);
        Mac mac = Mac.getInstance("HmacSHA256");

        return sign(mac, formatMsg);
    }

    private String sign(Mac mac, String formatMsg) throws InvalidKeyException {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        mac.init(key);
        mac.update(formatMsg.getBytes(StandardCharsets.UTF_8));

        return new String(Base64.getEncoder().encode(mac.doFinal()), StandardCharsets.UTF_8);
    }

    private String formatQuery(String method,
                               String path,
                               Map<String, String> query,
                               Map<String, Object> body,
                               long timestamp,
                               MediaType type) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");

        TreeMap<String, List<String>> queryMap = new TreeMap<>();
        if (query != null) {
            query.forEach((k, v) -> queryMap.put(k, Collections.singletonList(v)));
        }

        String bodyString = "";

        if (body != null && !MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(type)) {
            bodyString = JSONObject.toJSONString(
                    JsonUtil.formatJsonObj(new JSONObject(body)));

        } else if (body != null) {
            body.forEach((k, v) -> queryMap.put(k, Collections.singletonList(String.valueOf(v))));
        }


        String queryString = contactQureyString(queryMap);

        return timestamp + "\n" +
                accessKey + "\n" +
                method + "\n" +
                path + "\n" +
                queryString + "\n" +
                Base64.getEncoder().encodeToString(md5.digest(bodyString.getBytes(StandardCharsets.UTF_8)));
    }

    private static SortedMap<String, List<String>> sortQueryString(String queryString) {
        SortedMap<String, List<String>> result = new TreeMap<>();

        String[] queryStrings = queryString.split("&");
        for (String mapStr : queryStrings) {

            if (mapStr.length() < 2) {
                continue;
            }

            String[] map = mapStr.split("=");
            result.put(map[0], Collections.singletonList(map[1]));
        }

        return result;
    }

    private static String contactQureyString(SortedMap<String, List<String>> query) {
        StringBuilder sb = new StringBuilder();
        query.forEach((k, v) -> sb.append("&").append(k).append("=").append(v));
        return sb.length() == 0 ? "" : sb.substring(1);
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
