package com.virtue.csr.service;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtue.csr.constants.CONSTANTS;
import com.virtue.csr.constants.MaritalStatus;
import com.virtue.csr.constants.Scenario;
import com.virtue.csr.model.NPCObject;
import com.virtue.csr.model.student.DemoGraphics;
import com.virtue.csr.repository.NPCObjectRepository;
import com.virtue.csr.util.ConversionUtils;

@Service
public class NpcCommonService {
	private static final Logger logger = LoggerFactory.getLogger(NpcCommonService.class);

	@Autowired
	private SequenceGenerator seqGenerator;

	@Autowired
	private NPCObjectRepository npcdtoRepository;
	
	@Autowired
	private ConversionUtils conversionUtils;

	// Create operation
	public NPCObject create(NPCObject npcdto) {
		try {
			return npcdtoRepository.save(npcdto);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Retrieve operation
	public List<NPCObject> getAll() {
		return npcdtoRepository.findAll();
	}

	// Delete operation
	public void deleteAll() {
		npcdtoRepository.deleteAll();
	}

	public NPCObject update(NPCObject npcdto) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStudentId(DemoGraphics npcDto) {
		logger.debug("calling getStudentId:");
		Optional<String> first_part = Optional.empty();
		Optional<String> second_part = Optional.empty();
		Optional<String> third_part = Optional.empty();
		if (npcDto != null) {
			if (npcDto.getFirstName() != null)
				first_part = Optional.of(npcDto.getFirstName().substring(0, 2).toUpperCase());
			if (npcDto.getLastName() != null)
				second_part = Optional.of(npcDto.getLastName().substring(0, 2).toUpperCase());
			third_part = Optional.of(String.valueOf(seqGenerator.getNextSequenceId("student_id")));
		}
		logger.debug("Student id:" + first_part.orElse("") + second_part.orElse("") + third_part.orElse(""));
		return first_part.orElse("") + second_part.orElse("") + third_part.orElse("");
	}

	public void setScenario(DemoGraphics demo) {
		logger.debug("calling setScenario:");
		int age=demo.getAge();
		if (demo.isVeteran() 
				|| (age >= CONSTANTS.MIN_INDEPENDENT_AGE) 
				|| demo.getMaritalStatus().equals(MaritalStatus.MARRIED)
				|| demo.getMaritalStatus().equals(MaritalStatus.SEPARATED)) {
			if (demo.getDependentCount() != 0 || demo.getDependentCount() > 0) {
				demo.setScenario(Scenario.C);
			} else {
				demo.setScenario(Scenario.B);
			}
		} else {
			if (!demo.getMaritalStatus().equals(MaritalStatus.MARRIED)) {
				demo.setScenario(Scenario.A);
			} else {
				if (demo.getDependentCount() != 0 || demo.getDependentCount() > 0) {
					demo.setScenario(Scenario.C);
				} else {
					demo.setScenario(Scenario.B);
				}
			}
		}
		logger.debug("scenario calculated :" + demo.getScenario());
	}
}
