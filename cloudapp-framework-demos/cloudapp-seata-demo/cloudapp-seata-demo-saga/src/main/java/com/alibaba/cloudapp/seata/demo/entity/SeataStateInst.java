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

package com.alibaba.cloudapp.seata.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table
public class SeataStateInst {
    
    @Id
    private String id;
    @Column(name = "machine_inst_id")
    private String machineInstId;
    private String name;
    private String type;
    @Column(name = "service_name")
    private String serviceName;
    @Column(name = "service_method")
    private String serviceMethod;
    @Column(name = "service_type")
    private String serviceType;
    @Column(name = "business_key")
    private String businessKey;
    @Column(name = "state_id_compensated_for")
    private String stateIdCompensatedFor;
    @Column(name = "state_id_retried_for")
    private String stateIdRetriedFor;
    @Column(name = "is_for_update")
    private Boolean isForUpdate;
    @Lob
    @Column(name = "input_params")
    private String inputParams;
    @Lob
    @Column(name = "output_params")
    private String outputParams;
    @Column(name = "status")
    private String status;
    @Lob
    @Column(name = "excep")
    private byte[] excep;
    @Column(name = "gmt_started")
    private LocalDateTime gmtStarted;
    @Column(name = "gmt_updated")
    private LocalDateTime gmtUpdated;
    @Column(name = "gmt_end")
    private LocalDateTime gmtEnd;

}