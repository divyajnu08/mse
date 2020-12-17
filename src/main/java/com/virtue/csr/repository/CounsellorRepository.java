/**
 * 
 */
package com.virtue.csr.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.virtue.csr.model.CounsellorObject;

@Repository
public interface CounsellorRepository extends MongoRepository<CounsellorObject, Object>{

	List<CounsellorObject> findByInstitutionName(String name);
	
	@Query(value="{'institutionName' : ?0 }",count=true)
	long count(String instName);

	@Aggregation(pipeline = {
			"{ $match : { $and : [ { 'institutionName' : ?0 } , { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $project : { '_id':0 , 'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'ethnicity':1 } }",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findCompletedApplications(String instName, Pageable page);

	@Aggregation(pipeline = {
			"{ $match : { $and : [ { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $project : { '_id':0 , 'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'ethnicity':1 } }",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findCompletedApplications(Pageable page);

	@Query(value = "{'institutionName' : ?0 , 'submissionStatus' : 1 , 'transcripts'  : 1 , 'readerScore1' : { $gte : 0 } , 'readerScore2' : { $gte : 0 } }", count = true)
	long countCompletedApplications(String instName);

	@Query(value = "{'submissionStatus' : 1 , 'transcripts'  : 1 , 'readerScore1' : { $gte : 0 } , 'readerScore2' : { $gte : 0 } }", count = true)
	long countCompletedApplications();

	@Aggregation(pipeline = {
			"{ $match: { $and : [ { 'institutionName' : ?0 } , { $or: [ { 'submissionStatus' : 0 } , { 'transcripts' : 0 } , { 'readerScore1' : null } , { 'readerScore2' : null } ] } ] } }",
			"{ $project: { '_id':0 ,'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'completedSteps':1 , percentCompletion: { $divide: [ { $multiply: [ $completedSteps, 100 ] }, $totalSteps ] } } } ",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findInCompleteApplications(String instName, Pageable page);

	@Aggregation(pipeline = {
			"{ $match:  { $or: [ { 'submissionStatus' : 0 } , { 'transcripts' : 0 } , { 'readerScore1' : null } , { 'readerScore2' : null } ] } }",
			"{ $project: { '_id':0 ,'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'completedSteps':1 , percentCompletion: { $divide: [ { $multiply: [ $completedSteps, 100 ] }, $totalSteps ] } } } ",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findInCompleteApplications(Pageable page);

	@Query(value = "{ $and : [ { 'institutionName' : ?0 } , { $or: [ { 'submissionStatus' : 0 } , { 'transcripts' : 0 } , { 'readerScore1' : null } , { 'readerScore2' : null } ] } ]}", count = true)
	long countInCompleteApplications(String instName);
	
	@Query(value = "{ $or: [ { 'submissionStatus' : 0 } , { 'transcripts' : 0 } , { 'readerScore1' : null } , { 'readerScore2' : null } ] }", count = true)
	long countInCompleteApplications();

	@Aggregation(pipeline = {
			"{ $match: { $and: [ { 'institutionName' : ?0 } , { $expr:{ $eq : [ $completedSteps , $totalSteps ] } } ,{ 'submissionStatus' : 0 }  , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $project: { '_id':0 , 'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'completedSteps':1 } }",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findOnlySubmissionPendingApplications(String instName, Pageable page);

	@Aggregation(pipeline = {
			"{ $match: { $and: [ { $expr:{ $eq : [ $completedSteps , $totalSteps ] } } ,{ 'submissionStatus' : 0 }  , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $project: { '_id':0 , 'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'completedSteps':1 } }",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findOnlySubmissionPendingApplications(Pageable page);

	@Query(value = "{ $and: [ { 'institutionName' : ?0 } , { $expr:{ $eq : [ $completedSteps , $totalSteps ] } } ,{ 'submissionStatus' : 0 }  , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }", count = true)
	long countOnlySubmissionPendingApplications(String instName);

	@Query(value = "{ $and: [ { $expr:{ $eq : [ $completedSteps , $totalSteps ] } } ,{ 'submissionStatus' : 0 }  , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }", count = true)
	long countOnlySubmissionPendingApplications();

	@Aggregation(pipeline = {
			"{ $match: { $and: [ { 'institutionName' : ?0 } , { 'submissionStatus' : 1 } , { 'transcripts' : 0 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }}",
			"{ $project: { '_id':0 , 'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'completedSteps':1 } }",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findOnlyTranscriptsPendingApplications(String instName, Pageable page);

	@Aggregation(pipeline = {
			"{ $match: { $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 0 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }}",
			"{ $project: { '_id':0 , 'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'completedSteps':1 } }",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findOnlyTranscriptsPendingApplications(Pageable page);

	@Query(value = "{ $and: [ { 'institutionName' : ?0 } , { 'submissionStatus' : 1 } , { 'transcripts' : 0 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }", count = true)
	long countOnlyTranscriptsPendingApplications(String instName);

	@Query(value = "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 0 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }", count = true)
	long countOnlyTranscriptsPendingApplications();

	@Aggregation(pipeline = {
			"{ $match: { $and: [ { 'institutionName' : ?0 } , { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { $or : [ { 'readerScore1' : null } , { 'readerScore2' : null } ] }] }}",
			"{ $project: { '_id':0 , 'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'readerScore1':1 , 'readerScore2':1 , 'colorCode':1 } }",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findOnlyRecommendationsPendingApplications(String instName, Pageable page);

	@Aggregation(pipeline = {
			"{ $match: { $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { $or : [ { 'readerScore1' : null } , { 'readerScore2' : null } ] } ] }}",
			"{ $project: { '_id':0 , 'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'readerScore1':1 , 'readerScore2':1 , 'colorCode':1 } }",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findOnlyRecommendationsPendingApplications(Pageable page);

	@Query(value = "{ $and: [ { 'institutionName' : ?0 } , { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { $or : [ { 'readerScore1' : null } , { 'readerScore2' : null } ] } ] }", count = true)
	long countOnlyRecommendationsPendingApplications(String instName);

	@Query(value = "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { $or : [ { 'readerScore1' : null } , { 'readerScore2' : null } ] } ] }", count = true)
	long countOnlyRecommendationsPendingApplications();

	@Aggregation(pipeline = { "{ $match: { $and : [ { 'institutionName' : ?0 } ,  { $nor: [ "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 0 } , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 0 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { $or : [ { 'readerScore1' : null } , { 'readerScore2' : null } ] } ] } "
			+ "] } ] } } ",
			"{ $project: { '_id':0 , 'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'submittedApplicantRecommendations':1 , 'completedSteps':1 } }",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findMultipleStepsPendingApplications(String instName, Pageable page);

	@Aggregation(pipeline = { "{ $match: { $nor: [ "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 0 } , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 0 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { $or : [ { 'readerScore1' : null } , { 'readerScore2' : null } ] } ] } "
			+ "] } } ",
			"{ $project: { '_id':0 , 'applicationCode':1 , 'firstName':1 ,'lastName':1 , 'submittedApplicantRecommendations':1 , 'completedSteps':1 } }",
			"{ $sort : { 'applicationCode' : 1 } }" })
	Optional<List<HashMap<String, String>>> findMultipleStepsPendingApplications(Pageable page);

	@Query(value = "{ $and : [ { 'institutionName' : ?0 } ,  { $nor: [ "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 0 } , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 0 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { $or : [ { 'readerScore1' : null } , { 'readerScore2' : null } ] } ] } "
			+ "] } ] }", count = true)
	long countMultipleStepsPendingApplications(String instName);

	@Query(value = "{ $nor: [ "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 0 } , { 'transcripts' : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 0 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] }, "
			+ "{ $and: [ { 'submissionStatus' : 1 } , { 'transcripts' : 1 } , { $or : [ { 'readerScore1' : null } , { 'readerScore2' : null } ] } ] } "
			+ "] }", count = true)
	long countMultipleStepsPendingApplications();

	@Aggregation(pipeline = { "{ $match: { $and : [ { 'institutionName' : ?0 } , { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $group: { '_id' : '$ethnicity' , 'count' : { $sum : 1 }} }",
			"{ $project: { 'ethnicity':1 , 'count':1 }}" })
	Optional<List<HashMap<String, String>>> groupByEthnicity(String instName);
	
	@Aggregation(pipeline = { "{ $match: { $and : [ { 'category' : ?0 } , { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $group: { '_id' : '$ethnicity' , 'count' : { $sum : 1 }} }",
			"{ $project: { 'ethnicity':1 , 'count':1 }}" })
	Optional<List<HashMap<String, String>>> groupByEthnicityPerRegion(String region);

	@Aggregation(pipeline = { "{ $match: { $and : [ { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }", 
			"{ $group: { '_id' : '$ethnicity' , 'count' : { $sum : 1 }} }",
			"{ $project: { 'ethnicity':1 , 'count':1 }}" })
	Optional<List<HashMap<String, String>>> groupByEthnicity();
	
	@Aggregation(pipeline = { "{ $match: { $and : [ { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }", 
			"{ $group: { '_id' : '$ethnicity' , 'count' : { $sum : 1 }} }",
			"{ $project: { 'ethnicity':1 , 'count':1 }}" })
	Optional<List<HashMap<String, String>>> groupByEthnicityAllRegions();

	@Aggregation(pipeline = { "{ $match: { $and : [ { 'institutionName' : ?0 } , { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $group: { '_id' : '$gender' , 'count' : { $sum : 1 }} }", 
			"{ $project: { 'gender':1 , 'count':1 }}" })
	Optional<List<HashMap<String, String>>> groupByGender(String instName);
	
	@Aggregation(pipeline = { "{ $match: { $and : [ { 'category' : ?0 } , { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $group: { '_id' : '$gender' , 'count' : { $sum : 1 }} }", 
			"{ $project: { 'gender':1 , 'count':1 }}" })
	Optional<List<HashMap<String, String>>> groupByGenderPerRegion(String region);

	@Aggregation(pipeline = { "{ $match: { $and : [ { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }", 
			"{ $group: { '_id' : '$gender' , 'count' : { $sum : 1 }} }",
			"{ $project: { 'gender':1 , 'count':1 }}" })
	Optional<List<HashMap<String, String>>> groupByGender();
	
	@Aggregation(pipeline = { "{ $match: { $and : [ { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }", 
			"{ $group: { '_id' : '$gender' , 'count' : { $sum : 1 }} }",
			"{ $project: { 'gender':1 , 'count':1 }}" })
	Optional<List<HashMap<String, String>>> groupByGenderAllRegions();

	@Aggregation(pipeline = {
			"{ $match : { $and : [ { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $group: { '_id' : '$institutionName' , 'count' : { $sum : 1 }} }",
			"{ $sort: { 'count' : -1 } }",
			"{ $limit: 10 }",
			"{ $project : { 'institutionName':1 , 'count':1 } }" })
	Optional<List<HashMap<String, String>>> groupCompletedApplicationsBySchool();
	
	@Aggregation(pipeline = {
			"{ $match : { $and : [ { 'category' : ?0 }, { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $group: { '_id' : '$ceeb' , 'count' : { $sum : 1 }} }",
			"{ $project : { 'ceeb':1 , 'count':1 } }" })
	Optional<List<HashMap<String, Integer>>> groupCompletedApplicationsBySchoolPerRegion(String region);
	
	@Aggregation(pipeline = {
			"{ $match : { $and : [ { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }",
			"{ $group: { '_id' : '$ceeb' , 'count' : { $sum : 1 }} }",
			"{ $project : { 'ceeb':1 , 'count':1 } }" })
	Optional<List<HashMap<String, Integer>>> groupCompletedApplicationsBySchoolAllRegions();
	
	@Aggregation(pipeline = { 
			"{ $facet : { 'Total' : [{ $group : { '_id' : '$institutionName' , 'count' : { $sum : 1 }} }] ,"
			+ "'Complete' : [ "
			+ "{ $match : { $and : [ { 'submissionStatus' : 1 } , { 'transcripts'  : 1 } , { 'readerScore1' : { $gte : 0 } } , { 'readerScore2' : { $gte : 0 } } ] } }, "
			+ "{ $group : { '_id' : '$institutionName' , 'count' : { $sum : 1 }} }"
			+ "] }}"})
	Optional<HashMap<String,List<HashMap<String,String>>>> groupApplicationsBySchool();

}
