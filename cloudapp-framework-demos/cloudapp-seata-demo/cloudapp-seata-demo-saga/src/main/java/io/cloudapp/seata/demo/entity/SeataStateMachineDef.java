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
public class SeataStateMachineDef {
    
    @Id
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "app_name")
    private String appName;
    @Column(name = "type")
    private String type;
    @Column(name = "comment")
    private String comment;
    @Column(name = "comment_")
    private String commentBody;
    @Column(name = "ver")
    private String ver;
    @Column(name = "status")
    private String status;
    @Column(name = "recover_strategy")
    private String recoverStrategy;
    @Column(name = "content")
    @Lob
    private String content;
    @Column(name = "gmt_create")
    private LocalDateTime gmtCreate;
    @Column(name = "gmt_modified")
    private LocalDateTime gmtModified;

}