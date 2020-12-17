/**
 * 
 */
package com.virtue.csr.service;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.virtue.csr.constants.CONSTANTS;
import com.virtue.csr.model.CounsellorObject;
import com.virtue.csr.model.School;
import com.virtue.csr.repository.CounsellorRepository;
import com.virtue.csr.repository.MinEligibilityGridRepository;
import com.virtue.csr.repository.SchoolRepository;
import com.virtue.csr.repository.YoYRepository;
import com.virtue.csr.util.CommonUtils;

/**
 * 
 */
@Service
public class ReportGenerationService {

	@Autowired
	private CounsellorRepository counsellorRepo;

	@Autowired
	private MinEligibilityGridRepository minEligRepo;
	
	@Autowired
	private YoYRepository yoyRepo;
	
	@Autowired
	private SchoolRepository schoolRepo;

	private Predicate<CounsellorObject> isStudentEligibleForFinances = o -> {
		int householdSize = o.getHouseholdSize();
		double adjustedGrossIncome = o.getAdjustedGrossIncome();
		int maxHouseholdSize = minEligRepo.findTopByOrderByHouseholdSizeDesc().get().getHouseholdSize();
		return (householdSize <= maxHouseholdSize)
				? minEligRepo.findByHouseholdSizeAndMaxAGIGreaterThanEqual(householdSize, adjustedGrossIncome)
						.isPresent()
				: (adjustedGrossIncome <= ((householdSize - maxHouseholdSize)
						* CONSTANTS.MAX_AGI_PER_ADDITIONAL_MEMBER));

	};

	@Autowired
	CommonUtils utils;

	@Value("${report.base-path}")
	String counsellorReportPath;

	HashMap<ReportTypes, String> keyMap;
	
	public static enum ReportTypes {

		COMPLETE("Completed_Applications", 20), 
		INCOMPLETE("InComplete_Applications", 20),
		SUBMISSION_PEND("Submission_Pending_Applications", 10), 
		TRANSCRIPT_PEND("Transcripts_Pending_Applications", 10),
		RECOMMEND_PEND("Recommendations_Pending_Applications", 10), 
		MULTIPLE_PEND("Multiple_Pending_Applications", 10),
		TOTAL("Total_Applications",50);

		String reportName;
		int pageSize;

		/**
		 * @param string
		 * @param i
		 */
		ReportTypes(String value, int i) {
			this.reportName = value;
			this.pageSize = i;
		}

		public String getReportName() {
			return reportName;
		}

		public int getPageSize() {
			return pageSize;
		}
	}

	{
		keyMap = new HashMap<ReportTypes, String>();
		keyMap.put(ReportTypes.COMPLETE, "complete");
		keyMap.put(ReportTypes.INCOMPLETE, "incomplete");
		keyMap.put(ReportTypes.SUBMISSION_PEND, "submission_pending");
		keyMap.put(ReportTypes.TRANSCRIPT_PEND, "transcripts_pending");
		keyMap.put(ReportTypes.RECOMMEND_PEND, "recommendation_pending");
		keyMap.put(ReportTypes.MULTIPLE_PEND, "multiple_pending");
		keyMap.put(ReportTypes.TOTAL,"total_applications");
	}

