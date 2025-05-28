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

package io.cloudapp.apigateway.aliyun;

public enum AuthTypeEnum {

    BASIC(1, "BASIC"),
    OAUTH2(2, "OAuth2.0"),
    JWT(3, "JWT"),
    APIKEY(5, "API_KEY"),
    CUSTOM(10, "CUSTOM"),
    NO_AUTH(11, "NO_AUTH");

    private final int type;
    private final String name;

    AuthTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static AuthTypeEnum getAuthType(int type) {
        for (AuthTypeEnum authTypeEnum : AuthTypeEnum.values()) {
            if (authTypeEnum.getType() == type) {
                return authTypeEnum;
            }
        }

        return null;
    }

    public static AuthTypeEnum getAuthType(String name) {
        for (AuthTypeEnum authTypeEnum : AuthTypeEnum.values()) {
            if (authTypeEnum.getName().equals(name)) {
                return authTypeEnum;
            }
        }

        return null;
    }
}
