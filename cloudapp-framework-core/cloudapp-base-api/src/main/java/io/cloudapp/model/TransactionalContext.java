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

package io.cloudapp.model;

import java.lang.annotation.Annotation;
import java.util.Map;

public class TransactionalContext {
    
    /**
     * current transaction id
     */
    private String xid;
    /**
     * current branch id
     */
    private String branchId;
    /**
     * Transaction name or StateMachine name
     */
    private String transactionName;
    /**
     * tenant id
     */
    private String tenantId;
    /**
     * business key
     */
    private String businessKey;
    /**
     * start params
     */
    private Map<String, Object> params;
    /**
     * method annotation class
     */
    private Annotation[] annotations;
    /**
     * method return value
     */
    private Object returnValue;
    /**
     * whether the transaction is success
     */
    private boolean success = false;
    /**
     * transaction start milliseconds
     */
    private long startTime;
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getXid() {
        return xid;
    }
    
    public void setXid(String xid) {
        this.xid = xid;
    }
    
    public String getBranchId() {
        return branchId;
    }
    
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    
    public String getTransactionName() {
        return transactionName;
    }
    
    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getBusinessKey() {
        return businessKey;
    }
    
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    
    public Map<String, Object> getParams() {
        return params;
    }
    
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    
    public Annotation[] getAnnotations() {
        return annotations;
    }
    
    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }
    
    public Object getReturnValue() {
        return returnValue;
    }
    
    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
