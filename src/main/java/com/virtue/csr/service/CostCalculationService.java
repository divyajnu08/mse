package com.virtue.csr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.virtue.csr.constants.AdmissionTypes;
import com.virtue.csr.constants.CONSTANTS;
import com.virtue.csr.constants.FeesItems;
import com.virtue.csr.constants.HousingOptions;
import com.virtue.csr.constants.PellAwardTypes;
import com.virtue.csr.constants.Scenario;
import com.virtue.csr.constants.TestTypes;
import com.virtue.csr.customer.Customer;
import com.virtue.csr.customer.CustomerRepository;
import com.virtue.csr.dto.KeyValue;
import com.virtue.csr.dto.ResultDto;
import com.virtue.csr.model.BasicInformation;
import com.virtue.csr.model.CSRData;
import com.virtue.csr.model.ChoiceInformation;
import com.virtue.csr.model.CustGrantRule;
import com.virtue.csr.model.FeesObject;
import com.virtue.csr.model.NPCObject;
import com.virtue.csr.model.Question;
import com.virtue.csr.model.ResultObject;
import com.virtue.csr.model.ScholarshipInfo;
import com.virtue.csr.model.student.OtherInformation;
import com.virtue.csr.repository.FeesRepository;
import com.virtue.csr.repository.MeritReporsitory;
import com.virtue.csr.repository.QuestionsReporsitory;
import com.virtue.csr.util.ConversionUtils;

@Service
public class CostCalculationService {
	
	@Autowired
	MeritGrantMethodProvider meritMethodProvider;
	
	@Autowired
	FeesRepository feeRepository;
	
	@Autowired
	MeritReporsitory meritRepository;
	
	@Autowired
	QuestionsReporsitory questionRepository;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	StateGrantMethodProvider stateMethodProvider;

	@Autowired
	private HashMap<String, Object> configProperties;
	
	@Autowired
	private ConversionUtils utils;
	
	@Autowired
	private CustomerRepository custRepository;

	
	public Map<Integer,List<ResultDto>> generateOutput(CSRData obj,NPCObject npcObj){
		Map<Integer,List<ResultDto>> map=new HashMap<Integer,List<ResultDto>>();
		if(obj.getChoiceInfo().getCustomers()!=null) {
			for(Integer custid:obj.getChoiceInfo().getCustomers().keySet()) {
				Customer customer=mongoTemplate.findOne(new Query(Criteria.where("custid").is(custid)), Customer.class);
				map.put(custid,generateOutput(custid,custid,obj,npcObj));
			}
		}
		return map;
	}
	
