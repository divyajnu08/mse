package com.virtue.csr.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Optional<Customer> findByName(String name);
    
    List<Customer> findByStateCodeAndActive(String stateCode, Boolean active);
    
    List<Customer> findByActive(Boolean active);
    
    Optional<Customer> findByCustid(long custid);
}
