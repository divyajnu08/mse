package com.virtue.csr.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.virtue.csr.constants.CONSTANTS;
import com.virtue.csr.constants.EmploymentStatus;
import com.virtue.csr.constants.EnrollmentStatus;
import com.virtue.csr.constants.MaritalStatus;
import com.virtue.csr.controller.MainController;

@Service
public class LookupDataService {
	
	private static final Logger logger = LoggerFactory.getLogger(LookupDataService.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;	
	
	public double getIncomeProtectionAllowance(MaritalStatus maritalStatus, EnrollmentStatus enrollmentStatus) {
		logger.debug("Calling getIncomeProtectionAllowance");
		double income_protection_allowance = 0;
		if (maritalStatus == MaritalStatus.SINGLE
				|| maritalStatus == MaritalStatus.SEPARATED
				|| maritalStatus == MaritalStatus.DIVORCED) {
			income_protection_allowance = CONSTANTS.SCENARIO_B_IP_ALLOWANCE_UNMARRIED;

		}else if (maritalStatus == MaritalStatus.MARRIED) {
			if(ObjectUtils.isEmpty(enrollmentStatus)) {
				income_protection_allowance = 0;
			}else if( enrollmentStatus == EnrollmentStatus.MORE_THAN_HALF){
				income_protection_allowance = CONSTANTS.SCENARIO_B_IP_ALLOWANCE_MARRIED_GT_HALF;
			}else if( enrollmentStatus == EnrollmentStatus.LESS_THAN_HALF){
				income_protection_allowance = CONSTANTS.SCENARIO_B_IP_ALLOWANCE_MARRIED_LT_HALF;
			}
		}
		logger.debug("income protection allowance calculated" + income_protection_allowance);
		return income_protection_allowance;
	}
	
	public double getEmploymentExpenseAllowanceForA(MaritalStatus parents_marital_status,double parent1_income,double parent2_income) {
		logger.debug("Calling getEmploymentExpenseAllowanceForA");

		double employment_expense_allowance=0d;
		if ((parents_marital_status.equals(MaritalStatus.MARRIED))
				|| (parents_marital_status.equals(MaritalStatus.UNMARRIED_LIVING_TOGETHER))) {
			if ((parent1_income != 0) && (parent2_income != 0)) {
				if (parent1_income < parent2_income) {
					employment_expense_allowance = (parent1_income * CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_MUL_FACTOR) / 100;
					if (employment_expense_allowance > CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT)
						employment_expense_allowance = CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT;
				} else {
					employment_expense_allowance = (parent2_income * CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_MUL_FACTOR) / 100;
					if (employment_expense_allowance > CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT)
						employment_expense_allowance = CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT;
				}
			} else
				employment_expense_allowance = 0;
		} else {
			employment_expense_allowance = (parent1_income * CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_MUL_FACTOR) / 100;
			if (employment_expense_allowance > CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT)
				employment_expense_allowance = CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT;
		}
		
		logger.debug("employment expense allowance calculated" + employment_expense_allowance);
		return employment_expense_allowance;
	}
	
	public double getEmploymentExpenseAllowanceForB(double student_income, double spouse_income, MaritalStatus student_marital_status,EmploymentStatus student_employment_status) {
		logger.debug("Calling getEmploymentExpenseAllowanceForB");

		double employment_expense_allowance = 0d;
		
		if (student_marital_status.equals(MaritalStatus.MARRIED)) {
			if (student_employment_status.equals(EmploymentStatus.BOTH)) {
				if (student_income < spouse_income) {
					employment_expense_allowance = (student_income * CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_MUL_FACTOR) / 100;
					if (employment_expense_allowance > CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT)
						employment_expense_allowance = CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT;
				} else {
					employment_expense_allowance = (spouse_income * CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_MUL_FACTOR) / 100;
					if (employment_expense_allowance > CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT)
						employment_expense_allowance = CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT;
				}
			} else
				employment_expense_allowance = 0;
		} else {
			employment_expense_allowance = 0;
		}
		logger.debug("calculated employment_expense_allowance :" + employment_expense_allowance);
		return employment_expense_allowance;
	}
	
	public double getEmploymentExpenseAllowanceForC(double student_income, double spouse_income, MaritalStatus student_marital_status,EmploymentStatus student_employment_status) {
		logger.debug("Calling getEmploymentExpenseAllowanceForC");

		double employment_expense_allowance = 0d;
		
		if (student_marital_status.equals(MaritalStatus.MARRIED)) {
			if (student_employment_status.equals(EmploymentStatus.BOTH)) {
				if (student_income < spouse_income) {
					employment_expense_allowance = (student_income * CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_MUL_FACTOR) / 100;
					if (employment_expense_allowance > CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT)
						employment_expense_allowance = CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT;
				} else {
					employment_expense_allowance = (spouse_income * CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_MUL_FACTOR) / 100;
					if (employment_expense_allowance > CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT)
						employment_expense_allowance = CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT;
				}
			} else
				employment_expense_allowance = 0;
		} else {
			employment_expense_allowance = (student_income * CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_MUL_FACTOR) / 100;
			if (employment_expense_allowance > CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT)
				employment_expense_allowance = CONSTANTS.EMPLOYMENT_EXPENSE_ALLOWANCE_LOWER_LIMIT;
		}
		logger.debug("calculated employment_expense_allowance :" + employment_expense_allowance);
		return employment_expense_allowance;
	}
	
	public double getB4AssetProtectionAllowance(MaritalStatus marital_status,int age, String year) {
		logger.debug("Calling getB4AssetProtectionAllowance Input arguments");

		double asset_protection_allowance = 0.0;
		String tab_name = "b4_assest_protection_allowance";
		Query findQuery = new Query();
		findQuery.addCriteria(
				new Criteria().andOperator(
				Criteria.where("year").is(year),
				Criteria.where("older_person_age").is(age)));
		Document result = mongoTemplate.findOne(findQuery, Document.class, tab_name);
		if(!ObjectUtils.isEmpty(result)) {
			if(marital_status.equals(MaritalStatus.MARRIED)) {
				asset_protection_allowance = Double.valueOf(result.get("married_student_allowance").toString());
			}else {
				asset_protection_allowance = Double.valueOf(result.get("unmarried_student_allowance").toString());
			}
		}
		logger.debug("calculated asset_protection_allowance :" + asset_protection_allowance);
		return asset_protection_allowance;
	}
	
	public int getNoInCollge(EnrollmentStatus enStatus) {
		logger.debug("Calling getNoInCollge");

		int no_in_college = CONSTANTS.ENROLLEMNT_LT_HALF_NO_IN_COLLEGE;
		if(!ObjectUtils.isEmpty(enStatus) && enStatus == EnrollmentStatus.MORE_THAN_HALF)
			no_in_college = CONSTANTS.ENROLLEMNT_GT_HALF_NO_IN_COLLEGE;
		
		logger.debug("calculated no In college" + no_in_college);
		return no_in_college;
	}	
	
	public double getB2SocialSecurityTax(double income, String year) {
		logger.debug("Calling getB2SocialSecurityTax");
		
		double security_tax_amount = 0;
		Query findQuery = new Query();
		findQuery.addCriteria(
				new Criteria().andOperator(
				Criteria.where("year").is(year),
				Criteria.where("work_income_slab_min").lte(income),
				Criteria.where("work_income_slab_max").gte(income)
				));
		Document result = mongoTemplate.findOne(findQuery, Document.class, "b2_social_security_tax");
		if(!ObjectUtils.isEmpty(result)) {
			String operator = result.getString("operator");
			int diff = result.getInteger("difference");
			double percent = result.getDouble("social_security_tax_percent");
			double base = result.getDouble("base_value");
			if (operator.equals("+"))
				security_tax_amount = (((income - diff) * (percent)) / 100)	+ base;
		}
		logger.debug("calculated security_tax_amount" + security_tax_amount);
		return security_tax_amount;
	}
	
	public double getB3AdjustedBusinessNetworth(double businessNetworth, String year) {
		logger.debug("Calling getB3AdjustedBusinessNetworth");

		double adjusted_net_worth_of_business = 0;
		Query findQuery = new Query();
		findQuery.addCriteria(
				new Criteria().andOperator(
				Criteria.where("year").is(year),
				Criteria.where("business_net_worth_slab_min").lte(businessNetworth),
				Criteria.where("business_net_worth_slab_max").gte(businessNetworth)
				));
		Document result = mongoTemplate.findOne(findQuery, Document.class, "b3_business_or_farm");
		if(!ObjectUtils.isEmpty(result)) {
			String operator = result.getString("operator");
			int diff = result.getInteger("difference");
			double base_value = result.getInteger("base_value");
			int adjusted_net_worth_percent = result.getInteger("adjusted_net_worth_percent");
			if (operator.equals("+"))
				adjusted_net_worth_of_business = (((businessNetworth - diff) * (adjusted_net_worth_percent)) / 100)	+ base_value;
		}
		logger.debug("calculated adjusted_net_worth_of_business :" + adjusted_net_worth_of_business);
		return adjusted_net_worth_of_business;
	}
	
	public double getB1StateAndOtherTax(String state, String year) {
		logger.debug("Calling getB1StateAndOtherTax");
		
		double tax_percent = 0;
		Query findQuery = new Query();
		findQuery.addCriteria(
				new Criteria().andOperator(
				Criteria.where("year").is(year),
				Criteria.where("state").is(state)
				));
		findQuery.fields().include("total_income_percent");		
		Document result = mongoTemplate.findOne(findQuery, Document.class, "b1_state_and_other_tax_allowance");
		if(!ObjectUtils.isEmpty(result))
			tax_percent = Double.valueOf(result.get("total_income_percent").toString());
		logger.debug("calculated tax percent :" + tax_percent);
		return tax_percent;
	}
	
	public Double getA1StateTaxForParents(String parentsStateOfResidency,String year,final double total_income) {
		logger.debug("Calling getA1StateTaxForParents");

		String state_tab_name = "a1_parents_state_and_other_tax_allowance";
		double tax_percent=0d,percentage_amount=0d;
		List<Document> docList = mongoTemplate.find(new Query(Criteria.where("year").is(year).and("state").is(parentsStateOfResidency)), Document.class,state_tab_name);
		Optional<Document> tax_percent_doc = docList.stream().filter( o -> {
			double income_slab_min = Double.valueOf(o.getInteger("income_slab_min"));
			double income_slab_max = Double.valueOf(o.getInteger("income_slab_max"));
			if ((total_income >= income_slab_min)
					&& (total_income <= income_slab_max)) {
				return true;
			}
			return false;
		}).findFirst();
		if(tax_percent_doc.isPresent()) {
			tax_percent = Double.valueOf(tax_percent_doc.get().getInteger("total_income_percent"));
		}
		if (tax_percent != 0)
			percentage_amount = (total_income * tax_percent) / 100;
		else
			percentage_amount = 0;
		logger.debug("Calculated percentage amount :" + percentage_amount);
		return percentage_amount;
	}
	
	public Double getA2SocialSecurityTax(String year,final Double parent1_income) {
		logger.debug("Calling getA2SocialSecurityTax");

		String social_security_tab = "a2_social_security_tax";
		double security_tax_amount = 0d;
		List<Document> docList = mongoTemplate.find(new Query(Criteria.where("year").is(year)), Document.class,social_security_tab);
		List<Document> result = new ArrayList<Document>();
		for( Document o:docList) {
			double work_income_slab_min = Double.valueOf(o.get("work_income_slab_min").toString());
			double work_income_slab_max = Double.valueOf(o.get("work_income_slab_max").toString());
			
			if ((parent1_income != null) && (parent1_income >= work_income_slab_min)
					&& (parent1_income <= work_income_slab_max))  {
				result.add(o);
			}
		}
		
		if(!CollectionUtils.isEmpty(result)){
			double diff = Double.valueOf(result.get(0).get("difference").toString());
			double percent = Double.valueOf(result.get(0).get("social_security_tax_percent").toString());
			double base = Double.valueOf(result.get(0).get("base_value").toString());
			String operator = result.get(0).getString("operator");
			if (operator.equals("+"))
				security_tax_amount = (((parent1_income - diff) * (percent)) / 100)
						+ base;
		}
		logger.debug("calculated security tax amount: " + security_tax_amount);
		return security_tax_amount;
	}
	
	public Double getA3IncomeProtectionAllowance(int total_count, int stud_count, String academicYear) {
		logger.debug("Calling getA3IncomeProtectionAllowance");

		double income_protection_allowance = 0;	
		String tab = "a3_income_protection_allowance";
		Query findQuery = new Query();
		findQuery.addCriteria(new Criteria().andOperator(Criteria.where("year").is(academicYear),
				Criteria.where("no_of_people").is(total_count), Criteria.where("no_of_students").is(stud_count),
				Criteria.where("year").is(academicYear)));
		Document doc = mongoTemplate.findOne(findQuery, Document.class, "a3_income_protection_allowance");
		if(doc==null) {
			
			Criteria criteria = Criteria.where("year").is(academicYear);
			List<Document> list = mongoTemplate.find(new Query(criteria),Document.class,"a3_income_protection_allowance");
			
			int max_no_of_people = 0;
			Optional<Integer> optn_max_no_of_people = list.stream().map(o -> o.getInteger("no_of_people"))
					.max(Comparator.comparing(Integer::valueOf));
			if(optn_max_no_of_people.isPresent()) {
				max_no_of_people = optn_max_no_of_people.get();
			}
			
			int max_no_of_students = 0;
			Optional<Integer> optn_max_no_of_students = list.stream().map(o -> o.getInteger("no_of_students"))
					.max(Comparator.comparing(Integer::valueOf));
			if(optn_max_no_of_students.isPresent()) {
				max_no_of_students=optn_max_no_of_students.get();
			}
			
			double additional_student_amt = 0; 
			Optional<Double> optn_additional_student_amt = list.stream().map(o -> Double.valueOf(o.get("additional_student").toString()))
					.max(Comparator.comparing(Double::valueOf));
			if(optn_additional_student_amt.isPresent()) {
				additional_student_amt = optn_additional_student_amt.get();
			}
			
			
			double additional_family_member_amt = 0;
			Optional<Double> optn_additional_family_member_amt = list.stream().map(o -> Double.valueOf(o.get("additional_family_member").toString()))
					.max(Comparator.comparing(Double::valueOf));
			if(optn_additional_family_member_amt.isPresent()) {
				additional_family_member_amt = optn_additional_family_member_amt.get();
			}
		
			int extra_people, extra_student;
			Optional<Integer> max_people=list.stream().map(o -> o.getInteger("no_of_people")).max(Comparator.comparing(Integer::valueOf));
			Document finalRes = null;
		
			if (total_count > stud_count) {
				if ((total_count > max_no_of_people) && (stud_count > max_no_of_students)) {
					// con = scenario_b.getConnection();
					extra_people = (int) (total_count - max_no_of_people);
					extra_student = (int) (stud_count - max_no_of_students);
					List<Integer> mapped2=null;
					finalRes = mongoTemplate.findOne(
							new Query(new Criteria().andOperator(criteria,
									new Criteria().where("no_of_people").is(max_no_of_people),
									new Criteria().where("no_of_students").is(max_no_of_students))),
							Document.class, "a3_income_protection_allowance");
					double ip_allowance = 0;
					if(finalRes!=null)
						ip_allowance = Double.valueOf(finalRes.get("ip_allowance").toString());
					income_protection_allowance = (ip_allowance + (extra_people * additional_family_member_amt))
							- (extra_student * additional_student_amt);
				} else if ((total_count > max_no_of_people) && (stud_count <= max_no_of_students)) {
					extra_people = (int) (total_count - max_no_of_people);
					extra_student = (int) (stud_count - max_no_of_students);
					finalRes = mongoTemplate.findOne(
							new Query(new Criteria().andOperator(criteria,
									Criteria.where("no_of_people").is(max_no_of_people),
									Criteria.where("no_of_students").is(stud_count))),
							Document.class, "a3_income_protection_allowance");
					double ip_allowance = 0;
					if(finalRes!=null)
						ip_allowance = Double.valueOf(finalRes.get("ip_allowance").toString());
					income_protection_allowance = ip_allowance + (extra_people * additional_family_member_amt);
				}
			} else {
				income_protection_allowance = 0;
			}
		} else {
			income_protection_allowance = Double.valueOf(doc.get("ip_allowance").toString());
		}
		logger.debug("calculated income protection allowance :" + income_protection_allowance);
		return income_protection_allowance;
	}
	
	public Double getA4AdjustedNetworthOfBusiness(double networth_of_parents_own_job,String year) {
		logger.debug("Calling getA4AdjustedNetworthOfBusiness");

		String networth_adjustment_tab = "a4_parents_business_or_farm";
		double adjusted_net_worth_of_business = 0;
		Query findQuery = new Query();
		Criteria criteria = Criteria.where("year").is(year);
		List<Document> docList = mongoTemplate.find(findQuery, Document.class, "a4_parents_business_or_farm");
		String operator;
		int base_value, difference, adjusted_net_worth_percent;
		for(Document doc:docList) {
			int business_net_worth_slab_min = doc
					.getInteger("business_net_worth_slab_min");
			int business_net_worth_slab_max = doc
					.getInteger("business_net_worth_slab_max");
				if ((networth_of_parents_own_job >= business_net_worth_slab_min)
						&& (networth_of_parents_own_job <= business_net_worth_slab_max)) {
					base_value = doc.getInteger("base_value");
					operator = doc.getString("operator");
					difference = doc.getInteger("difference");
					adjusted_net_worth_percent = doc.getInteger("adjusted_net_worth_percent");
					if (operator.equals("+"))
						adjusted_net_worth_of_business = (((networth_of_parents_own_job - difference) * (adjusted_net_worth_percent)) / 100)
								+ base_value;
				}
		}
		logger.debug("calculated adjusted net worth business :" + adjusted_net_worth_of_business);
		return adjusted_net_worth_of_business;
	}
	
	public double getA5AssetProtectionAllowance(int parent_age, MaritalStatus maritalStats, String academic_year) {
		logger.debug("Calling getA5AssetProtectionAllowance");

		Query findQuery = new Query();
		double asset_protection_allowance = 0.0;
		findQuery.addCriteria(
				new Criteria().andOperator(
						Criteria.where("year").is(academic_year),
						Criteria.where("older_person_age").is(parent_age)
						));
		
		Document doc = mongoTemplate.findOne(findQuery, Document.class, "a5_assest_protection_allowance");
		if(doc!=null) {
			if(maritalStats == MaritalStatus.MARRIED || maritalStats == MaritalStatus.UNMARRIED_LIVING_TOGETHER)
				asset_protection_allowance = Double.valueOf(doc.get("two_parent_allowance").toString());			
			else
				asset_protection_allowance = Double.valueOf(doc.get("one_parent_allowance").toString());
		}
		logger.debug("calculated asset protection allowance :" + asset_protection_allowance);
		return asset_protection_allowance;
	}
	
	
	public double getA6ContributionFromAAI(double adjusted_available_income, String academicYear) {
		logger.debug("Calling getA6ContributionFromAAI");
		
		double adjusted_net_worth_of_business = 0;
		Query query = new Query();
		query.addCriteria(Criteria.where("year").is(academicYear));
		List<Document> docList = mongoTemplate.find(query, Document.class, "a6_parents_contribution_from_aai");
		String operator;
		double base_value, difference, contribution_from_aai;
		if(docList!=null) {
			for(Document doc:docList) {
			double aai_slab_min = Double.valueOf(doc.get("aai_slab_min").toString());
			double aai_slab_max = Double.valueOf(doc.get("aai_slab_max").toString());
			if ((adjusted_available_income >= aai_slab_min)
					&& (adjusted_available_income <= aai_slab_max)) {
				base_value = Double.valueOf(doc.get("base_value").toString());
				operator = doc.getString("operator");
				difference = Double.valueOf(doc.get("difference").toString());
				contribution_from_aai = Double.valueOf(doc.get("contribution_from_aai").toString());
				if (operator.equals("+"))
					adjusted_net_worth_of_business = (((adjusted_available_income - difference) * (contribution_from_aai)) / 100)
							+ base_value;
				}
			}
		}
		logger.debug("calculated percentage amount :" + adjusted_net_worth_of_business);
		return adjusted_net_worth_of_business;
	}
	
	public double getA7StateTaxForStudent(String state, double total_income, String year) {
		logger.debug("Calling getA7StateTaxForStudent");

		int tax_percent = 0;
		double percentage_amount = 0;
		Query findQuery = new Query();
		findQuery.addCriteria(
				new Criteria().andOperator(
				Criteria.where("year").is(year),
				Criteria.where("state").is(state)
				));
		Document result = mongoTemplate.findOne(findQuery, Document.class, "a7_students_state_and_other_tax_allowance");
		if(!ObjectUtils.isEmpty(result)) {
			tax_percent = result.getInteger("total_income_percent");
			percentage_amount = (total_income * tax_percent) / 100;
		}
		logger.debug("calculated percentage amount :" + percentage_amount);
		return percentage_amount;
	}
	
	public double getC1StateAndOtherTaxAllowance(String state, double income, String year) {
		logger.debug("Calling getC1StateAndOtherTaxAllowance");

		int tax_percent = 0;
		double percentage_amount = 0;
		Query findQuery = new Query();
		findQuery.addCriteria(
				new Criteria().andOperator(
				Criteria.where("year").is(year),
				Criteria.where("state").is(state),
				Criteria.where("income_slab_min").lte(income),
				Criteria.where("income_slab_max").gte(income)
				));
		Document result = mongoTemplate.findOne(findQuery, Document.class, "c1_state_and_other_tax_allowance");
		if(!ObjectUtils.isEmpty(result)) {
			tax_percent = result.getInteger("total_income_percent");
			percentage_amount = (income * tax_percent) / 100;
		}
		logger.debug("calculated percentage amount :" + percentage_amount);
		return percentage_amount;
	}
	
	public double getC2SocialSecurityTax(double income, String year) {
		logger.debug("Calling getC2SocialSecurityTax");

		double security_tax_amount = 0.0;
		Query findQuery = new Query();
		findQuery.addCriteria(
				new Criteria().andOperator(
				Criteria.where("year").is(year),
				Criteria.where("work_income_slab_min").lte(income),
				Criteria.where("work_income_slab_max").gte(income)
				));
		Document result = mongoTemplate.findOne(findQuery, Document.class, "c2_social_security_tax");
		if(!ObjectUtils.isEmpty(result)) {
			double diff = Double.valueOf(result.get("difference").toString());
			double percent = Double.valueOf(result.get("social_security_tax_percent").toString());
			double base = Double.valueOf(result.get("base_value").toString());
			String operator = result.getString("operator");
			if (operator.equals("+"))
				security_tax_amount = (((income - diff) * (percent)) / 100)
						+ base;
		}
		logger.debug("calculated scurity tax amount :" + security_tax_amount);
		return security_tax_amount;
	}
	
	
	public double getC3IncomeProtectionAllowance(int no_of_people, int no_of_students, String year) {
		logger.debug("Calling getC3IncomeProtectionAllowance");

		int max_no_of_people = 0;
		int max_no_of_students = 0;
		int max_additional_student = 0;
		int max_additional_family_member = 0;
		double income_protection_allowance = 0;

		Query findQuery = new Query();
		findQuery.addCriteria(new Criteria().andOperator(Criteria.where("year").is(year),
				Criteria.where("no_of_people").is(no_of_people), Criteria.where("no_of_students").is(no_of_students)));
		Document result = mongoTemplate.findOne(findQuery, Document.class, "c3_income_protection_allowance");
		if (!ObjectUtils.isEmpty(result)) {
			income_protection_allowance = Double.valueOf(result.get("ip_allowance").toString());
		} else {

			Query maxQuery1 = new Query();
			maxQuery1.addCriteria(new Criteria().andOperator(Criteria.where("year").is(year)));
			maxQuery1.fields().include("no_of_people");
			maxQuery1.with(Sort.by(Sort.Direction.DESC, "no_of_people"));
			maxQuery1.limit(1);

			result = mongoTemplate.findOne(maxQuery1, Document.class, "c3_income_protection_allowance");
			if (!ObjectUtils.isEmpty(result)) {
				max_no_of_people = result.getInteger("no_of_people");
			}

			Query maxQuery2 = new Query();
			maxQuery2.addCriteria(new Criteria().andOperator(Criteria.where("year").is(year)));
			maxQuery2.fields().include("no_of_students");
			maxQuery2.with(Sort.by(Sort.Direction.DESC, "no_of_students"));
			maxQuery2.limit(1);

			result = mongoTemplate.findOne(maxQuery2, Document.class, "c3_income_protection_allowance");
			if (!ObjectUtils.isEmpty(result)) {
				max_no_of_students = result.getInteger("no_of_students");
			}

			Query maxQuery3 = new Query();
			maxQuery3.addCriteria(new Criteria().andOperator(Criteria.where("year").is(year)));
			maxQuery3.fields().include("additional_student");
			maxQuery3.with(Sort.by(Sort.Direction.DESC, "additional_student"));
			maxQuery3.limit(1);

			result = mongoTemplate.findOne(maxQuery3, Document.class, "c3_income_protection_allowance");
			if (!ObjectUtils.isEmpty(result)) {
				max_additional_student = result.getInteger("additional_student");
			}

			Query maxQuery4 = new Query();
			maxQuery4.addCriteria(new Criteria().andOperator(Criteria.where("year").is(year)));
			maxQuery4.fields().include("additional_family_member");
			maxQuery4.with(Sort.by(Sort.Direction.DESC, "additional_family_member"));
			maxQuery4.limit(1);

			result = mongoTemplate.findOne(maxQuery4, Document.class, "c3_income_protection_allowance");
			if (!ObjectUtils.isEmpty(result)) {
				max_additional_family_member = result.getInteger("additional_family_member");
			}

			int extra_people = no_of_people - max_no_of_people;
			int extra_student = no_of_students - max_no_of_students;

			if (no_of_people > no_of_students) {
				if ((no_of_people > max_no_of_people) && (no_of_students > max_no_of_students)) {
					findQuery = new Query();
					findQuery.addCriteria(new Criteria().andOperator(Criteria.where("year").is(year),
							Criteria.where("no_of_people").is(max_no_of_people),
							Criteria.where("no_of_students").is(max_no_of_students)));
					findQuery.fields().include("ip_allowance");
					result = mongoTemplate.findOne(findQuery, Document.class, "c3_income_protection_allowance");
					double ip_allowance = 0;
					if (!ObjectUtils.isEmpty(result)) {
						ip_allowance = Double.valueOf(result.get("ip_allowance").toString());
					}
					income_protection_allowance = (ip_allowance + (extra_people * max_additional_family_member))
							- (extra_student * max_additional_student);

				} else if ((no_of_people > max_no_of_people) && (no_of_students <= max_no_of_students)) {
					findQuery = new Query();
					findQuery.addCriteria(new Criteria().andOperator(Criteria.where("year").is(year),
							Criteria.where("no_of_people").is(max_no_of_people),
							Criteria.where("no_of_students").is(no_of_students)));
					findQuery.fields().include("ip_allowance");
					result = mongoTemplate.findOne(findQuery, Document.class, "c3_income_protection_allowance");
					double ip_allowance = 0;
					if (!ObjectUtils.isEmpty(result)) {
						ip_allowance = Double.valueOf(result.get("ip_allowance").toString());
					}
					income_protection_allowance = ip_allowance + (extra_people * max_additional_family_member);
				}
			}
		}
		logger.debug("calculated income_protection_allowance :" + income_protection_allowance);
		return income_protection_allowance;
	}
	
	public double getC4AdjustedNetworthOfBusiness(double businessNetworth, String year) {
		logger.debug("Calling getC4AdjustedNetworthOfBusiness");

		double adjusted_net_worth_of_business = 0;
		Query findQuery = new Query();
		findQuery.addCriteria(
				new Criteria().andOperator(
				Criteria.where("year").is(year),
				Criteria.where("business_net_worth_slab_min").lte(businessNetworth),
				Criteria.where("business_net_worth_slab_max").gte(businessNetworth)
				));
		Document result = mongoTemplate.findOne(findQuery, Document.class, "b3_business_or_farm");
		if(!ObjectUtils.isEmpty(result)) {
			String operator = result.getString("operator");
			int diff = result.getInteger("difference");
			double base_value = result.getInteger("base_value");
			int adjusted_net_worth_percent = result.getInteger("adjusted_net_worth_percent");
			if (operator.equals("+"))
				adjusted_net_worth_of_business = (((businessNetworth - diff) * (adjusted_net_worth_percent)) / 100)	+ base_value;
		}
		logger.debug("calculated adjusted_net_worth_of_business :" + adjusted_net_worth_of_business);
		return adjusted_net_worth_of_business;
	}
	
	public double getC5AssetProtectionAllowance(int parent_age, MaritalStatus maritalStats, String academic_year) {
		logger.debug("Calling getC5AssetProtectionAllowance");

		Query findQuery = new Query();
		double asset_protection_allowance = 0.0;
		findQuery.addCriteria(
				new Criteria().andOperator(
						Criteria.where("year").is(academic_year),
						Criteria.where("older_person_age").is(parent_age)
						));
		
		Document doc = mongoTemplate.findOne(findQuery, Document.class, "c5_assest_protection_allowance");
		if(doc!=null) {
			if(maritalStats == MaritalStatus.MARRIED || maritalStats == MaritalStatus.UNMARRIED_LIVING_TOGETHER)
				asset_protection_allowance = Double.valueOf(doc.get("married_student_allowance").toString());			
			else
				asset_protection_allowance = Double.valueOf(doc.get("unmarried_student_allowance").toString());
		}
		logger.debug("calculated asset_protection_allowance :" + asset_protection_allowance);
		return asset_protection_allowance;
	}
	
	public double getC6ContributionFromAAI(double adjusted_available_income, String academicYear) {
		logger.debug("Calling getC6ContributionFromAAI");

		double adjusted_net_worth_of_business = 0;
		Query query = new Query();
		query.addCriteria(Criteria.where("year").is(academicYear));
		List<Document> docList = mongoTemplate.find(query, Document.class, "c6_students_contribution_from_aai");
		String operator;
		double base_value, difference, contribution_from_aai;
		if(docList!=null) {
			for(Document doc:docList) {
			double aai_slab_min = Double.valueOf(doc.get("aai_slab_min").toString());
			double aai_slab_max = Double.valueOf(doc.get("aai_slab_max").toString());
			if ((adjusted_available_income >= aai_slab_min)
					&& (adjusted_available_income <= aai_slab_max)) {
				base_value = Double.valueOf(doc.get("base_value").toString());
				operator = doc.getString("operator");
				difference = Double.valueOf(doc.get("difference").toString());
				contribution_from_aai = Double.valueOf(doc.get("contribution_from_aai").toString());
				if (operator.equals("+"))
					adjusted_net_worth_of_business = (((adjusted_available_income - difference) * (contribution_from_aai)) / 100)
							+ base_value;
				}
			}
		}
		logger.debug("calculated adjusted_networth_of_business :" + adjusted_net_worth_of_business);
		return adjusted_net_worth_of_business;
	}
	
	
	public double getB3AdjustedNetWorthOfBusiness(double networth_of_student_job,String academic_year) {
		logger.debug("Calling getB3AdjustedNetWorthOfBusiness");

		String operator;
		double base_value, difference, adjusted_net_worth_percent;
		double adjusted_networth_of_business = 0;
		String tab_name = "b3_business_or_farm";
		Query findQuery = new Query();
		findQuery.addCriteria(new Criteria().andOperator(
				Criteria.where("year").is(academic_year),
				Criteria.where("business_net_worth_slab_min").lte(networth_of_student_job),
				Criteria.where("business_net_worth_slab_max").gte(networth_of_student_job)
		));
		Document doc = mongoTemplate.findOne(findQuery, Document.class, tab_name);
		if(doc!=null) {
			base_value = Double.valueOf(doc.get("base_value").toString());
			operator = doc.getString("operator");
			difference = Double.valueOf(doc.get("difference").toString());
			adjusted_net_worth_percent = Double.valueOf(doc.get("adjusted_net_worth_percent").toString());
			if (operator.equals("+"))
				adjusted_networth_of_business = (((networth_of_student_job - difference) * (adjusted_net_worth_percent)) / 100)
						+ base_value;
		}
		logger.debug("calculated adjusted_networth_of_business :" + adjusted_networth_of_business);
		return adjusted_networth_of_business;
		
	}
}
