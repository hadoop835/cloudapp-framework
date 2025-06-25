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

package com.alibaba.cloudapp.exeption;

/**
 * Access exception including follow scenarios:
 * <p>
 * 1. client connecting error: socket connecting, tls error.
 * 2. credential error: AK/SK not correct, signature not match, etc.
 * 3. server error: 404, 503 codes
 */
public class CloudAppInvalidArgumentsException extends CloudAppException {
    
    private static final String CODE = "CloudApp.InvalidArgument";
    
    
    public CloudAppInvalidArgumentsException(String msg) {
        super(msg, CODE);
    }
    
    
    public CloudAppInvalidArgumentsException(String msg,
                                             String code,
                                             Throwable cause) {
        super(msg, code, cause);
    }
    
}
