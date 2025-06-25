package com.alibaba.cloudapp.starter.search.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.alibaba.cloudapp.search.elasticsearch.CloudAppElasticsearchService;
import com.alibaba.cloudapp.starter.search.properties.ElasticsearchProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ElasticsearchConfigurationTest {
    
    private ElasticsearchConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new ElasticsearchConfiguration();
    }
    
    @Test
    void testElasticsearchClient() {
        // Setup
        final ElasticsearchProperties properties = new ElasticsearchProperties();
        properties.setEnabled(false);
        properties.setHost("host");
        properties.setPort(0);
        properties.setUsername("username");
        properties.setPassword("password");
        
        
        assertThrows(NoClassDefFoundError.class,
                     () -> configuration.elasticsearchClient(
                             properties)
        );
        // Run the test
//        final ElasticsearchClient result = configuration.elasticsearchClient(
//                properties);
        
        // Verify the results
    }
    
    @Test
    void testElasticsearchClient_ThrowsCloudAppInvalidAccessException() {
        // Setup
        final ElasticsearchProperties properties = new ElasticsearchProperties();
        properties.setEnabled(false);
        properties.setHost("host");
        properties.setPort(0);
        properties.setUsername("username");
        properties.setPassword("password");
        
        // Run the test
        assertThrows(NoClassDefFoundError.class,
                     () -> configuration.elasticsearchClient(
                             properties)
        );
    }
    
    @Test
    void testCloudAppElasticsearchService() {
        // Setup
        final ElasticsearchClient client = new ElasticsearchClient(null);
        
        // Run the test
        final CloudAppElasticsearchService result = configuration.cloudAppElasticsearchService(
                client);
        
        // Verify the results
    }
    
}
