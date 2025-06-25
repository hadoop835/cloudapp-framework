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

/**
 * the metadata information for file storage
 */
public class ObjectMetadata<Delegation> extends BaseModel {

    /**
     * The object's underlying delegation
     */
    private Delegation delegationMetadata;

    /**
     * The object's etag
     */
    private String etag;

    /**
     * The object's size
     */
    private int size;

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Delegation getDelegationMetadata() {
        return delegationMetadata;
    }

    public void setDelegationMetadata(Delegation delegationMetadata) {
        this.delegationMetadata = delegationMetadata;
    }

    public static class Builder<Delegation> extends
            BaseModel.Builder<ObjectMetadata.Builder<Delegation>, ObjectMetadata<Delegation>>  {
        public ObjectMetadata.Builder<Delegation> etag(String etag) {
            operations.add(obj -> obj.setEtag(etag));
            return this;
        }

        public ObjectMetadata.Builder<Delegation> size(int size) {
            operations.add(obj -> obj.setSize(size));
            return this;
        }


        public ObjectMetadata.Builder<Delegation> delegatingMetadata(
                Delegation delegatingMetadata) {
            operations.add(obj -> obj.setDelegationMetadata(delegatingMetadata));
            return this;
        }

        @Override
        protected void validate(ObjectMetadata<Delegation> obj) {

        }
    }

    public static <Delegation> ObjectMetadata.Builder<Delegation> builder() {
        return new ObjectMetadata.Builder<>();
    }
}
