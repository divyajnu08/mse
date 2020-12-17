package com.virtue.csr.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtue.csr.constants.MESSAGE_TYPES;
import com.virtue.csr.dto.Status;
import com.virtue.csr.repository.QuestionsReporsitory;
import com.virtue.csr.service.AbstractService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api("Customer Resources")
public class CustomerService extends AbstractService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private CustomerValidator validator;

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private QuestionsReporsitory questionsReporsitory;
    
    private final static int limit = 30;

	/*
	 * @ApiOperation(value = "Create new Customer", response = Status.class)
	 * 
	 * @PostMapping(value = "/customer", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<Status>
	 * create(@RequestBody Customer customer) throws Exception { // validate the
	 * request validator.validate(customer); // get the sequence value long id =
	 * getNextSequence("customer"); customer.setId(id); repository.save(customer);
	 * final Status status = new Status(true, MESSAGE_TYPES.INFO,
	 * messageSource.getMessage("customer.saved", null, null), id); return
	 * ResponseEntity.ok(status); }
	 * 
	 * @ApiOperation(value = "Update Customer", response = Status.class)
	 * 
	 * @PutMapping(value = "/customer/{id}", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<Status>
	 * update(@PathVariable long id, @RequestBody Customer customer) throws
	 * Exception { // validate the request validator.checkIfExistsById(id);
	 * validator.validate(customer); // get the sequence value customer.setId(id);
	 * repository.save(customer); final Status status = new Status(true,
	 * MESSAGE_TYPES.INFO, messageSource.getMessage("customer.updated", null, null),
	 * id); return ResponseEntity.ok(status); }
	 * 
	 * @ApiOperation(value = "Delete Customer", response = Status.class)
	 * 
	 * @DeleteMapping(value = "/customer/{id}", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<Status>
	 * update(@PathVariable long id) throws Exception {
	 * 
	 * // validate the request validator.checkIfExistsById(id); // delete the
	 * customer information repository.deleteById(id); final Status status = new
	 * Status(true, MESSAGE_TYPES.INFO, messageSource.getMessage("customer.deleted",
	 * null, null), id); return ResponseEntity.ok(status); }
	 * 
	 * @ApiOperation(value = "Retrieve Customer", response = Status.class)
	 * 
	 * @GetMapping(value = "/customer/{id}", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<Status>
	 * get(@PathVariable long id) throws Exception {
	 * 
	 * // get the customer information final Optional<Customer> byId =
	 * repository.findById(id); final Status status; if (byId.isPresent()) { final
	 * Customer customer = byId.get(); status = new Status(true, MESSAGE_TYPES.INFO,
	 * messageSource.getMessage("customer.get", null, null), customer); } else {
	 * status = new Status(false, MESSAGE_TYPES.INFO,
	 * messageSource.getMessage("customer.notfound", new
	 * String[]{String.valueOf(id)}, null), null); } return
	 * ResponseEntity.ok(status); }
	 */

    @ApiOperation(value = "Retrieve all Customer", response = Status.class)
    @GetMapping(value = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> getAll() throws Exception {

        List<Customer> customers = (List<Customer>) repository.findAll();
        final Status status = new Status(false, MESSAGE_TYPES.INFO,
                messageSource.getMessage("customer.get", null, null), customers);
        return ResponseEntity.ok(status);
    }
    
    @ApiOperation(value = "Retrieve paginated Customer", response = Status.class)
    @GetMapping(value = "/customer/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> getCustomerByStateCode(@RequestParam(required = false) String statecode) throws Exception {
    	List<Customer> customers = null;
    	if (StringUtils.isEmpty(statecode)) {
    		customers = (List<Customer>) repository.findByActive(true);
    	} else {
    		customers = (List<Customer>) repository.findByStateCodeAndActive(statecode, true);
    	}
        List<CustomerResponse> resList = new ArrayList<CustomerResponse>();
        customers.forEach(customer -> {
        	CustomerResponse cusR = new CustomerResponse();
        	cusR.setId(customer.getCustid());
        	cusR.setName(customer.getName());
        	cusR.setNetPrice(customer.getNetPrice());
        	cusR.setGraduation_rate(customer.getGraduation_rate());
        	cusR.setCoa(customer.getCoa());
        	cusR.setQuestions(questionsReporsitory.findByCustidAndActive(customer.getCustid(),true));
        	resList.add(cusR);
        });
        
        final Status status = new Status(false, MESSAGE_TYPES.INFO,
                "List Of customers", resList);
        return ResponseEntity.ok(status);
    }


}
