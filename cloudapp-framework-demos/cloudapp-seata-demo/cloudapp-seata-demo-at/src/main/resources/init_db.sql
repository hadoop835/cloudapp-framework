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

DROP TABLE IF EXISTS tb_account;
create table tb_account (
    id           int  auto_increment  primary key,
    money        decimal(19, 2) null,
    user_id      varchar(255)   null,
    username     varchar(255)   null
);
INSERT INTO tb_account (id, money, user_id, username) VALUES (1, 116.00, '1', 'zhangsan');
INSERT INTO tb_account (id, money, user_id, username) VALUES (2, 54.00, '2', 'lisi');

DROP TABLE IF EXISTS tb_order;
create table tb_order (
    id           int auto_increment  primary key,
    amount       decimal(19, 2) null,
    count        int            null,
    product_code varchar(255)   null,
    user_id      varchar(255)   null
);

DROP TABLE IF EXISTS tb_stock;
create table tb_stock (
    id           int auto_increment  primary key,
    price        decimal(19, 2) null,
    product_code varchar(255)   null,
    product_name varchar(255)   null,
    quantity     int            null
);
INSERT INTO tb_stock (id, price, product_code, product_name, quantity) VALUES (1, 2.50, '1', '黑色签字笔', 8);
INSERT INTO tb_stock (id, price, product_code, product_name, quantity) VALUES (2, 1.00, '2', '草稿本', 20);

DROP TABLE IF EXISTS undo_log;
create table undo_log (
    id            bigint auto_increment primary key,
    branch_id     bigint       null,
    context       varchar(255) null,
    log_created   datetime(6)  null,
    log_modified  datetime(6)  null,
    log_status    int          null,
    rollback_info longblob     null,
    xid           varchar(255) null
);

