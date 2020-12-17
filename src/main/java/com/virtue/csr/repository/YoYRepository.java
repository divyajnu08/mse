/**
 * 
 */
package com.virtue.csr.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.virtue.csr.model.YoYData;

/**
 *
 */
@Repository
public interface YoYRepository extends MongoRepository<YoYData, Object> {

	@Aggregation(pipeline = { "{ $match : { 'hs_name' : ?0 }}" , 
							  "{ $project : { _id : $year , Jan : 1 , Feb : 1 , Mar : 1, Apr : 1, May : 1 , Jun : 1 "
							  + ", Jul : 1, Aug : 1, Sep : 1 , Oct : 1, Nov : 1 , Dec: 1 }}"})
	public List<HashMap<String, Integer>> countApplicationsByHighSchool(String instName);

	@Aggregation(pipeline = { "{ $group : { '_id': $year ,"
						+ "'Jan': { $sum :'$Jan'}, 'Feb': { $sum :'$Feb'},'Mar': { $sum :'$Mar'},'Apr': { $sum :'$Apr'},"
						+ "'May': { $sum :'$May'},'Jun': { $sum :'$Jun'} , 'Jul': { $sum :'$Jul'}," 
						+ "'Aug': { $sum :'$Aug'}, 'Sep': { $sum :'$Sep'} , 'Oct': { $sum :'$Oct'},"
						+ "'Nov': { $sum :'$Nov'} , 'Dec': { $sum :'$Dec'} }}"})
	public List<HashMap<String,Integer>> countAllApplications();

}
