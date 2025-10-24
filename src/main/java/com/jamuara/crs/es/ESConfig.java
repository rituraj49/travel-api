package com.jamuara.crs.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESConfig {
    @Bean
    public ElasticsearchClient elasticsearchClientInit() {
        return ElasticsearchClient.of(es -> es
                .host("http://localhost:9200")
        );
    }
}
