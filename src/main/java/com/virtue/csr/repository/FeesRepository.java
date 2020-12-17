package com.virtue.csr.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.virtue.csr.constants.FeesItems;
import com.virtue.csr.constants.HousingOptions;
import com.virtue.csr.model.FeesObject;

@Repository
public interface FeesRepository  extends MongoRepository<FeesObject, String>{
	
	List<FeesObject> findByKey(FeesItems key);
	List<FeesObject> findByYear(String year);
	List<FeesObject> findByYearAndHousingOption(String year,HousingOptions option);
	List<FeesObject> findByCustidAndCampidAndYearAndHousingOption(int custid,int campid,String year,HousingOptions option);
	List<FeesObject> findByCustidAndYearAndHousingOption(int custid,String year,HousingOptions option);

}
