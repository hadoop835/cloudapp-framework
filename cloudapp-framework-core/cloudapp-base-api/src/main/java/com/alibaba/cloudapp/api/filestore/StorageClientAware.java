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
package com.alibaba.cloudapp.api.filestore;

import com.alibaba.cloudapp.api.common.DelegatingClientAware;

public interface StorageClientAware<Client>
        extends DelegatingClientAware<Client> {

    /**
     * Get the underlying delegating storage client, e.g: MinIO Client,
     * AlibabaCloud OSS Client, Amazon S3 Client, etc.
     *
     * @return the underlying delegating storage client
     */
    Client getDelegatingStorageClient();

    @Override
    default Client getDelegatingClient() {
        return getDelegatingStorageClient();
    }
}
