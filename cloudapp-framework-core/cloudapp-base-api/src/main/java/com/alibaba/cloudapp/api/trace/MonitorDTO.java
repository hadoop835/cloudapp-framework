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

package com.alibaba.cloudapp.api.trace;

import java.io.Serializable;
import java.util.Date;


public class MonitorDTO implements Serializable {
    private static final long serialVersionUID = -6380762311874249117L;
    private String tenantCode ;
    private Integer provinceCode ;
    //@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date sttTime ;
    private String bizCode ;
    private String appSign ;
    private String epCode ;
    private String serviceName ;
    private Integer countSuccess;
    private Integer countFail ;


    public MonitorDTO(String tenantCode, Integer provinceCode, Date sttTime, String bizCode, String appSign, String epCode, String serviceName, Integer countSuccess, Integer countFail) {
        this.tenantCode = tenantCode;
        this.provinceCode = provinceCode;
        this.sttTime = sttTime;
        this.bizCode = bizCode;
        this.appSign = appSign;
        this.epCode = epCode;
        this.serviceName = serviceName;
        this.countSuccess = countSuccess;
        this.countFail = countFail;
    }

    public MonitorDTO() {

    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Date getSttTime() {
        return sttTime;
    }

    public void setSttTime(Date sttTime) {
        this.sttTime = sttTime;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getAppSign() {
        return appSign;
    }

    public void setAppSign(String appSign) {
        this.appSign = appSign;
    }

    public String getEpCode() {
        return epCode;
    }

    public void setEpCode(String epCode) {
        this.epCode = epCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getCountSuccess() {
        return countSuccess;
    }

    public void setCountSuccess(Integer countSuccess) {
        this.countSuccess = countSuccess;
    }

    public Integer getCountFail() {
        return countFail;
    }

    public void setCountFail(Integer countFail) {
        this.countFail = countFail;
    }

    @Override
    public String toString() {
        return "MonitorDTO{" +
                "tenantCode=" + tenantCode +
                ", provinceCode=" + provinceCode +
                ", sttTime=" + sttTime +
                ", bizCode='" + bizCode + '\'' +
                ", appSign='" + appSign + '\'' +
                ", epCode='" + epCode + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", countSuccess=" + countSuccess +
                ", countFail=" + countFail +
                '}';
    }
}
