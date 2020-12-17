package com.virtue.csr.service;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.virtue.csr.constants.CONSTANTS;
import com.virtue.csr.controller.MainController;
import com.virtue.csr.model.NPCObject;

@Component
public class StateGrantMethodProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(StateGrantMethodProvider.class);
	public static Map<String,Function<Object[],Double>> METHOD_MAP;
	
	static {
		METHOD_MAP = new HashMap<String,Function<Object[],Double>>();
		METHOD_MAP.put("AR", StateGrantMethodProvider::calculateStateGrant_AR );
		METHOD_MAP.put("MN", StateGrantMethodProvider::calculateStateGrant_MN );
	}
	
	public static double calculateStateGrant_AR(Object... vars) {
		logger.debug("calculating state grant for state=AR");
		String baseYear=(String)vars[0];
		int hsYear=(Integer)vars[1];
		String[] parts = baseYear.split("-");
		int part1=Integer.parseInt(parts[0]);
		int part2=Integer.parseInt(parts[1]);
		if(hsYear==part1) {
			return 1000;
		}
		return 0;
	}
	
	public static double calculateStateGrant_MN(Object... vars) {
		logger.debug("calculating state grant for state=MN");
		return 0;
	}

}
