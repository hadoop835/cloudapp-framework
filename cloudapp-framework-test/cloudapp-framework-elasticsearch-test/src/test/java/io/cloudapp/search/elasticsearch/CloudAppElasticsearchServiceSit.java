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
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import io.cloudapp.util.RandomStringGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CloudAppElasticsearchServiceSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppElasticsearchServiceSit.class);
    
    @Autowired
    CloudAppElasticsearchService es;
    
    @Test
    public void getDelegatingClient_create_index() {
        ElasticsearchClient elasticsearchClient = es.getDelegatingClient();
        try {
            CreateIndexResponse indexRequest = elasticsearchClient.indices()
                                                                  .create(createIndexBuilder -> createIndexBuilder
                                                                          .index("test-index")
                                                                          .aliases(
                                                                                  "test",
                                                                                  aliasBuilder -> aliasBuilder
                                                                                          .isWriteIndex(
                                                                                                  true)
                                                                          )
                                                                  );
            
            boolean acknowledged = indexRequest.acknowledged();
            logger.info("acknowledged: {}", acknowledged);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getDelegatingClient_get_index() {
        ElasticsearchClient client = es.getDelegatingClient();
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest.Builder()
                    .index("test-index")
                    .build();
            
            GetIndexResponse getIndexResponse = client.indices().get(
                    getIndexRequest);
            
            if (getIndexResponse != null && !getIndexResponse.result()
                                                             .isEmpty()) {
                logger.info("Index details: {}", getIndexResponse);
                getIndexResponse.result().forEach((index, indexMetadata) -> {
                    logger.info("Index: {}", index);
                    logger.info("Mappings: {}", indexMetadata.mappings());
                    logger.info("Settings: {}", indexMetadata.settings());
                });
            } else {
                logger.info("Index not found.");
            }
        } catch (IOException e) {
            logger.error("Error getting index details: ", e);
        }
    }
    
    @Test
    public void insertDocument_indexName_doc() {
        Map doc = new HashMap();
        doc.put("test-key", "test-value");
        String document = es.insertDocument("test-index", doc);
        logger.info("insert document result: {}", document);
    }
    
    @Test
    public void bulkInsertDocuments_indexName_docs() {
        List<Map<String, ?>> docs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map doc = new HashMap();
            doc.put("key" + i, "value" + i);
            docs.add(doc);
        }
        List<String> documents = es.bulkInsertDocuments("test-index", docs);
        logger.info("bulk insert documents result: {}", documents);
    }
    
    
    @Test
    public void search_indexName_fieldName_pattern_maxResults() {
        Collection<String> result = es.search("test-index", "test-key", "test",
                                              10
        );
        result.stream().forEach(res -> logger.info("result: {}", res));
    }
    
    @Test
    public void search_searchRequest() {
        SearchRequest sr = new SearchRequest.Builder()
                .index("test-index")
                .size(10)
                .query(Query.of(q -> q.matchAll(MatchAllQuery.of(mq -> mq))))
                .build();
        
        SearchResponse response = es.search(sr);
        logger.info("search response: {}", response);
    }
    
    @Test
    public void updateDocument_indexName_docId_doc() {
        Map doc = new HashMap();
        doc.put("test" + RandomStringGenerator.generate(5), "test-value");
        String document = es.insertDocument("test-index", doc);
        logger.info("insert document result: {}", document);
        
        Map<String, Object> new_doc = new HashMap<>();
        new_doc.put("test" + RandomStringGenerator.generate(5), "test-value");
        boolean updateDocument = es.updateDocument("test-index",
                                                   document,
                                                   new_doc
        );
        
        logger.info("update document result: {}", updateDocument);
    }
    
    @Test
    public void deleteDocument_indexName_docId() {
        Map doc = new HashMap();
        doc.put("test" + RandomStringGenerator.generate(5), "test-value");
        String document = es.insertDocument("test-index", doc);
        logger.info("insert document result: {}", document);
        
        boolean deleteDocument = es.deleteDocument("test-index", document);
        logger.info("delete document result: {}", deleteDocument);
    }
    
    
}
