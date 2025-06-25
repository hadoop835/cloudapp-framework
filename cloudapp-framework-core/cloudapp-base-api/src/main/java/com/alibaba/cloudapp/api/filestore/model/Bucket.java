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
package com.alibaba.cloudapp.api.filestore.model;

import com.alibaba.cloudapp.model.BaseModel;

import java.io.Serializable;

/**
 * Buckets are the carriers of objects, and can be understood as "containers" for storing objects,
 * and there is no upper limit on the capacity of such "containers". Objects are stored in buckets
 * in a flat structure, without the concept of folders and directories. Users can choose to store
 * objects in a single bucket or multiple buckets.
 */
public class Bucket<Delegation> extends BaseModel implements Serializable {
    /**
     * The delegating bucket
     */
    private Delegation delegatingBucket;

    /**
     * The creation time of bucket
     */
    private String creationDate;

    /**
     * the name of bucket
     */
    private String name;

    /**
     * The region of bucket
     */
    private String region;

    /**
     * Create a Builder instance. This method provides a convenient way to
     * initialize a Builder object. The Builder pattern is used to construct
     * complex objects. Through this method, the client can build an object step
     * by step, and each step can be controlled in detail.
     *
     * @return Returns a new Builder instance used to build objects of a specific type.
     */
    public static <Delegation> Bucket.Builder<Delegation> builder() {
        return new Bucket.Builder<>();
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Delegation getDelegatingBucket() {
        return delegatingBucket;
    }

    public void setDelegatingBucket(Delegation delegatingBucket) {
        this.delegatingBucket = delegatingBucket;
    }

    public static class Builder<Delegation> extends
            BaseModel.Builder<Bucket.Builder<Delegation>, Bucket<Delegation>>  {
        public Bucket.Builder<Delegation> creationDate(String creationDate) {
            operations.add(args -> args.setCreationDate(creationDate));
            return this;
        }

        public Bucket.Builder<Delegation> name(String name) {
            operations.add(obj -> obj.setName(name));
            return this;
        }

        public Bucket.Builder<Delegation> region(String region) {
            operations.add(obj -> obj.setRegion(region));
            return this;
        }

        public Bucket.Builder<Delegation> delegatingBucket(
                Delegation delegatingBucket) {
            operations.add(args -> args.setDelegatingBucket(delegatingBucket));
            return this;
        }

        @Override
        protected void validate(Bucket<Delegation> bucket) {
            if (bucket.name == null || bucket.name.isEmpty()) {
                throw new IllegalArgumentException(
                        "Bucket name cannot be null or empty");
            }
        }
    }
}
