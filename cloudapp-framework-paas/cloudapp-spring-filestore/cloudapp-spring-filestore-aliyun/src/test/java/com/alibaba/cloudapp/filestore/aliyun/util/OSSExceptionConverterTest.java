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

package com.alibaba.cloudapp.filestore.aliyun.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.jupiter.api.Test;

public class OSSExceptionConverterTest {
    
    @Test
    public void testConvert1() {
        // Setup
        final ClientException e = new ClientException(
                "errorMessage", "errorCode", "requestId",
                new Exception("message")
        );
        
        // Run the test
        final CloudAppException result = OSSExceptionConverter.convert(e);
        
        // Verify the results
    }
    
    @Test
    public void testConvert2() {
        // Setup
        final CloudAppException e = new CloudAppException(
                "msg", "code", new Exception("message")
        );
        
        // Run the test
        final CloudAppException result = OSSExceptionConverter.convert(e);
        
        // Verify the results
    }
    
    @Test
    public void testConvert3() {
        // Setup
        final OSSException ex = new OSSException();
        
        // Run the test
        final CloudAppException result = OSSExceptionConverter.convert(ex);
        
        // Verify the results
    }
    
    @Test
    public void testConvert4() {
        // Setup
        // Run the test
        final CloudAppException result = OSSExceptionConverter.convert(
                new Exception("message"));
        
        // Verify the results
    }
    
}
