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
package io.cloudapp.api.gateway.authentication;

import io.cloudapp.exeption.CloudAppException;
import org.springframework.http.HttpRequest;

/**
 * Authorizer interface, used to add the authorization header to a request. the
 * implementation of this interface should be thread safe. http authorization
 * methods like JWT, OAuth, etc. should implement this interface.For Api Gateway 2.0 ,
 * please refer to this document:
 * https://aliyuque.antfin.com/csb2/gbkg90/mpxgwg1rgahgoag9_kiggg8?singleDoc#
 */
public interface Authorizer {

    /**
     * Adds the authorization header to the request.
     *
     * @param request the request to add the authorization header to
     * @throws CloudAppException if an error occurs while adding the
     * authorization header
     */
    void applyAuthorization(HttpRequest request) throws CloudAppException;

}
