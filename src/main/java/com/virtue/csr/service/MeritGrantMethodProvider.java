package com.virtue.csr.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.virtue.csr.constants.MeritKeys;
import com.virtue.csr.constants.TestTypes;
import com.virtue.csr.dto.KeyValue;
import com.virtue.csr.model.CSRData;
import com.virtue.csr.model.ChoiceInformation;
import com.virtue.csr.model.MeritObject;
import com.virtue.csr.model.Question;
import com.virtue.csr.repository.QuestionsReporsitory;
import com.virtue.csr.util.ConversionUtils;

@Component
public class MeritGrantMethodProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(MeritGrantMethodProvider.class);
	private Map<Integer,Function<Object[],Double>> methodMap;
	private final int ID_22=22002;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	QuestionsReporsitory quesRepository;
	
	@Autowired
	private ConversionUtils utils;

	/**
	 * This map is contains the functions based on the customerid of the college
	 */
	public MeritGrantMethodProvider(){
		methodMap = new HashMap<Integer,Function<Object[],Double>>();
		methodMap.put(new Integer(22), this::calculateMeritGrant_22 );
		methodMap.put(new Integer(-1), this::calculateMeritGrant_default );
	}
	
	/**
	 * This function picks up the merit grant according to the act and gpa of the student
	 * merit_grant collection in the database has merit values according to the act and gpa scores
	 * @param custid this is the customer id for college
	 * @param campid this us the campus id for the college
	 * @param act the ACT value 
	 * @param gpa high school GPA out of 1-4
	 * @return double merit value calculated from merit_grant collection 
	 * 
	 */
	public double calculateMeritGrant_default(Object... vars) {
		double merit=0d;
		int custid=((Integer)vars[0]).intValue();
		CSRData obj=(CSRData)vars[2];
		TestTypes testType=obj.getAddtnInfo().getScore_type();
		Query findQuery = new Query();
		double gpa=obj.getAddtnInfo().getGpa();
		double act=getACTFromScore(testType,obj.getAddtnInfo().getScore());
		findQuery.addCriteria(
				new Criteria().andOperator(
				Criteria.where("groupKey").is(MeritKeys.MERIT),
				Criteria.where("custid").is(custid),
				Criteria.where("field1_min").lte(gpa),
				Criteria.where("field1_max").gt(gpa),
				Criteria.where("field2_min").lte(act),
				Criteria.where("field2_max").gt(act)
		));
		List<MeritObject> resultList = mongoTemplate.find(findQuery, MeritObject.class, "merit_grid");
		merit=Optional.ofNullable(resultList.stream().max(Comparator.nullsFirst((o1,o2) -> {
			return Double.compare(o1.getValue(), o2.getValue());
		})).orElse(new MeritObject()).getValue()).orElse(0d);
		return merit;
	}
	
	public double calculateMeritGrant_22(Object... vars) {
		double merit=0d;
		int custid=((Integer)vars[0]).intValue();
		CSRData obj=(CSRData)vars[2];
		TestTypes testType=obj.getAddtnInfo().getScore_type();
		String defaultMeritKey=MeritKeys.MERIT.toString();
		String extMeritKey=MeritKeys.MERIT_EXT.toString();
		Criteria varCriteria=Criteria.where("groupKey").is(defaultMeritKey);
		ChoiceInformation choiceInfo = obj.getChoiceInfo();
		Query findQuery = new Query();
		double gpa=obj.getAddtnInfo().getGpa();
		double act=getACTFromScore(testType,obj.getAddtnInfo().getScore());
		Criteria commonCriteria=new Criteria().andOperator(
				Criteria.where("custid").is(custid),
				Criteria.where("field1_min").lte(gpa),
				Criteria.where("field1_max").gt(gpa),
				Criteria.where("field2_min").lte(act),
				Criteria.where("field2_max").gt(act)
		);
		if (choiceInfo != null) {
			//get the list of all affinity questions for the college
			List<KeyValue> questions = choiceInfo.getCustomers().get(custid);
			for (KeyValue question : questions) {
				Integer questionId = Integer.valueOf(question.getKey());
				boolean answer = utils.ansToBoolean(question.getValue());
				//if user has selected the question as true  ,
				if (answer) {
					Question doc = quesRepository.findById(Long.valueOf(questionId)).orElse(null);
					if(doc != null && doc.getTextid()==ID_22) {
						varCriteria=Criteria.where("groupKey").is(extMeritKey);
					}
				}
			}
		}
		findQuery.addCriteria(new Criteria().andOperator(commonCriteria,varCriteria));
		List<MeritObject> resultList = mongoTemplate.find(findQuery, MeritObject.class, "merit_grid");
		merit=Optional.ofNullable(resultList.stream().max(Comparator.nullsFirst((o1,o2) -> {
			return Double.compare(o1.getValue(), o2.getValue());
		})).orElse(new MeritObject()).getValue()).orElse(0d);

		return merit;
	}
	
	public Map<Integer, Function<Object[], Double>> getMethodMap() {
		return methodMap;
	}

	public void setMethodMap(Map<Integer, Function<Object[], Double>> methodMap) {
		this.methodMap = methodMap;
	}
	
	private double getACTFromScore(TestTypes testType,double score) {
		double convertedValue=0d;
		if(testType.equals(TestTypes.CLT)) {
			convertedValue=utils.clt2act(score, 0.0);
		}else if(testType.equals(TestTypes.SAT)) {
			convertedValue=utils.sat2act(score, 0.0);
		}else {
			convertedValue=score;
		}
		return convertedValue;
	}

}

