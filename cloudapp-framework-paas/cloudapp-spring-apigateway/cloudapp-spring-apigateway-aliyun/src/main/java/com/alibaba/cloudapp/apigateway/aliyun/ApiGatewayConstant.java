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

package com.alibaba.cloudapp.apigateway.aliyun;

public class ApiGatewayConstant {

    public static final String CSB_OPENAPI = "CSB_OPENAPI";
    public static final String JWT_ALGORITHM_HS256 = "HS256";

    public static final String HEADER_ACCESS_KEY = "X-HMAC-ACCESS-KEY";
    public static final String HEADER_REQUEST_TIME = "X-HMAC-REQUEST-TIME";
    public static final String HEADER_SIGN = "X-HMAC-SIGN";
    public static final String HEADER_CSB_OPENID = "X-CSB-OPENAPI";

    public static final String CREATE_CONSUMER_URL = "/application/createApp";
    public static final String LIST_CONSUMER_URL = "/application/listApps";

    public static final String AUTHORIZATION_URI = "/oauth2/authorize";
    public static final String TOKEN_URI = "/oauth2/token";

}
