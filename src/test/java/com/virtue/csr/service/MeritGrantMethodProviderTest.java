package com.virtue.csr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.virtue.csr.constants.TestTypes;
import com.virtue.csr.dto.KeyValue;
import com.virtue.csr.model.AdditionalInformation;
import com.virtue.csr.model.CSRData;
import com.virtue.csr.model.ChoiceInformation;

@SpringBootTest
public class MeritGrantMethodProviderTest {
	
	@Autowired
	MeritGrantMethodProvider provider;

	private CSRData createData() {
		CSRData data=new CSRData();
		return data;
	}
	
	private CSRData createData_1() {
		CSRData data=createData();
		AdditionalInformation addtnInfo=new AdditionalInformation();
		addtnInfo.setScore_type(TestTypes.ACT);
		addtnInfo.setScore(31d);
		addtnInfo.setGpa(3.0);
		data.setAddtnInfo(addtnInfo);
		return data;
	}
	
	private CSRData createData_2() {
		CSRData data=createData_1();
		ChoiceInformation choiceInfo=new ChoiceInformation();
		List<KeyValue> quesList=new ArrayList<KeyValue>();
		quesList.add(new KeyValue("50","yes"));
		Map<Integer,List<KeyValue>> customers=new HashMap<Integer,List<KeyValue>>();
		customers.put(22, quesList);
		choiceInfo.setCustomers(customers);
		data.setChoiceInfo(choiceInfo);
		return data;
	}
	
	@Test
	public void run_calculateMeritGrant_default() {
		double merit=provider.calculateMeritGrant_default(12,12,createData_1());
		assertEquals(merit,22420d);
	}
	
	//temporary , to be fixed
	//@Test
	public void run_calculateMeritGrant_22() {
		double merit=provider.calculateMeritGrant_22(22,22,createData_1());
		assertEquals(merit,11900d);
		merit=provider.calculateMeritGrant_22(22,22,createData_2());
		assertEquals(merit,14000d);
	}

}
