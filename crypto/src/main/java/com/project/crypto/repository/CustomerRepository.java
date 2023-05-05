package com.project.crypto.repository;

import com.project.crypto.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerModel,Integer> {
    CustomerModel findCustomerById (Integer custId);
    List<CustomerModel> findAll();
}