	public List<ResultDto> generateOutput(int custid,int campid,CSRData object,NPCObject npcObj){
		ResultObject obj=processResult(custid,campid,object,npcObj);
		Query query = new Query();
		query.addCriteria(Criteria.where("groupKey").is(CONSTANTS.RESULT_OBJECT_KEY));
		List<Document> result = mongoTemplate.find(query, Document.class, "csr_config");
		List<Document> labelList = result.stream().sorted((o1, o2) -> {
			int index1 = Integer
					.valueOf(((String) o1.get("key")).substring(((String) o1.get("key")).lastIndexOf(".") + 1));
			int index2 = Integer
					.valueOf(((String) o2.get("key")).substring(((String) o2.get("key")).lastIndexOf(".") + 1));
			return Integer.compare(index1, index2);
		}).collect(Collectors.toList());
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		ResultDto dto = null;
		dto = new ResultDto(labelList.get(0).getString("key"), String.valueOf(Math.round(Optional.ofNullable(obj.getNetPrice()).orElse(0.0))),
				labelList.get(0).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(1).getString("key"), String.valueOf(Math.round(Optional.ofNullable(obj.getCoa()).orElse(0.0))),
				labelList.get(1).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(2).getString("key"), String.valueOf(Math.round(Optional.ofNullable(obj.getTotalGrants()).orElse(0.0))),
				labelList.get(2).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(3).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), getTutionType(object.getBasicInfo().getState())))),
				labelList.get(3).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(4).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.ACTIVITY))),
				labelList.get(4).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(5).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.HEALTH_SERVICE))),
				labelList.get(5).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(6).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.IT))),
				labelList.get(6).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(7).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.ROOM))),
				labelList.get(7).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(8).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.MEAL))),
				labelList.get(8).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(9).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.PERSONAL))),
				labelList.get(9).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(10).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.TRANSPORT))),
				labelList.get(10).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(11).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.BOOKS))),
				labelList.get(11).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(12).getString("key"), String.valueOf(Math.round(obj.getTotalGrants())),
				labelList.get(12).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(13).getString("key"), String.valueOf(Math.round(obj.getPellGrant())),
				labelList.get(13).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(14).getString("key"), String.valueOf(Math.round(obj.getStateGrant())),
				labelList.get(14).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(15).getString("key"), String.valueOf(Math.round(obj.getFseog())),
				labelList.get(15).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(16).getString("key"), String.valueOf(Math.round(obj.getMerit())),
				labelList.get(16).getString("value"));
		resultList.add(dto);
		Map<String,Double> otherAwards=obj.getOtherAwards();
		for(String questionId:otherAwards.keySet()) {
			Question ques=questionRepository.findById(Long.valueOf(questionId)).get();
			dto=new ResultDto("other grants", String.valueOf(Math.round(ques.getAwardAmount())),ques.getLabel());
			resultList.add(dto);
		}
		dto = new ResultDto(labelList.get(17).getString("key"), String.valueOf(Math.round(obj.getDsl())),
				labelList.get(17).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(18).getString("key"), String.valueOf(Math.round(obj.getDusl())),
				labelList.get(18).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(19).getString("key"), String.valueOf(Math.round(obj.getAddtnFinancialAid())),
				labelList.get(19).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(20).getString("key"), String.valueOf(Math.round(obj.getFws())),
				labelList.get(20).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(21).getString("key"), String.valueOf(Math.round(obj.getOtherLoans())),
				labelList.get(21).getString("value"));
		resultList.add(dto);
		
		return resultList;
	}
	
	private FeesItems getTutionType(String state) {
		if(state.equals(getHomeState())) {
			return FeesItems.INSTATE_TUTION;
		}
		return FeesItems.OUTSTATE_TUTION;
	}
	
	@SuppressWarnings("unchecked")
	public List<ScholarshipInfo> processScholarshipBreakdown(CSRData obj,NPCObject npcObj) {
		List<ScholarshipInfo> scholarshipList=new ArrayList<ScholarshipInfo>();
		Map<Integer,List<KeyValue>> customers = Optional.ofNullable(obj.getChoiceInfo()!=null?obj.getChoiceInfo().getCustomers():null).orElse(new HashMap<Integer,List<KeyValue>>()); 
		for(Integer custid:customers.keySet()) {
			Customer customer=mongoTemplate.findOne(new Query(Criteria.where("custid").is(custid)), Customer.class);
			double coa = customer!=null ? customer.getCoa() : 0;
			if(customer!=null) {
				Map<String,Object> scholarship=processScholarshipBreakdown(custid,custid,coa,obj,npcObj);
				ScholarshipInfo info=new ScholarshipInfo();
				if(!CollectionUtils.isEmpty(scholarship)) {
					info.setId(custid);
					info.setName(customer.getName());
					info.setCoa(coa);
					info.setGrants(Double.valueOf((String) scholarship.get("Total Grants")));
					info.setGrants_breakdown((List<KeyValue>) scholarship.get("Total Grants Breakdown"));
					info.setOtherGrants(Double.valueOf((String) scholarship.get("Other Grants")));
					info.setOtherGrants_breakdown((List<KeyValue>) scholarship.get("Other Grants Breakdown"));
					info.setApplyURL(customer.getApplyURL());
					scholarshipList.add(info);
				}
			}
		}
		return scholarshipList;
	}
	
	/**
	 * For a specific college the student has selected , calculates the scholarships
	 * by creating a temporary map and adding the grants in it , this map is returned 
	 * to the calling function to map the results to the ScholarshipInfo object 
	 * @param custid the college for which scholarship breakdown has to be calculated
	 * @param campid campus id
	 * @param coa cost of attendance 
	 * @param obj the form values filled by user
	 * @param npcObj npc form values
	 * @return the map which contains all the grants and the sum of grants calculated ,
	 * this map is extracted to create the output structure in the calling function
	 */
	public Map<String,Object> processScholarshipBreakdown(int custid, int campid, double coa,CSRData obj,NPCObject npcObj) {
		boolean grantsApplicable = utils.ansToBoolean(Optional.ofNullable(obj.getBasicInfo().getFederalFinancialAid()).orElse("NO"));
		//it contains the entries that are displayed
		Map<String,Object> resultMap=new HashMap<String,Object>();
		if(grantsApplicable) {
			addTotalGrants(custid,campid,coa,obj,resultMap);
		}
		addOtherGrants(custid,campid,coa,obj,resultMap);
		return resultMap;
	}
	
	/**
	 * Adds total grants to the map passed in argument as key value pair
	 * also adds the components that sum up to the total grants to the map.
	 * @param custid college the student selected
	 * @param campid campus id of the college
	 * @param coa cost of attendance of the college
	 * @param obj the input data of the student 
	 * @param resultMap the map which contains the final result values
	 */
	private void addTotalGrants(int custid,int campid,double coa,CSRData obj,Map<String,Object> resultMap) {
		double merit=0.0,totalGrants=0.0;
		List<KeyValue> resultList=new ArrayList<KeyValue>();
		
		merit = calculateMerit(custid, campid, obj);
		resultList.add(new KeyValue("Merit Grant",String.valueOf(merit)));
		
		List<Long> selectedList=new ArrayList<Long>();
		ChoiceInformation choiceInfo=obj.getChoiceInfo();
		if (choiceInfo != null) {
			//get the list of all affinity questions for the college
			List<KeyValue> questions = choiceInfo.getCustomers().get(custid);
			for (KeyValue question : questions) {
				Integer questionId = Integer.valueOf(question.getKey());
				boolean answer = utils.ansToBoolean(question.getValue());
				//if user has selected the question as true  ,
				//the award will be used in the calculation of scholarship  
				if (answer) {
					Question doc = questionRepository.findById(Long.valueOf(questionId)).orElse(null);
					if(doc != null) {
						//Question doc = questionRepository.findByTextid(questionId);
						selectedList.add(Long.valueOf(doc.getTextid()));
						//if award amount is not zero , it will be displayed on screen
						//so add it to result stack(display)	
						if(!ObjectUtils.isEmpty(doc.getLabel()) && !ObjectUtils.isEmpty(doc.getAwardAmount())) {
							resultList.add(new KeyValue(doc.getLabel(),String.valueOf(doc.getAwardAmount())));
						}
					}
				}
			}
		}
		resultMap.put("Total Grants Breakdown",resultList);
		
		totalGrants=calculateTotalGrants(custid,selectedList,merit,coa);
		resultMap.put("Total Grants",String.valueOf(totalGrants));
	}
	
	/**
	 * Adds other grants to the map passed in argument as key value pair
	 * also adds the components that sum up to the other grants to the map.
	 * @param custid college the student selected
	 * @param campid campus id of the college
	 * @param coa cost of attendance of the college
	 * @param obj the input data of the student 
	 * @param resultMap the map which contains the final result values
	 */
	private void addOtherGrants(int custid,int campid,double coa,CSRData obj,Map<String,Object> resultMap) {
		double otherGrants=0.0, pellGrant = 0.0;
		List<KeyValue> resultList=new ArrayList<KeyValue>();
		
		BasicInformation basicInfo=obj.getBasicInfo();
		pellGrant = calculatePellGrant(PellAwardTypes.FULL_TIME,coa, basicInfo.getEfc());
		resultList.add(new KeyValue("Pell Grant",String.valueOf(pellGrant)));
		resultMap.put("Other Grants Breakdown",resultList);
		
		otherGrants+=pellGrant;
		resultMap.put("Other Grants",String.valueOf(otherGrants));
	}
	
	/**
	 * This function will calculate merit for the college provided in arguments
	 * arguments are a variable list since we don't know in future what all values 
	 * will be required for merit grant for any new college added.There is a
	 * seperate class which is used to calculate the logic for each college.
	 * Based on the customerid of the college that function will be get from the map
	 * defined in the class , there is a default method if specific method is not there
	 * with id=-1
	 * @return merit value calculated for the college
	 */
	private double calculateMerit(Object... vars) {
		Integer custid=(Integer)vars[0];
		Function<Object[], Double> meritFunction = Optional.ofNullable(meritMethodProvider.getMethodMap().get(custid))
				.orElse(meritMethodProvider.getMethodMap().get(-1));
		if (meritFunction != null)
			return meritFunction.apply(vars);
		return 0d;
	}
	
	/**
	 * This method is used to calculate totalgrants for a college,
	 * total grants may be calculated by the rules defined in CustGrantRule collection
	 * or the sum of merit grants and affinity questions selected by the customer
	 * @param custid customer id for the college
	 * @param merit merit grant for the particular college
	 * @param selectedList The list of affinity questions that are selected by the user
	 * @return double total grants for the particular college
	 */
	private Double calculateTotalGrants(int custid,final List<Long> selectedList,Double merit,Double coa) {
		Double totalGrants=0d;
		CustGrantRule rule=mongoTemplate.findOne(new Query(Criteria.where("custid").is(custid)), CustGrantRule.class,"cust_grant_rule");
		//case 1: There is a rule defined for calculating total grants in CustGrantRule collection
		if(rule!=null) {
			List<Long> maxQuesIds=Optional.ofNullable(rule.getQues_maxRule()).orElse(new ArrayList<Long>()).stream().filter( o -> {
				return selectedList.contains(Long.valueOf(o));
			}).collect(Collectors.toList());
			List<Long> minQuesIds=Optional.ofNullable(rule.getQues_minRule()).orElse(new ArrayList<Long>()).stream().filter( o -> {
				return selectedList.contains(Long.valueOf(o));
			}).collect(Collectors.toList());
			List<Long> otherQuesIds=Optional.ofNullable(rule.getQues_otherRule()).orElse(new ArrayList<Long>()).stream().filter( o -> {
				return selectedList.contains(Long.valueOf(o));
			}).collect(Collectors.toList());
			Boolean meritIncluded=rule.isMeritMandatory();
			
			List<Question> maxQuestions=new ArrayList<Question>();
			questionRepository.findAllByTextids(maxQuesIds).forEach(maxQuestions::add);
			List<Question> minQuestions=new ArrayList<Question>();
			questionRepository.findAllByTextids(minQuesIds).forEach(minQuestions::add);
			List<Question> otherQuestions=new ArrayList<Question>();
			questionRepository.findAllByTextids(otherQuesIds).forEach(otherQuestions::add);
			
			Double maxAmount=0d;
			Double minAmount=0d;
			Double tempValue=0d;
			if(!CollectionUtils.isEmpty(maxQuestions) || meritIncluded) {
				if(!CollectionUtils.isEmpty(maxQuestions)) {
					//find the maximum amount from the affinity questions or the merit grant
					for(Question question : maxQuestions) {
						if(question.getAwardAmount()>maxAmount)maxAmount=question.getAwardAmount();
					}
					tempValue=maxAmount;
				}
				if(meritIncluded && (merit>maxAmount)) {
					tempValue=merit;
				}
				totalGrants+=tempValue;
			}
			
			
			if(!CollectionUtils.isEmpty(minQuestions) || meritIncluded) {
				tempValue=0d;
				if(!CollectionUtils.isEmpty(minQuestions)) {
					minAmount=Double.MAX_VALUE;
					//find the minimum amount from the affinity questions or the merit grant
					for(Question question : minQuestions) {
						if(question.getAwardAmount()<minAmount)minAmount=question.getAwardAmount();
					}
					tempValue=minAmount;
				}
				if(meritIncluded && (merit<minAmount)) {
					tempValue=merit;
				}
				totalGrants+=tempValue;
			}
			
			if(!CollectionUtils.isEmpty(otherQuestions)) {
				for(Question question : otherQuestions) {
					totalGrants+=question.getAwardAmount();
				}
			}
			if(!meritIncluded) {
				totalGrants+=merit;
			}
		}else {
			//Case 2: Total grants is the sum of merit grant and affinity awards
			List<Question> questions=new ArrayList<Question>();
			questionRepository.findAllByTextids(selectedList).forEach(questions::add);
			for(Question question:questions) {
				totalGrants+=question.getAwardAmount();
			}
			totalGrants+=merit;
		}
		totalGrants=Math.min(coa,totalGrants);
		return totalGrants;
	}
	
	public ResultObject processResult(int custid, int campid, CSRData obj, NPCObject npcObj) {
		double coa = 0.0, merit = 0.0, pellGrant = 0.0, fseog = 0.0, tutionFee = 0.0, totalGrants = 0.0,
				stateGrant = 0.0, needAid = 0.0, dsl = 0.0, dusl = 0.0, fws = 0.0, otherLoans = 0.0, unmet_cost = 0.0,
				addtnFinancialAid = 0.0, netPrice = 0.0, monthlyPayment = 0.0,score=0.0;
		ResultObject result = new ResultObject();
		boolean grantsApplicable = utils.ansToBoolean(obj.getBasicInfo().getFederalFinancialAid());
		HousingOptions housingOption = Optional.ofNullable(npcObj.getOthers()).orElseGet(() -> { return new OtherInformation(); }).getHousingOption();
		List<FeesObject> feeitems=null;
		Map<String, Double> otherAwardsMap = new HashMap<String, Double>();
		if(grantsApplicable) {
			FeesItems tutionType=getTutionType(obj.getBasicInfo().getState());
			feeitems=calculateFeeItems(custid,campid,housingOption,tutionType);
			result.setFeesItems(calculateFeeItems(custid,campid,housingOption));
			coa = calculateCOA(result.getFeesItems());
			tutionFee=getItemFromFees(feeitems,tutionType);
			
			TestTypes testType=obj.getAddtnInfo().getScore_type();
			if(testType.equals(TestTypes.CLT)) {
				score=utils.clt2act(obj.getAddtnInfo().getScore(), 0.0);
			}else if(testType.equals(TestTypes.SAT)) {
				score=utils.sat2act(obj.getAddtnInfo().getScore(), 0.0);
			}else {
				score=obj.getAddtnInfo().getScore();
			}
			merit = calculateMerit(custid, campid, score, obj.getAddtnInfo().getGpa());
			pellGrant = calculatePellGrant(PellAwardTypes.FULL_TIME, coa, obj.getBasicInfo().getEfc());
			fseog = calculateFSEOG(custid,campid,pellGrant);
			stateGrant = calculateStateGrant(npcObj.getDemoGraphics().getScenario(), grantsApplicable,
					npcObj.getDemoGraphics().getAdmissionType(), npcObj.getDemoGraphics().getYears_completed(), tutionFee,
					pellGrant, npcObj.getParentsContribution(), obj.getBasicInfo().getEfc());
			needAid = calculateNeedAid(pellGrant, fseog, stateGrant);
			totalGrants += merit + needAid;
			ChoiceInformation choiceInfo = obj.getChoiceInfo();
			if (choiceInfo != null) {
				List<KeyValue> questions = choiceInfo.getCustomers().get(custid);
				for (KeyValue question : questions) {
					Integer questionId = Integer.valueOf(question.getKey());
					boolean answer = utils.ansToBoolean(question.getValue());
					if (answer) {
						Document doc = mongoTemplate.findOne(new Query(Criteria.where("_id").is(questionId)),
								Document.class,"questions");
						otherAwardsMap.put(question.getKey(), Double.valueOf(doc.get("awardAmount").toString()));
						totalGrants += Double.valueOf(doc.get("awardAmount").toString());
					}
				}
			}
			
			unmet_cost = coa - totalGrants;
			Map<String, Double> valueMap = calculateDSL_DUSL_FWS(unmet_cost, grantsApplicable,
					npcObj.getDemoGraphics().getScenario(), pellGrant);
			dsl = valueMap.get("dsl");
			dusl = valueMap.get("dusl");
			fws = valueMap.get("fws");
			otherLoans = valueMap.get("other_loans");
			addtnFinancialAid = calculateAddtnFinAid(fws, otherLoans);
			netPrice = calculateNetPrice(coa, totalGrants);
			monthlyPayment = calculateMonthlyPayment(netPrice);
		}
		
		result.setFeesItems(feeitems);
		result.setCoa(coa);
		result.setMerit(merit);
		result.setPellGrant(pellGrant);
		result.setFseog(fseog);
		result.setStateGrant(stateGrant);
		result.setNeedAid(needAid);
		result.setTotalGrants(totalGrants);
		result.setDsl(dsl);
		result.setDusl(dusl);
		result.setFws(fws);
		result.setOtherLoans(otherLoans);
		result.setAddtnFinancialAid(addtnFinancialAid);
		result.setNetPrice(netPrice);
		result.setMonthlyPayment(monthlyPayment);
		result.setOtherAwards(otherAwardsMap);

		return result;
	}
	
	private double calculateMonthlyPayment(double netPrice) {
		return netPrice/8;
	}

	private List<FeesObject> calculateFeeItems(int custid,int campid,HousingOptions housingOption,FeesItems tutionType) {
		List<FeesObject> feeItems = new ArrayList<FeesObject>();
		final FeesItems filterType=tutionType.equals(FeesItems.INSTATE_TUTION)?FeesItems.OUTSTATE_TUTION:FeesItems.INSTATE_TUTION;
		feeItems = feeRepository.findByCustidAndYearAndHousingOption(custid,  getAcademicYear(), housingOption);
		feeItems.stream().filter(o->o.getKey()!=filterType).collect(Collectors.toList());
		return feeItems;
	}
	
	public double getItemFromFees(List<FeesObject> items, FeesItems item) {
		for (FeesObject obj : items) {
			if (obj.getKey().equals(item)) {
				return obj.getValue();
			}
		}
		return 0d;
	}
	
	private List<FeesObject> calculateFeeItems(int custid,int campid,HousingOptions housingOption) {
		String year=getAcademicYear();
		List<FeesObject> list=feeRepository.findByCustidAndYearAndHousingOption(custid, year, housingOption);
		return list;
	}

	private double calculateNetPrice(double coa, double totalGrants) {
		if ((coa - totalGrants) >= 0) {
			return coa - totalGrants;
		}
		return 0d;		
	}
	
	private double calculateFSEOG(int custid,int campid,double pellGrant) {
		if(pellGrant>0) {
			return Double.valueOf((String) configProperties.get("fseog."+ custid + "." + campid));
		}
		return 0d;
	}
	
	public String getAcademicYear() {
		return (String) configProperties.get(CONSTANTS.BASE_YEAR_KEY);
	}

	public String getHomeState() {
		return (String) configProperties.get(CONSTANTS.HOME_STATE_KEY);
	}
	
	private double calculateAddtnFinAid(double fws,double other_loans) {
		return fws+other_loans;
	}
	
	public Map<String, Double> calculateDSL_DUSL_FWS(double unmet_cost, boolean us_flag, Scenario scenario,
			double pell_grant) {
		double fws = 0d, dsl = 0d, dusl = 0d, other_loans = 0d;
		if (us_flag) {
			if (unmet_cost < CONSTANTS.REMAINING_NEED_LIMIT_1) {
				dsl = unmet_cost;
				dusl = 0d;
			} else if ((unmet_cost >= CONSTANTS.REMAINING_NEED_LIMIT_1)
					&& (unmet_cost <= CONSTANTS.REMAINING_NEED_LIMIT_2)
					&& ((scenario.equals(Scenario.A)) || (scenario.equals(Scenario.B)))) {
				dsl = CONSTANTS.DSL_VALUE;
				dusl = unmet_cost - CONSTANTS.REMAINING_NEED_LIMIT_1;
			} else if ((unmet_cost > CONSTANTS.REMAINING_NEED_LIMIT_2)
					&& ((scenario.equals(Scenario.A)) || (scenario.equals(Scenario.B)))) {
				dsl = CONSTANTS.DSL_VALUE;
				dusl = CONSTANTS.DUSL_VALUE;
				if (pell_grant > 0) {
					if (unmet_cost <= CONSTANTS.REMAINING_NEED_LIMIT_2) {
						fws = unmet_cost - CONSTANTS.REMAINING_NEED_LIMIT_2;
					} else {
						fws = CONSTANTS.FWS_VALUE;
						other_loans = unmet_cost - dsl - dusl - fws;
					}
				} else {
					fws = 0d;
					other_loans = unmet_cost - dsl - dusl - fws;
				}
			} else if ((unmet_cost >= CONSTANTS.REMAINING_NEED_LIMIT_1)
					&& (unmet_cost <= CONSTANTS.REMAINING_NEED_LIMIT_2) && (scenario.equals("C"))) {
				dsl = CONSTANTS.DSL_VALUE;
				dusl = unmet_cost - CONSTANTS.REMAINING_NEED_LIMIT_1;
			} else {
				dsl = CONSTANTS.DSL_VALUE;
				dusl = CONSTANTS.DUSL_VALUE;
				if (pell_grant > 0) {
					if (unmet_cost <= CONSTANTS.REMAINING_NEED_LIMIT_3) {
						fws = unmet_cost - CONSTANTS.REMAINING_NEED_LIMIT_2;
					} else {
						fws = CONSTANTS.FWS_VALUE;
						other_loans = unmet_cost - dsl - dusl - fws;
					}
				} else {
					fws = 0d;
					other_loans = unmet_cost - dsl - dusl - fws;
				}
			}
		} else {
			dsl = 0d;
			dusl = 0d;
			fws = 0d;
			other_loans = unmet_cost;
		}
		Map<String, Double> resultMap = new HashMap<String, Double>();
		resultMap.put("dsl", dsl);
		resultMap.put("dusl", dusl);
		resultMap.put("fws", fws);
		resultMap.put("other_loans", other_loans);
		return resultMap;
	}
	
	private double calculateNeedAid(double pellGrant,double fseog,double stateGrant) {
		return pellGrant+fseog+stateGrant;
	}
	
	private double calculateFseog(double pellGrant) {
		if (pellGrant > 0) {
			return 2000d;
		}
		return 0d;
	}
	
	private double calculateCOA(List<FeesObject> feeItems) {
		return feeItems.stream().mapToDouble(o->o.getValue()).sum();
	}
	
	/**
	 * This function calculates pell grant based on the cost of attendance and efc values
	 * @param the award type 
	 * @param coa The cost of attendance for the college
	 * @param efc the expected family contribution for the student 
	 * @return The pell grant value calculated
	 */
	private Double calculatePellGrant(PellAwardTypes award_type, double coa, double efc) {
		Double pellGrantValue = 0d;
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("min").lte(coa), Criteria.where("max").gte(coa),
					Criteria.where("payment_schedule").is(award_type.toString()),
					Criteria.where("year").is(getAcademicYear())));
			Document doc = mongoTemplate.findOne(query, Document.class, "pell_grant");
			String field = null;
			if (doc != null) {
				for (String key : doc.keySet()) {
					if (key.contains("_") && !key.equals("payment_schedule") && !key.equals("_id")) {
						String spl[] = key.split("_");
						int min1 = Integer.parseInt(spl[0]);
						int max1 = Integer.parseInt(spl[1]);
						if (min1 <= efc && max1 >= efc) {
							field = key;
						}
					}
				}
			}
			if (field != null) {
				pellGrantValue = Double.valueOf(doc.get(field).toString()).doubleValue();
			}
		return pellGrantValue;
	}
	
	public static double calculateStateGrant(Scenario scenario,boolean us_flag,AdmissionTypes admission_type,int year_attended_count,double tution_fee,double pell_grant,double parents_contribution,double efc) {
		double annual_tuition_cap = Math.min(tution_fee, CONSTANTS.SCENARIO_A_TUTION_CAP);
		double miscellaneous_cap = CONSTANTS.SCENARIO_A_MISC_CAP;
		double state_budget = 0, mn_scholarship = 0;
		if (scenario.equals(Scenario.A) && us_flag
				&& (admission_type.equals(AdmissionTypes.FR) || admission_type.equals(AdmissionTypes.TR))
				&& year_attended_count <= 4) {
			state_budget = annual_tuition_cap + miscellaneous_cap;
			mn_scholarship = CONSTANTS.SCENARIO_A_STATE_BUDGET_MUL_FACTOR * state_budget
					- (pell_grant + CONSTANTS.SCENARIO_A_PARENTS_CONTRIB_MUL_FACTOR * parents_contribution);
			mn_scholarship = Math.max(mn_scholarship, 0);
		} else if (scenario.equals(Scenario.B) && us_flag
				&& (admission_type.equals(AdmissionTypes.FR) || admission_type.equals(AdmissionTypes.TR))
				&& year_attended_count <= 4) {
			annual_tuition_cap = Math.min(tution_fee, CONSTANTS.SCENARIO_B_TUTION_CAP);
			miscellaneous_cap = CONSTANTS.SCENARIO_B_MISC_CAP;
			state_budget = annual_tuition_cap + miscellaneous_cap;
			mn_scholarship = CONSTANTS.SCENARIO_B_STATE_BUDGET_MUL_FACTOR * state_budget
					- (pell_grant + CONSTANTS.SCENARIO_B_EFC_MUL_FACTOR * efc);
			mn_scholarship = Math.max(mn_scholarship, 0);
		} else if (scenario.equals(Scenario.C) && us_flag
				&& (admission_type.equals(AdmissionTypes.FR) || admission_type.equals(AdmissionTypes.TR))
				&& year_attended_count <= 4) {
			annual_tuition_cap = Math.min(tution_fee, CONSTANTS.SCENARIO_C_TUTION_CAP);
			miscellaneous_cap = CONSTANTS.SCENARIO_C_MISC_CAP;
			state_budget = annual_tuition_cap + miscellaneous_cap;
			mn_scholarship = CONSTANTS.SCENARIO_C_STATE_BUDGET_MUL_FACTOR * state_budget
					- (pell_grant + CONSTANTS.SCENARIO_C_MISC_CAP * efc);
			mn_scholarship = Math.max(mn_scholarship, 0);
		}
		return mn_scholarship;
	}
	
}