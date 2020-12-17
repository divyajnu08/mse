package com.virtue.csr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.virtue.csr.constants.CONSTANTS;
import com.virtue.csr.constants.FeesItems;
import com.virtue.csr.constants.HousingOptions;
import com.virtue.csr.constants.MaritalStatus;
import com.virtue.csr.constants.MeritKeys;
import com.virtue.csr.constants.PellAwardTypes;
import com.virtue.csr.constants.Scenario;
import com.virtue.csr.dto.ResultDto;
import com.virtue.csr.model.FeesObject;
import com.virtue.csr.model.MeritObject;
import com.virtue.csr.model.NPCObject;
import com.virtue.csr.model.PaymentInfo;
import com.virtue.csr.model.ResultObject;
import com.virtue.csr.model.student.Financials;
import com.virtue.csr.repository.FeesRepository;
import com.virtue.csr.repository.MeritReporsitory;
import com.virtue.csr.util.CommonUtils;
import com.virtue.csr.util.ConversionUtils;

@Service
public class NpcCostCalculationService {
	private static final Logger logger = LoggerFactory.getLogger(NpcCostCalculationService.class);

	@Autowired
	FeesRepository feeRepo;

	@Autowired
	MeritReporsitory meritRepository;

	@Autowired
	LookupDataService lookupDataService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ConversionUtils conversionUtils;

	@Autowired
	private StateGrantMethodProvider stateGrantMethodProvider;

	@Autowired
	private HashMap<String, Object> configProperties;
	
	@Autowired
	CommonUtils util;

	private ResultObject result;

	public ResultObject getResult() {
		return this.result;
	}

	public void processFinalResult(NPCObject npcDto) throws JsonProcessingException {
		logger.debug("calling processFinalResult:");
		logger.debug("Input Object:" + util.toJsonString(npcDto));
		ResultObject result = new ResultObject();
		Map<String, Double> valueMap = null;
		Double remainingNeed = 0d;
		Double coa = 0d;
		Double pellGrant = 0d;
		Double fseog = 0d;
		Double stateGrant = 0d;
		Double merit = 0d;
		Double collegeSpecific = 0d;
		Double dsl = 0d;
		Double dusl = 0d;
		Double fws = 0d;
		Double privateFunding = 0d;
		Double efc = 0d;
		Double originalNeed = 0d;
		Double totalGrants = 0d;
		Double churchDisc = 0d;
		Double alumniDisc = 0d;
		Double otherLoans = 0d;
		Double netPrice = 0d;

		boolean isUSCitizen = npcDto.getDemoGraphics().isUsCitizen();
		int act = Optional.ofNullable(npcDto.getAcademics().getAct()).orElse(0);
		double gpa = Optional.ofNullable(npcDto.getAcademics().getHsGpa4()).orElse(0.0);

		result = new ResultObject();
		result.setMeritGrant(calculateMeritGrant(act, gpa));
		result.setFeesItems(calculateFeeItems(npcDto.getOthers().getHousingOption(), isUSCitizen));
		efc = calculateEFC(npcDto);
		logger.debug("efc calculated :" + efc);

		coa = calculateCOA(result.getFeesItems());
		logger.debug("coa calculated :" + coa);
		remainingNeed = coa - efc;

		merit = calculateMerit(result.getMeritGrant());
		logger.debug("merit calculated :" + merit);
		remainingNeed = remainingNeed - merit;

		if (remainingNeed > 0) {
			pellGrant = calculatePellGrant(PellAwardTypes.FULL_TIME, coa, efc, isUSCitizen, getAcademicYear());
			logger.debug("pell grant calculated :" + pellGrant);
			remainingNeed = remainingNeed - pellGrant;
		}

		if (remainingNeed > 0) {
			fseog = calculateFseog(pellGrant);
			logger.debug("fseog calculated :" + fseog);
			remainingNeed = remainingNeed - fseog;
		}

		if (remainingNeed > 0) {
			stateGrant = calculateStateGrant(getAcademicYear(), npcDto.getAcademics().getHsYear());
			logger.debug("state grant calculated :" + stateGrant);
			remainingNeed = remainingNeed - stateGrant;
		}

		if (remainingNeed > 0) {
			collegeSpecific = calculateCollegeSpecific();
			logger.debug("college specific calculated :" + collegeSpecific);
			remainingNeed = remainingNeed - collegeSpecific;
		}

		double unmet_cost = Math.max(remainingNeed, coa - getDirectCost(result.getFeesItems()));

		if (remainingNeed > 0) {
			valueMap = calculateDSL_DUSL_FWS(unmet_cost, npcDto.getDemoGraphics().isUsCitizen(),
					npcDto.getDemoGraphics().getScenario(), pellGrant);
			dsl = valueMap.get("dsl");
			logger.debug("dsl calculated :" + dsl);
			remainingNeed = remainingNeed - valueMap.get("dsl");
		}

		if (remainingNeed > 0) {
			remainingNeed = remainingNeed - valueMap.get("dusl");
			dusl = valueMap.get("dusl");
			logger.debug("dusl calculated :" + dusl);
		}

		if (remainingNeed > 0) {
			remainingNeed = remainingNeed - valueMap.get("fws");
			fws = valueMap.get("fws");
			logger.debug("fws calculated :" + fws);
		}

		if (remainingNeed > 0) {
			privateFunding = calculatePrivateFunding(remainingNeed);
			logger.debug("private funsing calculated :" + privateFunding);
			remainingNeed = remainingNeed - privateFunding;
		}
		originalNeed = calculateOriginalNeed(coa, efc);
		logger.debug("original need calculated :" + originalNeed);
		churchDisc = calculateChristianDisc(npcDto.getOthers().isChurchAffliation());
		logger.debug("church disc calculated :" + churchDisc);
		alumniDisc = calculateAlumniDisc(npcDto.getOthers().isLegacyAffliation());
		logger.debug("alumni disc calculated :" + alumniDisc);
		totalGrants = calculateTotalGrants(merit, pellGrant, fseog, stateGrant, alumniDisc, churchDisc);
		logger.debug("total grants calculated :" + totalGrants);
		netPrice = calculateNetPrice(coa, totalGrants);
		logger.debug("net price calculated :" + netPrice);
		if (valueMap != null) {
			otherLoans = valueMap.get("other_loans");
			logger.debug("other loans calculated :" + otherLoans);
		}

		result.setEfc(efc);
		result.setCoa(coa);
		result.setMerit(merit);
		result.setPellGrant(pellGrant);
		result.setFseog(fseog);
		result.setStateGrant(stateGrant);
		result.setCollegeSpecific(collegeSpecific);
		result.setDsl(dsl);
		result.setDusl(dusl);
		result.setFws(fws);
		result.setPrivateFunding(privateFunding);
		result.setOriginalNeed(originalNeed);
		result.setRemainingNeed(remainingNeed);
		result.setTotalGrants(totalGrants);
		result.setNetPrice(netPrice);
		result.setOtherLoans(otherLoans);
		this.result = result;
		logger.info("NPC Object is populated with result");
	}

