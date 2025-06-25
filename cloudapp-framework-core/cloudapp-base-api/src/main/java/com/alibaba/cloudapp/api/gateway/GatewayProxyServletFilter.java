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

import com.alibaba.cloudapp.exeption.CloudAppException;

import javax.servlet.*;

public abstract class GatewayProxyServletFilter implements Filter, Authorized {

    /*
     * Default content type
     */
    String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    /*
     * Default content encoding
     */
    String DEFAULT_CONTENT_ENCODING = "UTF-8";


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws ServletException {
        try {
            proxy(request, response);
        } catch (Throwable t) {
            throw new ServletException(t);
        }
    }

    /**
     * Proxy the original servlet request to the gateway server directly.
     *
     * @param request the original servlet request
     * @param response the response to the original servlet request
     *
     * @throws CloudAppException exception
     */
    public abstract void proxy(ServletRequest request, ServletResponse response)
            throws CloudAppException;
}
