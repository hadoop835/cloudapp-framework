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

package com.alibaba.cloudapp.oauth2.handler;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.cloudapp.api.gateway.model.OAuthToken;
import com.alibaba.cloudapp.api.oauth2.handler.LoginHandler;
import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultLoginHandler implements LoginHandler {
    
    private final String successUrl;
    
    public DefaultLoginHandler(String successUrl) {
        this.successUrl = successUrl;
    }
    
    @Override
    public void loginSuccess(HttpServletRequest request,
                             HttpServletResponse response,
                             OAuthToken token)
            throws IOException, ServletException {
        request.getRequestDispatcher(successUrl).forward(request, response);
    }
    
    @Override
    public void loginFailure(HttpServletRequest request,
                             HttpServletResponse response,
                             Exception e) throws IOException {
        
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        JSONObject json = new JSONObject();
        json.put("error", e.getMessage());
        response.getOutputStream().write(json.toJSONBBytes());
    }
    
}
