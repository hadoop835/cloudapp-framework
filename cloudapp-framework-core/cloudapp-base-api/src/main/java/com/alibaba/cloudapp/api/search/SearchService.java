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
package com.alibaba.cloudapp.api.search;

import com.alibaba.cloudapp.api.common.DelegatingClientAware;
import com.alibaba.cloudapp.exeption.CloudAppException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * CloudApp search service abstract interface
 */
public interface SearchService<Client, Request, Response>
        extends DelegatingClientAware<Client> {

    /**
     * Search the key world from the search engine
     *
     * @param indexName The index name
     * @param fieldName The field name
     * @param pattern The key word to be searched
     * @param maxResults The maximum number of results to be returned
     *
     * @return A collection of document as string.
     * @throws CloudAppException exception
     */
    Collection<String> search(String indexName,
                              String fieldName,
                              String pattern,
                              int maxResults) throws CloudAppException;

    /**
     * Search the key world from the search engine
     *
     * @param request The search request
     *
     * @return A collection of response.
     * @throws CloudAppException exception
     */
    Response search(Request request)
            throws CloudAppException;

    /**
     * Insert a document into the search engine
     *
     * @param indexName: Index name
     * @param doc: Document to be inserted
     * @return Document ID
     */
    String insertDocument(String indexName, Map<String, ?> doc)
            throws CloudAppException;

    /**
     * Bulk insert documents into the search engine
     *
     * @param indexName The index name
     * @param docs The list of documents to be inserted
     * @return The list of document IDs
     *
     * @throws CloudAppException exception
     */
    List<String> bulkInsertDocuments(String indexName, List<Map<String, ?>> docs)
            throws CloudAppException;

    /**
     * Delete a document from the search engine
     *
     * @param indexName: Index name
     * @param docId: Document ID
     * @return Whether the deletion is successful
     */
    boolean deleteDocument(String indexName, String docId)
            throws CloudAppException;

    /**
     * Update a document in the search engine
     *
     * @param indexName: Index name
     * @param docId: Document ID
     * @param doc: Document to be updated
     * @return Whether the update is successful
     */
    boolean updateDocument(String indexName, String docId, Map<String, ?> doc)
            throws CloudAppException;
}
