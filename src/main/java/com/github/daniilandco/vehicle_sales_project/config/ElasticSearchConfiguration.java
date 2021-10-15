package com.github.daniilandco.vehicle_sales_project.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.net.URI;

@Configuration
public class ElasticSearchConfiguration extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.url}")
    private String elasticsearchUrl;

    @Bean
    @Profile({"dev"})
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration configuration = ClientConfiguration.builder()
                    .connectedTo(elasticsearchUrl)
                    .build();

        return RestClients.create(configuration).rest();
    }

    @Bean
    @Profile({"stage"})
    public RestHighLevelClient elasticsearchClientStage() {
        URI elasticUri = URI.create(elasticsearchUrl);

        String[] credentials = elasticUri.getUserInfo().split(":");
        final ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo(elasticUri.getAuthority().split("@")[1])
                .usingSsl()
                .withBasicAuth(credentials[0], credentials[1])
                .build();

        return RestClients.create(configuration).rest();
    }

}