	public Map<String, List<HashMap<String, String>>> extractCounsellorData(String instName) {
		Map<String, List<HashMap<String, String>>> resultMap = new HashMap<String, List<HashMap<String, String>>>();
		List<HashMap<String, String>> applicationsData = null;

		Pageable pageable = PageRequest.of(0, ReportTypes.COMPLETE.getPageSize(),
				Sort.by(Direction.ASC, "applicationCode"));
		applicationsData = instName != null
				? counsellorRepo.findCompletedApplications(instName, pageable)
						.orElse(new ArrayList<HashMap<String, String>>())
				: counsellorRepo.findCompletedApplications(pageable).orElse(new ArrayList<HashMap<String, String>>());
		resultMap.put(keyMap.get(ReportTypes.COMPLETE), applicationsData);

		pageable = PageRequest.of(0, ReportTypes.INCOMPLETE.getPageSize(), Sort.by(Direction.ASC, "applicationCode"));
		applicationsData = instName != null
				? counsellorRepo.findInCompleteApplications(instName, pageable)
						.orElse(new ArrayList<HashMap<String, String>>())
				: counsellorRepo.findInCompleteApplications(pageable).orElse(new ArrayList<HashMap<String, String>>());
		resultMap.put(keyMap.get(ReportTypes.INCOMPLETE), applicationsData);

		pageable = PageRequest.of(0, ReportTypes.SUBMISSION_PEND.getPageSize(),
				Sort.by(Direction.ASC, "applicationCode"));
		applicationsData = instName != null
				? counsellorRepo.findOnlySubmissionPendingApplications(instName, pageable)
						.orElse(new ArrayList<HashMap<String, String>>())
				: counsellorRepo.findOnlySubmissionPendingApplications(pageable)
						.orElse(new ArrayList<HashMap<String, String>>());
		resultMap.put(keyMap.get(ReportTypes.SUBMISSION_PEND), applicationsData);

		pageable = PageRequest.of(0, ReportTypes.TRANSCRIPT_PEND.getPageSize(),
				Sort.by(Direction.ASC, "applicationCode"));
		applicationsData = instName != null
				? counsellorRepo.findOnlyTranscriptsPendingApplications(instName, pageable)
						.orElse(new ArrayList<HashMap<String, String>>())
				: counsellorRepo.findOnlyTranscriptsPendingApplications(pageable)
						.orElse(new ArrayList<HashMap<String, String>>());
		resultMap.put(keyMap.get(ReportTypes.TRANSCRIPT_PEND), applicationsData);

		pageable = PageRequest.of(0, ReportTypes.RECOMMEND_PEND.getPageSize(),
				Sort.by(Direction.ASC, "applicationCode"));
		applicationsData = instName != null
				? counsellorRepo.findOnlyRecommendationsPendingApplications(instName, pageable)
						.orElse(new ArrayList<HashMap<String, String>>())
				: counsellorRepo.findOnlyRecommendationsPendingApplications(pageable)
						.orElse(new ArrayList<HashMap<String, String>>());
		resultMap.put(keyMap.get(ReportTypes.RECOMMEND_PEND), applicationsData);

		pageable = PageRequest.of(0, ReportTypes.MULTIPLE_PEND.getPageSize(),
				Sort.by(Direction.ASC, "applicationCode"));
		applicationsData = instName != null
				? counsellorRepo.findMultipleStepsPendingApplications(instName, pageable)
						.orElse(new ArrayList<HashMap<String, String>>())
				: counsellorRepo.findMultipleStepsPendingApplications(pageable)
						.orElse(new ArrayList<HashMap<String, String>>());
		resultMap.put(keyMap.get(ReportTypes.MULTIPLE_PEND), applicationsData);

		return resultMap;
	}
	
	public Map<String,Long> countTotalApplications(String instName) {
		Map<String,Long> resultMap=new HashMap<String,Long>();
		if(instName != null) {
			resultMap.put(keyMap.get(ReportTypes.COMPLETE),counsellorRepo.countCompletedApplications(instName));
			resultMap.put(keyMap.get(ReportTypes.INCOMPLETE),counsellorRepo.countInCompleteApplications(instName));
			resultMap.put(keyMap.get(ReportTypes.SUBMISSION_PEND),counsellorRepo.countOnlySubmissionPendingApplications(instName));
			resultMap.put(keyMap.get(ReportTypes.TRANSCRIPT_PEND),counsellorRepo.countOnlyTranscriptsPendingApplications(instName));
			resultMap.put(keyMap.get(ReportTypes.RECOMMEND_PEND),counsellorRepo.countOnlyRecommendationsPendingApplications(instName));
			resultMap.put(keyMap.get(ReportTypes.MULTIPLE_PEND),counsellorRepo.countMultipleStepsPendingApplications(instName));
			resultMap.put(keyMap.get(ReportTypes.TOTAL),counsellorRepo.count(instName));
		}else {
			resultMap.put(keyMap.get(ReportTypes.COMPLETE),counsellorRepo.countCompletedApplications());
			resultMap.put(keyMap.get(ReportTypes.INCOMPLETE),counsellorRepo.countInCompleteApplications());
			resultMap.put(keyMap.get(ReportTypes.SUBMISSION_PEND),counsellorRepo.countOnlySubmissionPendingApplications());
			resultMap.put(keyMap.get(ReportTypes.TRANSCRIPT_PEND),counsellorRepo.countOnlyTranscriptsPendingApplications());
			resultMap.put(keyMap.get(ReportTypes.RECOMMEND_PEND),counsellorRepo.countOnlyRecommendationsPendingApplications());
			resultMap.put(keyMap.get(ReportTypes.MULTIPLE_PEND),counsellorRepo.countMultipleStepsPendingApplications());
			resultMap.put(keyMap.get(ReportTypes.TOTAL),counsellorRepo.count());
		}
		return resultMap;
	}

