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
package com.alibaba.cloudapp.api.common;

import com.alibaba.cloudapp.model.BaseModel;

import java.util.List;

/**
 * @param <T> Data types for paginated lists
 */
public class Pagination<T> extends BaseModel {

    /**
     * List data for paginated lists
     */
    private List<T> dataList;
    /**
     * Specifies how many items the list contains
     */
    private Integer maxResults;
    /**
     * Indicates whether there is a next page
     */
    private Boolean hasNext;
    /**
     * The token used for get the next page
     */
    private String nextToken;

    /**
     * Builder class for Pagination
     *
     * @param <T> Data types for paginated lists
     */
    public static class Builder<T> extends
            BaseModel.Builder<Builder<T>, Pagination<T>>  {

        public Builder<T> dataList(List<T> dataList) {
            this.operations.add(obj -> obj.dataList = dataList);
            return this;
        }

        public Builder<T> maxResults(Integer maxResults) {
            this.operations.add(obj -> obj.maxResults = maxResults);
            return this;
        }

        public Builder<T> hasNext(Boolean hasNext) {
            this.operations.add(obj -> obj.hasNext = hasNext);
            return this;
        }

        public Builder<T> nextToken(String nextToken) {
            this.operations.add(obj -> obj.nextToken = nextToken);
            return this;
        }

        @Override
        protected void validate(Pagination<T> args) {

        }
    }

    /**
     * Create a Builder instance. This method provides a convenient way to
     * initialize a Builder object. The Builder pattern is used to construct
     * complex objects. Through this method, the client can build an object step
     * by step, and each step can be controlled in detail.
     *
     * @return Returns a new Builder instance used to build objects of a specific type.
     */
    public Builder<T> builder() {
        return new Builder<>();
    }


    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
