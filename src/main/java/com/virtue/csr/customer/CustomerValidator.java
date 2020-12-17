package com.virtue.csr.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.virtue.csr.exception.ApplicationException;

@Component
public class CustomerValidator {

    @Autowired
    private CustomerRepository repository;

    public void validate(Customer customer) throws ApplicationException {
        List<String> errors = new ArrayList<>();
        final String name = customer.getName();
        if(StringUtils.isEmpty(name)) {
            errors.add("Customer name must be provided");
        }
        if(!errors.isEmpty()) {
            throw new ApplicationException(StringUtils.collectionToDelimitedString(errors, System.lineSeparator()));
        }
    }

    public void checkIfExistsById(long id) throws ApplicationException {
        final Optional<Customer> byId = repository.findById(id);
        if(!byId.isPresent()) {
            throw new ApplicationException(String.format("Customer not found with the provided id [%d]", id));
        }
    }
}
