package com.bca.payment.gatway.repository;

import com.bca.payment.gatway.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerModel,Integer> {

    CustomerModel findCustomerByCustomerNumber(Integer customerNumber);
    List<CustomerModel> findAll();
}
