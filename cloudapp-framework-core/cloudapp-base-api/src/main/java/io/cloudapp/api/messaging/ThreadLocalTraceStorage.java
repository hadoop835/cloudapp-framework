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

package io.cloudapp.api.messaging;

public class ThreadLocalTraceStorage implements TraceStorage {

    private static final ThreadLocal<String> TRACE_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<String> SPAN_LOCAL = new ThreadLocal<>();

    public void setTraceId(String traceId) {
        TRACE_LOCAL.set(traceId);
    }

    public String getTraceId() {
        return TRACE_LOCAL.get();
    }

    public void setSpanId(String spanId) {
        SPAN_LOCAL.set(spanId);
    }

    public String getSpanId() {
        return SPAN_LOCAL.get();
    }

    public void clearTrace() {
        TRACE_LOCAL.remove();
    }

    public void clearSpan() {
        SPAN_LOCAL.remove();
    }
}
