package com.github.daniilandco.vehicle_sales_project.repository.ad;

import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AdElasticSearchRepository extends ElasticsearchRepository<Ad, Long> {
}