	public byte[] exportReportToCSV(String instName, ReportTypes reportType) throws IOException {
		File counsellorReportFile = null;

		List<HashMap<String, String>> reportRawData = null;
		if (reportType.equals(ReportTypes.COMPLETE)) {
			counsellorReportFile = new File(counsellorReportPath + "/" + ReportTypes.COMPLETE.getReportName() + ".csv");
			reportRawData = instName != null
					? counsellorRepo.findCompletedApplications(instName, Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo.findCompletedApplications(Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>());
		}
		if (reportType.equals(ReportTypes.INCOMPLETE)) {
			counsellorReportFile = new File(
					counsellorReportPath + "/" + ReportTypes.INCOMPLETE.getReportName() + ".csv");
			reportRawData = instName != null
					? counsellorRepo.findInCompleteApplications(instName, Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo.findInCompleteApplications(Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>());
		}
		if (reportType.equals(ReportTypes.SUBMISSION_PEND)) {
			counsellorReportFile = new File(
					counsellorReportPath + "/" + ReportTypes.SUBMISSION_PEND.getReportName() + ".csv");
			reportRawData = instName != null
					? counsellorRepo.findOnlySubmissionPendingApplications(instName, Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo.findOnlySubmissionPendingApplications(Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>());
		}
		if (reportType.equals(ReportTypes.TRANSCRIPT_PEND)) {
			counsellorReportFile = new File(
					counsellorReportPath + "/" + ReportTypes.TRANSCRIPT_PEND.getReportName() + ".csv");
			reportRawData = instName != null
					? counsellorRepo.findOnlyTranscriptsPendingApplications(instName, Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo.findOnlyTranscriptsPendingApplications(Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>());
		}
		if (reportType.equals(ReportTypes.RECOMMEND_PEND)) {
			counsellorReportFile = new File(
					counsellorReportPath + "/" + ReportTypes.RECOMMEND_PEND.getReportName() + ".csv");
			reportRawData = instName != null
					? counsellorRepo.findOnlyRecommendationsPendingApplications(instName, Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo.findOnlyRecommendationsPendingApplications(Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>());
		}
		if (reportType.equals(ReportTypes.MULTIPLE_PEND)) {
			counsellorReportFile = new File(
					counsellorReportPath + "/" + ReportTypes.MULTIPLE_PEND.getReportName() + ".csv");
			reportRawData = instName != null
					? counsellorRepo.findMultipleStepsPendingApplications(instName, Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo.findMultipleStepsPendingApplications(Pageable.unpaged())
							.orElse(new ArrayList<HashMap<String, String>>());
		}

		Files.createParentDirs(counsellorReportFile);
		Files.touch(counsellorReportFile);
		byte[] strReportData = reportRawData.parallelStream().map(o -> utils.mapToCSVString(o))
				.collect(Collectors.joining(CONSTANTS.LINE_SEPERATOR)).getBytes();
		Files.asByteSink(counsellorReportFile).write(strReportData);
		return Files.asByteSource(counsellorReportFile).read();
	}

	/**
	 * 
	 * @param
	 * @return
	 */

	public List<HashMap<String, String>> extractCounsellorData(String instName, ReportTypes reportType, int pageno) {
		List<HashMap<String, String>> reportRawData = null;
		if (reportType.equals(ReportTypes.COMPLETE)) {
			reportRawData = instName != null
					? counsellorRepo
							.findCompletedApplications(instName,
									PageRequest.of(pageno, ReportTypes.COMPLETE.getPageSize(),
											Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo
							.findCompletedApplications(PageRequest.of(pageno, ReportTypes.COMPLETE.getPageSize(),
									Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>());
		}
		if (reportType.equals(ReportTypes.INCOMPLETE)) {
			reportRawData = instName != null
					? counsellorRepo
							.findInCompleteApplications(instName,
									PageRequest.of(pageno, ReportTypes.INCOMPLETE.getPageSize(),
											Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo
							.findInCompleteApplications(PageRequest.of(pageno, ReportTypes.INCOMPLETE.getPageSize(),
									Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>());
		}
		if (reportType.equals(ReportTypes.SUBMISSION_PEND)) {
			reportRawData = instName != null
					? counsellorRepo
							.findOnlySubmissionPendingApplications(instName,
									PageRequest.of(pageno, ReportTypes.SUBMISSION_PEND.getPageSize(),
											Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo
							.findOnlySubmissionPendingApplications(
									PageRequest.of(pageno, ReportTypes.SUBMISSION_PEND.getPageSize(),
											Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>());
		}
		if (reportType.equals(ReportTypes.TRANSCRIPT_PEND)) {
			reportRawData = instName != null
					? counsellorRepo
							.findOnlyTranscriptsPendingApplications(instName,
									PageRequest.of(pageno, ReportTypes.TRANSCRIPT_PEND.getPageSize(),
											Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo
							.findOnlyTranscriptsPendingApplications(
									PageRequest.of(pageno, ReportTypes.TRANSCRIPT_PEND.getPageSize(),
											Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>());

		}
		if (reportType.equals(ReportTypes.RECOMMEND_PEND)) {
			reportRawData = instName != null
					? counsellorRepo
							.findOnlyRecommendationsPendingApplications(instName,
									PageRequest.of(pageno, ReportTypes.RECOMMEND_PEND.getPageSize(),
											Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo
							.findOnlyRecommendationsPendingApplications(
									PageRequest.of(pageno, ReportTypes.RECOMMEND_PEND.getPageSize(),
											Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>());
		}
		if (reportType.equals(ReportTypes.MULTIPLE_PEND)) {
			reportRawData = instName != null
					? counsellorRepo
							.findMultipleStepsPendingApplications(instName,
									PageRequest.of(pageno, ReportTypes.MULTIPLE_PEND.getPageSize(),
											Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>())
					: counsellorRepo
							.findMultipleStepsPendingApplications(PageRequest.of(pageno,
									ReportTypes.MULTIPLE_PEND.getPageSize(), Sort.by(Direction.ASC, "applicationCode")))
							.orElse(new ArrayList<HashMap<String, String>>());
		}
		return reportRawData;
	}

	public boolean isStudentEligibleForFinances(CounsellorObject studentData) {
		return isStudentEligibleForFinances.test(studentData);
	}

	public List<CounsellorObject> extractStudentsEligibleForFinances(String instName) {
		List<CounsellorObject> counsellorOptn = (instName != null) ? counsellorRepo.findByInstitutionName(instName)
				: counsellorRepo.findAll();
		if (counsellorOptn != null) {
			return counsellorOptn.parallelStream().filter(isStudentEligibleForFinances).collect(Collectors.toList());
		}
		return new ArrayList<CounsellorObject>();
	}

	public List<HashMap<String, String>> countCompletedApplicationsBySchool() {
		return counsellorRepo.groupCompletedApplicationsBySchool().get();
	}
	
	public List<HashMap<String, String>> countCompletedApplicationsBySchoolPerRegion(String region) {
		List<HashMap<String, Integer>> tempList = (region != null)
				? counsellorRepo.groupCompletedApplicationsBySchoolPerRegion(region).get()
				: counsellorRepo.groupCompletedApplicationsBySchoolAllRegions().get();
		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		tempList.forEach(o -> {
			if(o.get("_id")!=null) {
				School sch = schoolRepo.findByCeeb(o.get("_id")).orElse(null);
				if (sch != null) {
					HashMap<String, String> tempMap = new HashMap<String, String>();
					tempMap.put("name", sch.getName());
					tempMap.put("count", String.valueOf(o.get("count")));
					resultList.add(tempMap);
				}
			}
		});
		return resultList;
	}

	public List<HashMap<String, String>> countStudentsByGender(String instName) {
		return (instName != null) ? counsellorRepo.groupByGender(instName).get() 
				: counsellorRepo.groupByGender().get();
	}
	
	public List<HashMap<String, String>> countStudentsByGenderPerRegion(String region) {
		return (region != null) ? counsellorRepo.groupByGenderPerRegion(region).get() 
				: counsellorRepo.groupByGenderAllRegions().get();
	}

	public List<HashMap<String, String>> countStudentsByEthnicity(String instName) {
		return (instName != null) ? counsellorRepo.groupByEthnicity(instName).get()
				: counsellorRepo.groupByEthnicity().get();
	}
	
	public List<HashMap<String, String>> countStudentsByEthnicityPerRegion(String region) {
		return (region != null) ? counsellorRepo.groupByEthnicityPerRegion(region).get()
				: counsellorRepo.groupByEthnicityAllRegions().get();
	}
	
	//public Map<String, HashMap<String, Collection<String>>>
	public Map<String,HashMap<String,Collection<String>>> countApplicationsByMonth(String instName){
		List<HashMap<String,Integer>> dbList = (instName != null) ? yoyRepo.countApplicationsByHighSchool(instName)
				: yoyRepo.countAllApplications();
		DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM").withLocale(Locale.ENGLISH);
		Map<String,HashMap<String,Collection<String>>> resultMap=new HashMap<String,HashMap<String,Collection<String>>>();
		
		TreeMap<String,Integer> descMap=
				new TreeMap<String,Integer>(new Comparator<String>(){
					@Override
					public int compare(String o1, String o2) {
						TemporalAccessor accessor1 = parser.parse(o1);
						TemporalAccessor accessor2 = parser.parse(o2);
						return accessor2.get(ChronoField.MONTH_OF_YEAR) - accessor1.get(ChronoField.MONTH_OF_YEAR);
					}
		});
		
		dbList.stream().forEach( o -> {
			HashMap<String,Collection<String>> tempMap=new HashMap<String,Collection<String>>();
			String year=String.valueOf(o.get("_id"));
			o.remove("_id");
			Set<Entry<String, Integer>> entrySet=o.entrySet();
			entrySet.forEach(t -> { descMap.put(t.getKey(),t.getValue()); } );
			while(descMap.firstEntry()!=null && descMap.firstEntry().getValue()==0) {
				descMap.pollFirstEntry();
			}
			NavigableMap<String,Integer> sortedMap=descMap.descendingMap();
			tempMap.put("months",sortedMap.keySet().stream().map(String::toUpperCase).collect(Collectors.toList()));
			tempMap.put("values",sortedMap.values().stream().map(String::valueOf).collect(Collectors.toList()));
			resultMap.put(year,tempMap);
			sortedMap.clear();
		});
		return resultMap;
	}
	
	public List<HashMap<String,String>> countApplicationsBySchool(){
		HashMap<String, List<HashMap<String, String>>> dbMap=counsellorRepo.groupApplicationsBySchool().get();
		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String,String>>();
		Multimap<String,Integer> tempMap=ArrayListMultimap.create();
		if(dbMap != null) {
			dbMap.get("Total").forEach( t -> {
				tempMap.put( t.get("_id"), Integer.parseInt(t.get("count")));
			});
			dbMap.get("Complete").forEach( t -> {
				tempMap.put( t.get("_id"), Integer.parseInt(t.get("count")));
			});
		}
		tempMap.asMap().forEach( (k,v) -> {
			HashMap<String,String> varMap=new HashMap<String,String>();
			Integer tempArr[]=v.toArray(new Integer[2]);
			varMap.put("key",k);
			varMap.put("value",k);
			varMap.put("totalCount",String.valueOf(tempArr[0]));
			varMap.put("CompletedCount",String.valueOf(Optional.ofNullable(tempArr[1]).orElse(0)));
			resultList.add(varMap);
		});
		return resultList;
	}
}
