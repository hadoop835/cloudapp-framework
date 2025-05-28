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

package io.cloudapp.seata.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table
public class SeataStateMachineInst {
    
    @Id
    private String id;
    @Column(name = "machine_id")
    private String machineId;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "parent_id")
    private String parentId;
    @Column(name = "gmt_started")
    private LocalDateTime gmtStarted;
    @Column(name = "business_key")
    private String businessKey;
    @Lob
    @Column(name = "start_params")
    private String startParams;
    @Column(name = "gmt_end")
    private LocalDateTime gmtEnd;
    @Lob
    @Column(name = "excep")
    private byte[] excep;
    @Lob
    @Column(name = "end_params")
    private String endParams;
    @Column(name = "status")
    private String status;
    @Column(name = "compensation_status")
    private String compensationStatus;
    @Column(name = "is_running")
    private Boolean isRunning;
    @Column(name = "gmt_updated")
    private LocalDateTime gmtUpdated;
    
}