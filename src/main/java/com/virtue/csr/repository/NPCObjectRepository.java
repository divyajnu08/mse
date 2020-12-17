package com.virtue.csr.repository;



import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.virtue.csr.model.NPCObject;

@Repository
public interface NPCObjectRepository extends MongoRepository<NPCObject, String>{
   
	Optional<NPCObject> findOneByStudentId(String student_id);
}
