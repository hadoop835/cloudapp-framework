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
package com.alibaba.cloudapp.api.gateway;

import com.alibaba.fastjson2.JSON;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.springframework.http.*;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.concurrent.Future;

public abstract class GatewayService implements Authorized {
    /**
     * Get the delegating rest template.
     *
     * @return The delegating rest template.
     */
    public abstract RestTemplate getRestTemplate();

    /**
     * Get the delegating async rest template.
     *
     * @return The delegating async rest template.
     */
    public abstract AsyncRestTemplate getAsyncRestTemplate();

    /**
     * Simple request. Get an object from the requesting url.
     *
     * @param url the requesting url, e.g: /api/user/123.
     * @param type the response type, e.g: UserInfo.class.
     * @return a Serializable object.
     *
     * @throws CloudAppException exception
     */
    public <T extends Serializable> T  get(String url, Class<T> type)
            throws CloudAppException {
        return getRestTemplate().getForObject(url, type);
    }


    /**
     * Simple request. Get an object from the requesting url asynchronously.
     *
     * @param url the requesting url, e.g: /api/user/123.
     * @param type the response type, e.g: UserInfo.class.
     * @return a Future object.
     * @throws CloudAppException exception
     */
    public <T extends Serializable> Future<T> asyncGet(String url, Class<T> type) {
        return getAsyncRestTemplate().execute(url,
                HttpMethod.GET,
                null,
                response -> getRestTemplate().getForObject(url, type)
        );
    }

    /**
     * Simple request. Post an object to the requesting url with the content type:
     * application/x-www-form-urlencoded.
     *
     * @param url the requesting url, e.g: /api/user/123.
     * @param body the body content.
     * @param type the response type, e.g: UserInfo.class.
     * @return a Serializable object.
     * @throws CloudAppException exception
     */
    public <T extends Serializable> T  post(String url, String body, Class<T> type)
            throws CloudAppException {
        return getRestTemplate().postForObject(url, body, type);
    }

    /**
     * Post an object to the requesting url asynchronously with content type:
     * application/x-www-form-urlencoded.
     * @param url the requesting url, e.g: /api/user/123.
     * @param body the body content.
     * @param type the response type, e.g: UserInfo.class.
     * @return a Future object.
     */
    public <T extends Serializable> Future<T>  asyncPost(String url,
                                                  String body,
                                                  Class<T> type) {
        RequestEntity request = RequestEntity
                .post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body);

        return getAsyncRestTemplate()
                .postForEntity(url, request, type)
                .completable()
                .thenApplyAsync(HttpEntity::getBody);
    }

    /**
     * Simple request. Post an object to the requesting url with the content type:
     * application/json.
     *
     * @param url        The URL for the HTTP POST request.
     * @param jsonBody   The object to be posted, which will be converted to JSON format.
     * @param type       The type of the returned object, used to deserialize the response data.
     * @return           Returns an object of type T, which is the deserialized response from the server.
     * @throws CloudAppException If the request fails or the deserialization of the response fails, a CloudAppException is thrown.
     */
    public <T extends Serializable> T  postJson(String url, Object jsonBody, Class<T> type)
            throws CloudAppException {
        return getRestTemplate().postForObject(url, JSON.toJSON(jsonBody), type);
    }

    /**
     * Post an object to the requesting url asynchronously with content type:
     * application/json.
     *
     * @param url       The URL to which the request is sent.
     * @param jsonBody  The object to be posted, which will be serialized into JSON format.
     * @param type      The type of the returned object, used to deserialize the response data.
     * @return          Returns a Future object, which is used to get the result of the asynchronous post request.
     */
    <T extends Serializable> Future<T>  asyncPostJson(String url,
                                                      Object jsonBody,
                                                      Class<T> type) {
        RequestEntity<?> request = RequestEntity
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(JSON.toJSON(jsonBody));

        return getAsyncRestTemplate()
                .postForEntity(url, request, type)
                .completable()
                .thenApplyAsync(HttpEntity::getBody);
    }

    /**
     * Simple request. send a complicated request to the gateway server.
     *
     * @param request The request entity, containing the request details.
     * @param clazz   The type of the response body.
     * @return A ResponseEntity object containing the response details.
     *
     * @throws CloudAppException Thrown when an exception occurs during the request.
     */
    public <T> ResponseEntity<T> request(RequestEntity<T> request, Class<T> clazz)
            throws CloudAppException {
        return getRestTemplate().exchange(request, clazz);
    }

    /**
     * Async request. send a complicated request to the gateway server.
     *
     * @param request The request entity, containing the request URL and request body, etc.
     * @param clazz The type of the response body, used to convert the response data into the corresponding Java object type.
     * @return Returns a Future object, which can be used to obtain the response body asynchronously.
     */
    public <T> Future<T>  asyncRequest(RequestEntity<?> request,
                                       Class<T> clazz) {
        return getAsyncRestTemplate()
                .postForEntity(request.getUrl(), request, clazz)
                .completable()
                .thenApplyAsync(HttpEntity::getBody);
    }
}
