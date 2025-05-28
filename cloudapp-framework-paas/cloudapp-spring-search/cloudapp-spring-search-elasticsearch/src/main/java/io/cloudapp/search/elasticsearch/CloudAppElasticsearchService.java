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
package io.cloudapp.search.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.alibaba.fastjson2.JSON;
import io.cloudapp.api.search.SearchService;
import io.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

public class CloudAppElasticsearchService implements
        SearchService<ElasticsearchClient, SearchRequest, SearchResponse> {
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppElasticsearchService.class);
    
    private final ElasticsearchClient client;
    
    public CloudAppElasticsearchService(ElasticsearchClient client) {
        this.client = client;
    }
    
    @Override
    public Collection<String> search(String indexName, String fieldName, String pattern, int maxResults) throws CloudAppException {
        try {
            Assert.hasText(indexName, "indexName must not be empty or blank");
            Assert.hasText(fieldName, "fieldName must not be empty or blank");
            Assert.hasText(pattern, "pattern must not be empty or blank");
            
            SearchRequest req = new SearchRequest.Builder().index(indexName).query(q -> q.match(fq -> fq.field(fieldName).query(pattern))).size(maxResults).build();
            
            if (logger.isDebugEnabled()) {
                logger.debug("Search request: {}", req.toString());
            }
            
            SearchResponse<Object> resp = client.search(req, Object.class);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Search response: {}", resp.toString());
            }
            
            if (Objects.isNull(resp.hits()) || resp.hits().hits().isEmpty()) {
                return Collections.emptyList();
            }
            
            return resp.hits().hits().stream().map(i -> JSON.toJSONString(i.source())).collect(Collectors.toList());
            
        } catch (Exception e) {
            String msg = String.format(
                    "Search failed, indexName: %s, fieldName: %s, pattern: %s",
                    indexName, fieldName, pattern
            );
            
            throw new CloudAppException(msg, "CloudApp.ElasticSearchError", e);
        }
    }
    
    @Override
    public SearchResponse search(SearchRequest req) throws CloudAppException {
        try {
            Assert.notNull(req, "request must not be null");
            
            if (logger.isDebugEnabled()) {
                logger.debug("Search request: {}", req);
            }
            
            SearchResponse<Object> resp = client.search(req, Object.class);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Search response: {}", resp);
            }
            
            return resp;
        } catch (Exception e) {
            throw new CloudAppException(e.getMessage(),
                                        "CloudApp.ElasticSearchError",
                                        e
            );
        }
        
    }
    
    @Override
    public String insertDocument(String indexName, Map<String, ?> doc) throws CloudAppException {
        try {
            Assert.hasText(indexName, "indexName must not be empty or blank");
            Assert.notEmpty(doc, "doc must not be null");
            
            IndexRequest<Map<String, ?>> req = new IndexRequest.Builder<Map<String, ?>>()
                    .index(indexName)
                    .document(doc)
                    .build();
            
            if (logger.isDebugEnabled()) {
                logger.debug("Index request: {}", req.toString());
            }
            
            IndexResponse resp = client.index(req);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Index response: {}", JSON.toJSONString(resp));
            }
            
            return resp.id();
        } catch (Exception e) {
            String msg = String.format("Insert failed, indexName: %s",
                                       indexName
            );
            throw new CloudAppException(msg,
                                        "CloudApp.ElasticSearchDocumentError",
                                        e
            );
            
        }
    }
    
    @Override
    public List<String> bulkInsertDocuments(String indexName, List<Map<String, ?>> docs) throws CloudAppException {
        try {
            Assert.hasText(indexName, "indexName must not be empty or blank");
            Assert.notEmpty(docs, "docs must not be null");
            for (Map<String, ?> doc : docs) {
                Assert.notEmpty(doc, "doc must not be empty");
            }
            
            BulkRequest.Builder reqBuilder = new BulkRequest.Builder();
            docs.forEach(doc ->
                                 reqBuilder.operations(op -> op
                                         .index(idx -> idx
                                                 .index(indexName)
                                                 .document(doc)
                                         )
                                 )
            );
            BulkRequest req = reqBuilder.build();
            
            if (logger.isDebugEnabled()) {
                logger.debug("Bulk request: {}", req.toString());
            }
            
            BulkResponse resp = client.bulk(req);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Bulk response: {}", JSON.toJSONString(resp));
            }
            
            if (resp.errors()) {
                String msg = String.format("Bulk insert failed, indexName: %s",
                                           indexName);
                throw new CloudAppException(msg, "CloudApp.ElasticSearchDocumentError");
            }
            
            if (Objects.isNull(resp.items()) || resp.items().isEmpty()) {
                return Collections.emptyList();
            }
            
            return resp.items().stream().map(BulkResponseItem::id).collect(Collectors.toList());
            
        } catch (Exception e) {
            String msg = String.format(
                    "Bulk insert failed, indexName: %s", indexName
            );
            
            throw new CloudAppException(msg,
                                        "CloudApp.ElasticSearchDocumentError",
                                        e
            );
            
        }
    }
    
    @Override
    public boolean deleteDocument(String indexName, String docId)
            throws CloudAppException {
        try {
            Assert.hasText(indexName, "indexName must not be empty or blank");
            Assert.hasText(docId, "docId must not be empty or blank");
            
            DeleteRequest req = new DeleteRequest.Builder()
                    .index(indexName)
                    .id(docId)
                    .build();
            
            if (logger.isDebugEnabled()) {
                logger.debug("Delete request: {}", req.toString());
            }
            
            DeleteResponse resp = client.delete(req);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Delete response: {}", JSON.toJSONString(resp));
            }
            
            return true;
        } catch (Exception e) {
            String msg = String.format(
                    "Delete failed, indexName: %s, docId: %s",
                    indexName, docId
            );
            
            throw new CloudAppException(msg,
                                        "CloudApp.ElasticSearchDocumentError",
                                        e
            );
            
        }
    }
    
    @Override
    public boolean updateDocument(String indexName, String docId,
                                  Map<String, ?> doc) throws CloudAppException {
        try {
            Assert.hasText(indexName, "indexName must not be empty or blank");
            Assert.hasText(docId, "docId must not be empty or blank");
            Assert.notEmpty(doc, "doc must not be empty");
            
            UpdateRequest<Map<String, ?>, Map<String, ?>> req = new UpdateRequest.Builder<Map<String, ?>, Map<String, ?>>().index(indexName).id(docId).doc(doc).build();
            
            if (logger.isDebugEnabled()) {
                logger.debug("Update request: {}", req.toString());
            }
            
            UpdateResponse<Map<String, ?>> resp = client.update(req, Map.class);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Update response: {}", JSON.toJSONString(resp));
            }
            
            return Objects.nonNull(resp.result()) && resp.result().equals(
                    Result.Updated);
        } catch (Exception e) {
            String msg = String.format(
                    "Update failed, indexName: %s, docId: %s",
                    indexName, docId
            );
            
            throw new CloudAppException(msg,
                                        "CloudApp.ElasticSearchDocumentError",
                                        e
            );
            
        }
    }
    
    @Override
    public ElasticsearchClient getDelegatingClient() {
        return client;
    }
    
}
