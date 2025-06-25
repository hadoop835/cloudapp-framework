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

package com.alibaba.cloudapp.search.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CloudAppElasticsearchServiceTest {
    
    @Mock
    private ElasticsearchClient client;
    
    @InjectMocks
    private CloudAppElasticsearchService service;
    
    private final String indexName = "test-index";
    private final String docId = "doc-id";
    private final Map<String, String> doc = new HashMap<String, String>() {{
        put("key1", "value1");
    }};
    private final List<Map<String, ?>> docs = new ArrayList<Map<String, ?>>() {{
        add(Collections.singletonMap("key1", "value1"));
        add(Collections.singletonMap("key2", "value2"));
    }};
    
    // Search parameters
    private final String fieldName = "test-field";
    private final String pattern = "test-pattern";
    private final int maxResults = 10;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CloudAppElasticsearchService(client);
    }
    
    @Test
    public void insertDocument_success() throws Exception {
        IndexResponse mockResponse = mock(IndexResponse.class);
        when(client.index(any(IndexRequest.class))).thenReturn(mockResponse);
        
        // Act
        String id = service.insertDocument(indexName, doc);
        
        // Assert
        verify(client).index(any(IndexRequest.class));
    }
    
    @Test(expected = CloudAppException.class)
    public void insertDocument_CloudAppException_when_indexName_is_null() {
        service.insertDocument(null, doc);
    }
    
    @Test(expected = CloudAppException.class)
    public void insertDocument_CloudAppException_when_indexName_is_blank() {
        service.insertDocument(" ", doc);
    }
    
    @Test(expected = CloudAppException.class)
    public void insertDocument_CloudAppException_when_doc_is_null() {
        service.insertDocument(indexName, null);
    }
    
    @Test(expected = CloudAppException.class)
    public void insertDocument_CloudAppException_when_doc_is_blank() {
        Map<String, Object> doc = new HashMap<>();
        service.insertDocument(indexName, doc);
    }
    
    @Test(expected = CloudAppException.class)
    public void search_CloudAppException_when_indexName_is_null() {
        service.search(null, fieldName, pattern, maxResults);
    }
    
    @Test
    public void search() throws Exception {
        
        List<Hit<Object>> hits = Collections.singletonList(
                new Hit.Builder<>()
                        .source(new HashMap<>())
                        .fields(new HashMap<>())
                        .id("1")
                        .index("default")
                        .build()
        );
        
        SearchResponse<Object> response = SearchResponse.of(
                r -> r.hits(h -> h.total(
                        t -> t.value(1L)
                              .relation(TotalHitsRelation.Eq)
                ).hits(hits)).took(1000L).timedOut(false).shards(
                        s -> s.successful(1).total(1)
                              .skipped(0).failed(0)
                ));
        when(client.search(any(SearchRequest.class), eq(Object.class)))
                .thenReturn(response);
        
        // Act
        service.search("default", fieldName, pattern, maxResults);
        
        // Assert
        verify(client).search(any(SearchRequest.class), eq(Object.class));
    }
    
    @Test(expected = CloudAppException.class)
    public void search_CloudAppException_when_indexName_is_blank() {
        service.search(" ", fieldName, pattern, maxResults);
    }
    
    @Test(expected = CloudAppException.class)
    public void search_CloudAppException_when_fieldName_is_null() {
        service.search(indexName, null, pattern, maxResults);
    }
    
    @Test(expected = CloudAppException.class)
    public void search_CloudAppException_when_fieldName_is_blank() {
        service.search(indexName, " ", pattern, maxResults);
    }
    
    @Test(expected = CloudAppException.class)
    public void search_CloudAppException_when_pattern_is_null() {
        service.search(indexName, fieldName, null, maxResults);
    }
    
    @Test(expected = CloudAppException.class)
    public void search_CloudAppException_when_pattern_is_blank() {
        service.search(indexName, fieldName, " ", maxResults);
    }
    
    @Test
    public void search_request_success() throws Exception {
        SearchResponse<Object> mockResponse = mock(SearchResponse.class);
        when(client.search(any(SearchRequest.class),
                           eq(Object.class)
        )).thenReturn(mockResponse);
        
        // Act
        SearchResponse response = service.search(mock(SearchRequest.class));
        
        // Assert
        assertNotNull(response);
        verify(client).search(any(SearchRequest.class), eq(Object.class));
    }
    
    @Test(expected = CloudAppException.class)
    public void search_request_CloudAppException_when_request_is_null() {
        service.search(null);
    }
    
    
    @Test
    public void bulkInsertDocuments_success() throws Exception {
        BulkResponse mockResponse = mock(BulkResponse.class);
        when(client.bulk(any(BulkRequest.class))).thenReturn(mockResponse);
        
        // Act
        List<String> ids = service.bulkInsertDocuments(indexName, docs);
        
        // Assert
        assertNotNull(ids);
        verify(client).bulk(any(BulkRequest.class));
    }
    
    @Test(expected = CloudAppException.class)
    public void bulkInsertDocuments_CloudAppException_when_indexName_is_null() {
        service.bulkInsertDocuments(null, docs);
    }
    
    @Test(expected = CloudAppException.class)
    public void bulkInsertDocuments_CloudAppException_when_indexName_is_blank() {
        service.bulkInsertDocuments(" ", docs);
    }
    
    @Test(expected = CloudAppException.class)
    public void bulkInsertDocuments_CloudAppException_when_docs_is_null() {
        service.bulkInsertDocuments(indexName, null);
    }
    
    @Test(expected = CloudAppException.class)
    public void bulkInsertDocuments_CloudAppException_when_docs_is_blank() {
        List<Map<String, ?>> docs = new ArrayList<>();
        service.bulkInsertDocuments(indexName, docs);
    }
    
    @Test(expected = CloudAppException.class)
    public void updateDocument_CloudAppException_when_indexName_is_null() {
        service.updateDocument(null, docId, doc);
    }
    
    @Test(expected = CloudAppException.class)
    public void updateDocument_CloudAppException_when_indexName_is_blank() {
        service.updateDocument(" ", docId, doc);
    }
    
    @Test
    public void updateDocument() throws IOException {
        UpdateResponse<Map<String, ?>> resp =
                new UpdateResponse.Builder<Map<String, ?>>()
                        .result(Result.Updated)
                        .id("1")
                        .index("default")
                        .version(1)
                        .shards(s -> s.successful(1).total(1)
                                      .skipped(0).failed(0))
                        .build();
        
        doReturn(resp).when(client).update(
                (UpdateRequest<Map<String, ?>, Map<String, ?>>) any(),
                eq(Map.class)
        );
        
        service.updateDocument("default", docId, doc);
    }
    
    @Test(expected = CloudAppException.class)
    public void updateDocument_CloudAppException_when_docId_is_null() {
        service.updateDocument(indexName, null, doc);
    }
    
    @Test(expected = CloudAppException.class)
    public void updateDocument_CloudAppException_when_docId_is_blank() {
        service.updateDocument(indexName, " ", doc);
    }
    
    @Test(expected = CloudAppException.class)
    public void updateDocument_CloudAppException_when_doc_is_null() {
        service.updateDocument(indexName, docId, null);
    }
    
    @Test(expected = CloudAppException.class)
    public void updateDocument_CloudAppException_when_doc_is_empty() {
        service.updateDocument(indexName, docId, Collections.emptyMap());
    }
    
    @Test
    public void deleteDocument_success() throws Exception {
        DeleteResponse mockResponse = mock(DeleteResponse.class);
        when(client.delete(any(DeleteRequest.class))).thenReturn(mockResponse);
        
        // Act
        boolean result = service.deleteDocument(indexName, docId);
        
        // Assert
        assertTrue(result);
    }
    
    @Test(expected = CloudAppException.class)
    public void deleteDocument_CloudAppException_when_indexName_is_null() {
        service.deleteDocument(null, docId);
    }
    
    @Test(expected = CloudAppException.class)
    public void deleteDocument_CloudAppException_when_indexName_is_blank() {
        service.deleteDocument(" ", docId);
    }
    
    @Test(expected = CloudAppException.class)
    public void deleteDocument_CloudAppException_when_docId_is_blank() {
        service.deleteDocument(indexName, "");
    }
    
    
    @Test
    public void getDelegatingClient_ReturnsCorrectClient() {
        ElasticsearchClient delegatingClient = service.getDelegatingClient();
        
        assertNotNull(delegatingClient);
        assertEquals(client, delegatingClient);
    }
    
}
