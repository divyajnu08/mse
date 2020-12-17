package com.virtue.csr.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.virtue.csr.model.Country;

public interface CountryReporsitory extends MongoRepository<Country, Integer>{
	
	Optional<Country> findOneByIso2(String countryCode);
	
}
