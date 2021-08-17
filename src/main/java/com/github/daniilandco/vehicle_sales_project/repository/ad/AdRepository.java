package com.github.daniilandco.vehicle_sales_project.repository.ad;

import org.springframework.data.repository.CrudRepository;
import com.github.daniilandco.vehicle_sales_project.model.user.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface AdRepository extends CrudRepository<User, Integer> {

}
