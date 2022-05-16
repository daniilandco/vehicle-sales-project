package com.github.daniilandco.vehicle_sales_project;

import com.github.daniilandco.vehicle_sales_project.repository.AdElasticSearchRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AdElasticSearchRepository.class))
@EnableElasticsearchRepositories(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AdElasticSearchRepository.class))
public class VehicleSalesProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleSalesProjectApplication.class, args);
    }
}




