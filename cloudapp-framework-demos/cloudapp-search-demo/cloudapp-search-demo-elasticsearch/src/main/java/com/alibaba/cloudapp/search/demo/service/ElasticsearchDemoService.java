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

package com.alibaba.cloudapp.search.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.alibaba.cloudapp.api.search.SearchService;
import com.alibaba.cloudapp.exeption.CloudAppException;

@Service
public class ElasticsearchDemoService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchDemoService.class);

    @Autowired
    private SearchService searchService;

    private final String indexName = "test-index";

    public String insertDocument() throws CloudAppException {
        Map<String, Object> doc = new HashMap<>();
        doc.put("title", "EDAS");
        doc.put("content", "EDAS content");

        String result = searchService.insertDocument(indexName, doc);
        logger.info("insert document result: {}", JSON.toJSONString(result));

        return result;
    }

    public List<String> bulkInsertDocuments() throws CloudAppException {
        List<Map<String, ?>> docs = new ArrayList<Map<String, ?>>() {{
            add(Collections.singletonMap("key1", "value1"));
            add(Collections.singletonMap("key2", "value2"));
        }};

        List<String> result = searchService.bulkInsertDocuments(indexName, docs);
        logger.info("bulk insert document result: {}", JSON.toJSONString(result));

        return result;
    }

    public void updateDocument(String docId) throws CloudAppException {
        Map<String, Object> doc = new HashMap<>();
        doc.put("title", "title-updated");
        doc.put("content", "content-updated");

        boolean result = searchService.updateDocument(indexName, docId, doc);
        logger.info("update document result: {}", JSON.toJSONString(result));
    }

    public void deleteDocument(String docId) throws CloudAppException {
        boolean result = searchService.deleteDocument(indexName, docId);
        logger.info("delete document result: {}", JSON.toJSONString(result));
    }

    public void search() throws CloudAppException {
        String fieldName = "title";
        String pattern = "EDAS";
        int maxResults = 10;

        Collection<String> result = searchService.search(indexName, fieldName, pattern, maxResults);
        logger.info("search document result: {}", JSON.toJSONString(result));
    }

    public void searchReq() throws CloudAppException {
        String fieldName = "title";
        String pattern = "EDAS";
        SearchRequest req = new SearchRequest.Builder().index(indexName).query(q -> q.match(fq -> fq.field(fieldName).query(pattern))).build();

        Object result = searchService.search(req);
        logger.info("search req document result: {}", result.toString());
    }

    @PostConstruct
    public void init() throws CloudAppException, InterruptedException {
        String docId = insertDocument();

        // Insert data and wait about 1 second before searching, so it's easier to see the data.
        Thread.sleep(1000);
        search();
        searchReq();

        updateDocument(docId);

        deleteDocument(docId);

        List<String> docIds = bulkInsertDocuments();
        for (String id : docIds) {
            deleteDocument(id);
        }
    }
}