	private double getItemFromFees(List<FeesObject> items, FeesItems item) {
		for (FeesObject obj : items) {
			if (obj.getKey().equals(item)) {
				return obj.getValue();
			}
		}
		return 0;
	}

	private double calculateAlumniDisc(boolean alumni_flag) {
		if (alumni_flag) {
			return CONSTANTS.ALUMNI_DISC;
		}
		return 0;
	}

	private double calculateChristianDisc(boolean church_flag) {
		if (church_flag) {
			return CONSTANTS.CHRISTIAN_DISC;
		}
		return 0;
	}

	private double calculateTotalGrants(double merit, double pellGrant, double fseog, double stateGrant,
			double alumniDisc, double churchDisc) {
		return merit + pellGrant + fseog + stateGrant + alumniDisc + churchDisc;
	}

	private double calculateNetPrice(double coa, double totalGrants) {
		if ((coa - totalGrants) >= 0) {
			return coa - totalGrants;
		} else {
			return 0;
		}
	}

	public List<ResultDto> generateOutput(ResultObject obj) {
		Query query = new Query();
		query.addCriteria(Criteria.where("groupKey").is("result.object"));
		List<Document> result = mongoTemplate.find(query, Document.class, "npc_config");
		List<Document> labelList = result.stream().sorted((o1, o2) -> {
			int index1 = Integer
					.valueOf(((String) o1.get("key")).substring(((String) o1.get("key")).lastIndexOf(".") + 1));
			int index2 = Integer
					.valueOf(((String) o2.get("key")).substring(((String) o2.get("key")).lastIndexOf(".") + 1));
			return Integer.compare(index1, index2);
		}).collect(Collectors.toList());
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		ResultDto dto = null;
		dto = new ResultDto(labelList.get(0).getString("key"), String.valueOf(Math.round(obj.getNetPrice())),
				labelList.get(0).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(1).getString("key"), String.valueOf(Math.round(obj.getPellGrant())),
				labelList.get(1).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(2).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.INSTATE_TUTION))),
				labelList.get(2).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(3).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.PERSONAL))),
				labelList.get(3).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(4).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.ROOM))),
				labelList.get(4).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(5).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.TRANSPORT))),
				labelList.get(5).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(6).getString("key"), String.valueOf(Math.round(obj.getFseog())),
				labelList.get(6).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(7).getString("key"), String.valueOf(Math.round(obj.getMerit())),
				labelList.get(7).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(8).getString("key"), String.valueOf(Math.round(0)),
				labelList.get(8).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(9).getString("key"), String.valueOf(Math.round(obj.getStateGrant())),
				labelList.get(9).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(10).getString("key"), String.valueOf(Math.round(obj.getCoa())),
				labelList.get(10).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(11).getString("key"), String.valueOf(Math.round(obj.getTotalGrants())),
				labelList.get(11).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(12).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.ACTIVITY))),
				labelList.get(12).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(13).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.HEALTH_SERVICE))),
				labelList.get(13).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(14).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.IT))),
				labelList.get(14).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(15).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.MEAL))),
				labelList.get(15).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(16).getString("key"),
				String.valueOf(Math.round(getItemFromFees(obj.getFeesItems(), FeesItems.BOOKS))),
				labelList.get(16).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(17).getString("key"), String.valueOf(Math.round(0)),
				labelList.get(17).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(18).getString("key"), String.valueOf(Math.round(obj.getDsl())),
				labelList.get(18).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(19).getString("key"), String.valueOf(Math.round(obj.getDusl())),
				labelList.get(19).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(20).getString("key"), String.valueOf(Math.round(obj.getFws())),
				labelList.get(20).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(21).getString("key"), String.valueOf(Math.round(obj.getOtherLoans())),
				labelList.get(21).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(22).getString("key"),
				String.valueOf(Math.round(getAddtnFinancialAid(obj.getFws(), obj.getOtherLoans()))),
				labelList.get(22).getString("value"));
		resultList.add(dto);
		dto = new ResultDto(labelList.get(23).getString("key"), String.valueOf(Math.round(0)),
				labelList.get(23).getString("value"));
		resultList.add(dto);
		return resultList;
	}

	private double getAddtnFinancialAid(double fws, double otherLoans) {
		return fws + otherLoans;
	}

	public String getAcademicYear() {
		return (String) configProperties.get(CONSTANTS.BASE_YEAR_KEY);
	}

	public String getHomeState() {
		return (String) configProperties.get(CONSTANTS.HOME_STATE_KEY);
	}

	private double getDirectCost(List<FeesObject> feesItems) {
		double directCost = 0;
		for (FeesObject feeItem : feesItems) {
			if (feeItem.getKey().equals(FeesItems.INSTATE_TUTION) || feeItem.getKey().equals(FeesItems.ACTIVITY)
					|| feeItem.getKey().equals(FeesItems.HEALTH_SERVICE) || feeItem.getKey().equals(FeesItems.IT)
					|| feeItem.getKey().equals(FeesItems.ROOM) || feeItem.getKey().equals(FeesItems.MEAL)) {
				directCost += feeItem.getValue();
			}
		}
		return directCost;
	}

	private Double calculatePrivateFunding(Double remainingNeed) {
		return remainingNeed;
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

	private Double calculateCollegeSpecific() {
		return 0.0;
	}

	private Double calculateMerit(List<MeritObject> meritItems) {
		return meritItems.stream().mapToDouble(o -> o.getValue()).sum();
	}

	private Double calculateStateGrant(Object... vars) {
		Function<Object[], Double> stateGrantFunction = StateGrantMethodProvider.METHOD_MAP.get(getHomeState());
		if (stateGrantFunction != null)
			return stateGrantFunction.apply(vars);
		return 0d;
	}

	private Double calculatePellGrant(PellAwardTypes award_type, double coa, double efc, boolean isUSCitizen,
			String academicYear) {
		Double pellGrantValue = 0d;
		if (isUSCitizen) {
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("min").lte(coa), Criteria.where("max").gte(coa),
					Criteria.where("payment_schedule").is(award_type.toString()),
					Criteria.where("year").is(academicYear)));
			Document doc = mongoTemplate.findOne(query, Document.class, "pell_grant");
			String field = null;
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
			if (field != null) {
				pellGrantValue = Double.valueOf(doc.get(field).toString()).doubleValue();
			}
		}
		return pellGrantValue;
	}

	private Double calculateOriginalNeed(double coa, double efc) {
		double originalNeed;
		if (efc < 0)
			originalNeed = coa;
		else
			originalNeed = coa - efc;
		return originalNeed;
	}

	private List<MeritObject> calculateMeritGrant(int act, double gpa) {
		List<MeritObject> objs = meritRepository.findByGroupKey(MeritKeys.MERIT);
		List<MeritObject> resultList = new ArrayList<MeritObject>();
		for (MeritObject o : objs) {
			if ((gpa >= o.getField1Min() && gpa < o.getField1Max())
					&& (act >= o.getField2Min() && act < o.getField2Max())) {
				resultList.add(o);
			}
		}
		return resultList;
	}

	private List<FeesObject> calculateFeeItems(HousingOptions desiredHousingOption, boolean isUSCitizen) {
		List<FeesObject> feeItems = new ArrayList<FeesObject>();
		if (isUSCitizen) {
			feeItems = feeRepo.findByYearAndHousingOption(getAcademicYear(), desiredHousingOption);
		}
		return feeItems;
	}

	private double calculateFseog(double pellGrant) {
		if (pellGrant > 0) {
			return 2000d;
		}
		return 0d;
	}

	private double calculateCOA(List<FeesObject> feeItems) {
		return feeItems.stream().mapToDouble(o -> o.getValue()).sum();
	}

	public Double calculateEFC(NPCObject npcDto) {
		double efc = 0.0;
		Scenario scenario = npcDto.getDemoGraphics().getScenario();
		boolean isUSCitizen = npcDto.getDemoGraphics().isUsCitizen();
		if (isUSCitizen) {
			if (Scenario.A == scenario) {
				efc = calculateEfcForA(npcDto);
			} else if (Scenario.B == scenario) {
				efc = calculateEfcForB(npcDto);
			}
			if (Scenario.C == scenario) {
				efc = calculateEfcForC(npcDto);
			}
		}
		return efc;
	}

	public double calculateEfcForA(NPCObject npcDto) {
		Financials finInfo = npcDto.getFinancials();
		String academic_year = getAcademicYear();
		double studentTax = Optional.ofNullable(finInfo.getStudentTax()).orElse(0.0).doubleValue();
		double parentsTax = Optional.ofNullable(finInfo.getParentsTax()).orElse(0.0).doubleValue();
		double studentNetWorth = Optional.ofNullable(finInfo.getStudentNetworth()).orElse(0.0).doubleValue();
		double studentIncome = Optional.ofNullable(finInfo.getStudentIncome()).orElse(0.0).doubleValue();
		double studentTotalAssets = Optional.ofNullable(finInfo.getStudentTotalAssests()).orElse(0.0).doubleValue();
		double parentsTotalAssets = Optional.ofNullable(finInfo.getParentsTotalAssests()).orElse(0.0).doubleValue();
		double parent1_income = Optional.ofNullable(finInfo.getParent1Income()).orElse(0.0).doubleValue();
		double parent2_income = Optional.ofNullable(finInfo.getParent2Income()).orElse(0.0).doubleValue();
		double parentNetWorth = Optional.ofNullable(finInfo.getNetworthParentsBusiness()).orElse(0.0).doubleValue();
		String parentsState = conversionUtils.getState(finInfo.getParentResidencyState());

		double parent_total_income = finInfo.getParent1Income() != null ? finInfo.getParent1Income() : 0d;

		if (finInfo.getParentsMritalStatus() == MaritalStatus.MARRIED
				|| finInfo.getParentsMritalStatus() == MaritalStatus.UNMARRIED_LIVING_TOGETHER) {
			parent_total_income = parent_total_income + finInfo.getParent2Income();
		}

		double parent_state_and_other_tax = lookupDataService.getA1StateTaxForParents(parentsState, academic_year,
				parent_total_income);
		double parents_social_security_tax = lookupDataService.getA2SocialSecurityTax(academic_year,
				finInfo.getParent1Income());

		if (finInfo.getParentsMritalStatus() == MaritalStatus.MARRIED
				|| finInfo.getParentsMritalStatus() == MaritalStatus.UNMARRIED_LIVING_TOGETHER) {
			parents_social_security_tax = parents_social_security_tax
					+ lookupDataService.getA2SocialSecurityTax(academic_year, finInfo.getParent2Income());
		}
		
		double income_protection_allowance = lookupDataService.getA3IncomeProtectionAllowance(
				finInfo.getHouseholdAllCount(), finInfo.getHouseholdStudcountExclParents(), academic_year);
		
		double employment_expense_allowance = lookupDataService
				.getEmploymentExpenseAllowanceForA(finInfo.getParentsMritalStatus(), parent1_income, parent2_income);

		// total_allowance -A2000
		double total_allowance = parent_state_and_other_tax + parents_social_security_tax + income_protection_allowance
				+ employment_expense_allowance + parentsTax + studentTax;

		// Contribution from Available Income
		// Available Income = A1000 minus A2000 - A3000
		double available_income = parent_total_income - total_allowance;

		// Parent's Contribution from Assets
		// Parents Total Assets - A6000 - parents_total_assests
		// Parent's Net Worth of Business and/or Investment farm -A7000 -
		// networth_of_parents_own_job
		// Adjusted Net worth of Business and/or Investment farm - A7000A4

		double adjusted_net_worth_of_business = lookupDataService.getA4AdjustedNetworthOfBusiness(parentNetWorth,
				academic_year);

		// Net Worth = A6000+A7000A4 - A8000
		double net_worth = parentsTotalAssets + adjusted_net_worth_of_business;
		double asset_protection_allowance = lookupDataService.getA5AssetProtectionAllowance(finInfo.getOldPersonAge(),
				finInfo.getParentsMritalStatus(), academic_year);

		// Discretionary net worth = A8000-A9000A5 - A10000
		double discretionary_net_worth = net_worth - asset_protection_allowance;

		// Asset Conversion Rate A11000
		// double asset_conversion_rate = 0.12;
		// Contribution from Assets = A10000*A11000 - A12000
		double contribution_from_assets = discretionary_net_worth * CONSTANTS.SCENARIO_A_ASSET_CONVERSION_RATE;

		// Parent's Contribution
		// Adjusted Available Income (AAI) = A3000+A12000 - A13000
		double adjusted_available_income = available_income + contribution_from_assets;

		// Total parent Contribution from AAI = A13500A6
		double total_parent_contribution_from_AAI = lookupDataService
				.getA6ContributionFromAAI(adjusted_available_income, academic_year);

		// Parents Contribution = A13500A6 divided by PCOUNTC (If negative enter zero) -
		// A0001
		double parents_contribution = 0.0;
		if (finInfo.getHouseholdStudcountExclParents() <= 0)
			parents_contribution = total_parent_contribution_from_AAI;
		else
			parents_contribution = total_parent_contribution_from_AAI / finInfo.getHouseholdStudcountExclParents();

		// Allowance Against Student Income
		double student_state_and_other_tax = 0;
		// Student Income (Maybe a negative number) B0250
		// Calculate State and Other Tax using Table A7 - A15000A7

		student_state_and_other_tax = lookupDataService
				.getA7StateTaxForStudent(conversionUtils.getState(getHomeState()), studentIncome, academic_year);

		// Income Protection Allowance = $6,570 (Check each year) - A17000IPA

		// double student_income_protection_allowance = 6840;

		// Allowance for Parent's negative AAI A18000AAI
		double parents_negative_AAI;
		// Total Allowances = A15000A7 + A16000A2 + A17000IPA + A18000AAI -
		// A20000
		if (adjusted_available_income < 0)
			parents_negative_AAI = -(adjusted_available_income);
		else
			parents_negative_AAI = 0;

		double student_social_security_tax = lookupDataService.getA2SocialSecurityTax(academic_year, studentIncome);

		double student_total_allowances = student_state_and_other_tax + student_social_security_tax
				+ CONSTANTS.SCENARIO_A_STUDENT_INCOME_PROTECTION_ALLOWANCE + parents_negative_AAI;

		//
		// Students Contribution from Income
		// Available Income = B0250 minus A20000 - A21000
		double student_available_income = studentIncome - student_total_allowances;

		// Assessment of AI - A22000
		// double assessmentof_AI = 0.5;
		// Student's Contribution from AI = A21000*A22000 (if negative enter
		// zero) - A23000
		double Student_contribution_from_AI = student_available_income * CONSTANTS.SCENARIO_A_ASSESMENT_OF_AI;
		if (Student_contribution_from_AI < 0)
			Student_contribution_from_AI = 0.0;

		// Student Contribution from Assets
		// Net Worth = A24000 + A25000 - A26000

		double student_net_worth = studentTotalAssets + studentNetWorth;

		// Assessment Rate - A27000
		// double student_assessment_rate = 0.2;
		// Students Contribution from Assets = A26000*A27000 = A28000
		double students_contribution_from_assets = student_net_worth * CONSTANTS.SCENARIO_A_STUDENT_ASSESMENT_RATE;

		// Expected Family Contribution = A0001 + A23000 + A28000 (If negative enter
		// zero) = A0000

		double efc = Math.max(parents_contribution, 0.0) + Math.max(Student_contribution_from_AI, 0.0)
				+ Math.max(students_contribution_from_assets, 0.0);

		return efc;
	}

	public double calculateEfcForB(NPCObject npcDto) {
		Financials finInfo = npcDto.getFinancials();
		String academic_year = getAcademicYear();
		double studentTax = Optional.ofNullable(finInfo.getStudentTax()).orElse(0.0).doubleValue();
		double studentsAssets = Optional.ofNullable(finInfo.getStudentTotalAssests()).orElse(0.0).doubleValue();
		double studentIncome = Optional.ofNullable(finInfo.getStudentIncome()).orElse(0.0).doubleValue();
		double spouseIncome = Optional.ofNullable(finInfo.getSpouseIncome()).orElse(0.0).doubleValue();
		double student_total_assests = Optional.ofNullable(finInfo.getStudentTotalAssests()).orElse(0.0).doubleValue();
		double networth_of_student_job = Optional.ofNullable(finInfo.getStudentNetworth()).orElse(0.0).doubleValue();
		String birthYear = String.valueOf(npcDto.getDemoGraphics().getBirthYear());
		int birthMonth = npcDto.getDemoGraphics().getBirthMonth();
		String strBirthMonth = null;
		if (birthMonth < 10)
			strBirthMonth = "0" + String.valueOf(birthMonth);
		else
			strBirthMonth = String.valueOf(birthMonth);
		String dateOfBirth = strBirthMonth + "-" + birthYear;
		double total_income = studentIncome + spouseIncome;
		double state_and_other_tax = (total_income
				* lookupDataService.getB1StateAndOtherTax(conversionUtils.getState(getHomeState()), academic_year))
				/ 100;
		double student_social_security_tax = lookupDataService.getB2SocialSecurityTax(studentIncome, academic_year);
		double spouse_social_security_tax = lookupDataService.getB2SocialSecurityTax(spouseIncome, academic_year);
		double income_protection_allowance = lookupDataService.getIncomeProtectionAllowance(
				npcDto.getDemoGraphics().getMaritalStatus(), npcDto.getFinancials().getSpouseEnrollmentStatus());
		double employment_expense_allowance = lookupDataService.getEmploymentExpenseAllowanceForB(studentIncome,
				spouseIncome, npcDto.getDemoGraphics().getMaritalStatus(),
				npcDto.getFinancials().getEmploymentStatus());
		double total_allowance = state_and_other_tax + student_social_security_tax + spouse_social_security_tax
				+ income_protection_allowance + employment_expense_allowance + studentTax;
		// Available Income - B3000
		double available_income = total_income - total_allowance;
		// Assessment Rate - B4000
		// double assessment_rate = 0.5;
		// Contribution from AI (Available Income) = B3000 multiplied by B4000
		// -B5000
		double contribution_from_availabale_income = available_income * CONSTANTS.SCENARIO_B_ASSESMENT_RATE;

		// Student Contribution from Assets
		// Adjusted Net worth of Business and/or Investment farm
		double adjusted_net_worth_of_business = lookupDataService
				.getB3AdjustedNetWorthOfBusiness(networth_of_student_job, academic_year);

		// Net Worth = B6000+B7000B3 - B8000
		double net_worth = student_total_assests + adjusted_net_worth_of_business;
		int studentAge = npcDto.getDemoGraphics().getAge();
		double asset_protection_allowance = lookupDataService
				.getB4AssetProtectionAllowance(npcDto.getDemoGraphics().getMaritalStatus(), studentAge, academic_year);
		// Discretionary net worth = B8000-B9000B4 -B10000
		double discretionary_net_worth = net_worth - asset_protection_allowance;

		// Asset Conversion Rate B11000
		// double asset_conversion_rate = 0.2;
		// Contribution from Assets = B10000*B11000 - B12000
		double contribution_from_assets = discretionary_net_worth * CONSTANTS.SCENARIO_B_ASSET_CONVERSION_RATE;
		// Contribution from AI and Assets = B5000+B12000 - B13000
		double contribution_from_AI_and_Assets = contribution_from_availabale_income + contribution_from_assets;

		int no_in_college = lookupDataService.getNoInCollge(finInfo.getSpouseEnrollmentStatus());
		double efc = contribution_from_AI_and_Assets / no_in_college;
		return efc;
	}

	public double calculateEfcForC(NPCObject npcDto) {
		Financials finInfo = npcDto.getFinancials();
		String academic_year = getAcademicYear();
		double studentTax = Optional.ofNullable(finInfo.getStudentTax()).orElse(0.0).doubleValue();
		double studentIncome = Optional.ofNullable(finInfo.getStudentIncome()).orElse(0.0).doubleValue();
		double spouseIncome = Optional.ofNullable(finInfo.getSpouseIncome()).orElse(0.0).doubleValue();
		double studentNetWorth = Optional.ofNullable(finInfo.getStudentNetworth()).orElse(0.0).doubleValue();
		double studentTotalAssets = Optional.ofNullable(finInfo.getStudentTotalAssests()).orElse(0.0).doubleValue();
		int age = npcDto.getDemoGraphics().getAge();
		double total_income = studentIncome + spouseIncome;
		double state_and_other_tax = (total_income * lookupDataService
				.getC1StateAndOtherTaxAllowance(conversionUtils.getState(getHomeState()), studentIncome, academic_year))
				/ 100;
		double student_social_security_tax = lookupDataService.getC2SocialSecurityTax(studentIncome, academic_year);

		double spouse_social_security_tax = lookupDataService.getC2SocialSecurityTax(spouseIncome, academic_year);
		// Income Protection Allowance - C1000IPA
		double income_protection_allowance = lookupDataService.getC3IncomeProtectionAllowance(
				finInfo.getHouseholdAllCount(), finInfo.getHouseholdAllStudCount(), academic_year);
		double employment_expense_allowance = lookupDataService.getEmploymentExpenseAllowanceForC(studentIncome,
				spouseIncome, npcDto.getDemoGraphics().getMaritalStatus(),
				npcDto.getFinancials().getEmploymentStatus());
		// total_allowance -C2000
		double total_allowance = state_and_other_tax + student_social_security_tax + spouse_social_security_tax
				+ income_protection_allowance + employment_expense_allowance + studentTax;

		// Contribution from Available Income
		// Available Income - C3000
		double available_income = total_income - total_allowance;
		// Adjusted Net worth of Business and/or Investment farm - C7000C4
		double adjusted_net_worth_of_business = lookupDataService.getC4AdjustedNetworthOfBusiness(studentNetWorth,
				academic_year);
		// Net Worth = C6000+C7000C4 - C8000
		double net_worth = studentTotalAssets + adjusted_net_worth_of_business;

		double asset_protection_allowance = lookupDataService.getC5AssetProtectionAllowance(age,
				finInfo.getParentsMritalStatus(), academic_year);
		// Discretionary net worth = C8000-C9000C5 -C10000
		double discretionary_net_worth = net_worth - asset_protection_allowance;

		// Asset Conversion Rate C11000
		// double asset_conversion_rate = 0.07;
		// Contribution from Assets = C10000*C11000 - C12000
		double contribution_from_assets = discretionary_net_worth * CONSTANTS.SCENARIO_C_ASSET_CONVERSION_RATE;

		// Adjusted Available Income (AAI) = C3000+C12000 - C13000
		double adjusted_AAI = available_income + contribution_from_assets;
		// Total Contribution from AAI = C13000C6
		double total_contrib_from_AAI = lookupDataService.getC6ContributionFromAAI(adjusted_AAI, academic_year);

		int no_in_college = lookupDataService.getNoInCollge(finInfo.getSpouseEnrollmentStatus());
		// EFC (Expected Family Contribution) = C13000C6 divided by B14000
		double efc = Math.max(total_contrib_from_AAI, 0.0) / no_in_college;
		return efc;
	}

}
