package com.github.daniilandco.vehicle_sales_project.repository.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.SearchByParamsRequest;
import com.github.daniilandco.vehicle_sales_project.exception.CategoryException;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import org.springframework.stereotype.Repository;

@Repository
public interface AdHibernateSearchRepository {
    Iterable<Ad> search(String terms);

    Iterable<Ad> search(SearchByParamsRequest request) throws CategoryException;
}
