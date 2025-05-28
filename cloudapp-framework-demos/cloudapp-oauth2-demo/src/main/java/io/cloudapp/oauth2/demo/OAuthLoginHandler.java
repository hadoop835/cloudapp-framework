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
package io.cloudapp.oauth2.demo;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.cloudapp.api.gateway.model.OAuthToken;
import io.cloudapp.api.oauth2.handler.LoginHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuthLoginHandler implements LoginHandler {
    @Override
    public void loginSuccess(HttpServletRequest request,
                             HttpServletResponse response,
                             OAuthToken token)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSON.toJSONString(token));
    }
    
    @Override
    public void loginFailure(HttpServletRequest request,
                             HttpServletResponse response,
                             Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        
        JSONObject object = new JSONObject();
        object.put("error", e.getMessage());
        object.put("code", 500);
        
        response.getWriter().write(object.toJSONString());
    }
    
}
