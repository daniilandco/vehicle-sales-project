package com.github.daniilandco.vehicle_sales_project.repository.ad;

import com.github.daniilandco.vehicle_sales_project.controller.request.SearchByParamsRequest;
import com.github.daniilandco.vehicle_sales_project.exception.CategoryException;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.category.Category;
import com.github.daniilandco.vehicle_sales_project.service.category.CategoryService;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AdHibernateSearchRepositoryImplementation implements AdHibernateSearchRepository {

    private final CategoryService categoryService;
    private final int PAGINATION = 20;
    @Autowired
    private AdRepository adRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public AdHibernateSearchRepositoryImplementation(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public List search(String queryRequest) {
        // get fullTextEntityManager, используя entityManager
        var fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

        // create query using Hibernate Search query DSL
        var queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Ad.class)
                .get();

        // mark fields for searching on them
        var query = queryBuilder
                .keyword()
                .onFields("title", "description")
                .matching(queryRequest)
                .createQuery();

        //wrap Lucene Query into Hibernate Query object
        var jpaQuery = fullTextEntityManager.createFullTextQuery(query, Ad.class);

        //return entities list
        return jpaQuery.getResultList();
    }

    @Override
    public Iterable<Ad> search(SearchByParamsRequest request) throws CategoryException {

        var fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

        var adQueryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Ad.class)
                .get();

        var priceQuery = (request.getPriceRange() != null) ? adQueryBuilder
                .range()
                .onField("price")
                .from(request.getPriceRange().getFirst())
                .to(request.getPriceRange().getSecond())
                .createQuery() : adQueryBuilder.all().createQuery();

        var releaseYearQuery = (request.getReleaseYearRange() != null) ? adQueryBuilder
                .range()
                .onField("release_year")
                .from(request.getReleaseYearRange().getFirst())
                .to(request.getReleaseYearRange().getSecond())
                .createQuery() : adQueryBuilder.all().createQuery();

        var finalQuery = adQueryBuilder
                .bool()
                .must(priceQuery)
                .must(releaseYearQuery)
                .createQuery();

        FullTextQuery fullTextQuery = fullTextEntityManager
                .createFullTextQuery(finalQuery, Ad.class);

        Sort sort = new Sort(new SortField("created_at", SortField.Type.LONG));

        fullTextQuery
                .setSort(sort)
                .setFirstResult(PAGINATION);

        List list = fullTextQuery.getResultList();

        Category category = categoryService.getCategory(request.getCategoriesHierarchy());

        return (Iterable<Ad>) list.stream().filter(ad -> category.equals(((Ad) ad).getCategory()))
                .collect(Collectors.toList());

    }
}
