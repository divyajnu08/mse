package com.virtue.csr.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class ConversionUtils {

	private static LinkedHashMap<String, String> states = new LinkedHashMap<String, String>() {{
		put("AL","Alabama");
		put("AK","Alaska");
		put("AZ","Arizona");
		put("AR","Arkansas");
		put("CA","California");
		put("CO","Colorado");
		put("CT","Connecticut");
		put("DE","Delaware");
		put("DC","District of Columbia");
		put("FL","Florida");
		put("GA","Georgia");
		put("HI","Hawaii");
		put("ID","Idaho");
		put("IL","Illinois");
		put("IN","Indiana");
		put("IA","Iowa");
		put("KS","Kansas");
		put("KY","Kentucky");
		put("LA","Louisiana");
		put("ME","Maine");
		put("MD","Maryland");
		put("MA","Massachusetts");
		put("MI","Michigan");
		put("MN","Minnesota");
		put("MS","Mississippi");
		put("MO","Missouri");
		put("MT","Montana");
		put("NE","Nebraska");
		put("NV","Nevada");
		put("NH","New Hampshire");
		put("NJ","New Jersey");
		put("NM","New Mexico");
		put("NY","New York");
		put("NC","North Carolina");
		put("ND","North Dakota");
		put("OH","Ohio");
		put("OK","Oklahoma");
		put("OR","Oregon");
		put("PA","Pennsylvania");
		put("RI","Rhode Island");
		put("SC","South Carolina");
		put("SD","South Dakota");
		put("TN","Tennessee");
		put("TX","Texas");
		put("UT","Utah");
		put("VT","Vermont");
		put("VA","Virginia");
		put("WA","Washington");
		put("WV","West Virginia");
		put("WI","Wisconsin");
		put("WY","Wyoming");
	}};
	
	private static LinkedHashMap<String, String> colleges = new LinkedHashMap<String, String>(){{
		put("MN-TC","University Of Minnesota-Twin Cities");
		put("AB","Augsburg University");
		put("ST","University Of Saint Thomas");
		put("SO","Saint Olaf College");
		put("HU","Hamline University");
		put("SC","Saint Catherine University");
		put("MS","Macalester College");
		put("MN-DU","University Of Minnesota-Duluth");
		put("CS","College of Saint Benedict");
		put("GA","Gustavus Adolphus College");
		put("CC","Carleton College");
		put("BU","Bethel University");
		put("SJ","Saint John's University");
		put("MU","Metropolitan State University");
		put("MN-MO","University Of Minnesota-Morris");
		put("SC","Saint Cloud State University");
		put("MN-RO","University Of Minnesota-Rochester");
		put("CSCH","College of Saint Scholastica");
		put("MSU","Minnesota State University");
		put("UNWSP","University Of NorthWestern-St Paul");
		put("BS","Bemidji State University");
		put("BG","Bethany Global University");
		put("BL","Bethany Lutheran College");
		put("BC","Bethlehem College and Seminary");
		put("CM","Concordia College at Moorhead");
		put("CSP","Concordia University Saint Paul");
		put("CR","Crown College");
		put("DC","Dunwoody College of Technology");
		put("HU","Herzing University-Minneapolis");
		put("ML","Martin Luther College");
		put("MMS","Mayo Clinic College Of Medicine and Science");
		put("MA","Minneapolis College of Art and design");
		put("MN-MR","Minnesota State University Moorhead");
		put("NC","North Central University");
		put("NH","NorthWestern Health Sciences University");
		put("OK","Oak Hills Christian College");
		put("SC","Saint Cloud State university");
		put("SJ","Saint John's University");
		put("SM","Saint Mary's University of Minnesota");
		put("SW","SouthWest Minnesota State University");
		put("MN-CS","University of Minnesota Crookston");
		put("WS","WInona State University");
	}};

	
    public double sat2act(double sat_composite, double act_composite) {
        int converted_act_composite=0;
        if (sat_composite >0){

            converted_act_composite=(sat_composite>=1570 ? 1: 0)*36+ (sat_composite<1570 && sat_composite>=1530 ? 1: 0)*35 +
                    (sat_composite<1530 && sat_composite>=1490 ? 1: 0)*34+ (sat_composite<1490 && sat_composite>=1450 ? 1: 0)*33+
                    (sat_composite<1450 && sat_composite>=1420 ? 1: 0)*32+(sat_composite<1420 && sat_composite>=1390 ? 1: 0)*31+
                    (sat_composite<1390 && sat_composite>=1360 ? 1: 0)*30+ (sat_composite<1360 && sat_composite>=1330 ? 1: 0)*29+
                    (sat_composite<1330 && sat_composite>=1300 ? 1: 0)*28+(sat_composite<1300 && sat_composite>=1260 ? 1: 0)*27+
                    (sat_composite<1260 && sat_composite>=1230 ? 1: 0)*26+(sat_composite<1230 && sat_composite>=1200 ? 1: 0)*25+
                    (sat_composite<1200 && sat_composite>=1160 ? 1: 0)*24+(sat_composite<1160 && sat_composite>=1130 ? 1: 0)*23+
                    (sat_composite<1130 && sat_composite>=1100 ? 1: 0)*22+(sat_composite<1100 && sat_composite>=1060 ? 1: 0)*21+
                    (sat_composite<1060 && sat_composite>=1030 ? 1: 0)*20+(sat_composite<1030 && sat_composite>=990 ? 1: 0)*19+
                    (sat_composite<990 && sat_composite>=960 ? 1: 0)*18+(sat_composite<960 && sat_composite>=920 ? 1: 0)*17+
                    (sat_composite<920 && sat_composite>=880 ? 1: 0)*16+(sat_composite<880 && sat_composite>=830 ? 1: 0)*15+
                    (sat_composite<830 && sat_composite>=780 ? 1: 0)*14+(sat_composite<780 && sat_composite>=730 ? 1: 0)*13+
                    (sat_composite<730 && sat_composite>=690 ? 1: 0)*12+(sat_composite<690 && sat_composite>=650 ? 1: 0)*11+
                    (sat_composite<650 && sat_composite>=620 ? 1: 0)*10+(sat_composite<620 && sat_composite>=590 ? 1: 0)*9;

        }else{
            converted_act_composite=0;
        }

        if(act_composite>0){
            act_composite=Math.max(converted_act_composite, act_composite);
        }else{
            act_composite=converted_act_composite;
        }
        return act_composite;
    }

    public double clt2act(double clt_composite, double act_composite) {
        int converted_act_composite=0;
        if (clt_composite >0){

            converted_act_composite=(clt_composite>=111 ? 1: 0)*36+ (clt_composite<111 && clt_composite>=107 ? 1: 0)*35 +
                    (clt_composite<107 && clt_composite>=103 ? 1: 0)*34+ (clt_composite<103 && clt_composite>=100 ? 1: 0)*33+
                    (clt_composite<100 && clt_composite>=97 ? 1: 0)*32+(clt_composite<97 && clt_composite>=94 ? 1: 0)*31+
                    (clt_composite<94 && clt_composite>=92 ? 1: 0)*30+ (clt_composite<92 && clt_composite>=89 ? 1: 0)*29+
                    (clt_composite<89 && clt_composite>=86 ? 1: 0)*28+ (clt_composite<86 && clt_composite>=84 ? 1: 0)*27+
                    (clt_composite<84 && clt_composite>=81 ? 1: 0)*26+ (clt_composite<81 && clt_composite>=78 ? 1: 0)*25+
                    (clt_composite<78 && clt_composite>=76 ? 1: 0)*24+ (clt_composite<76 && clt_composite>=74 ? 1: 0)*23+
                    (clt_composite<74 && clt_composite>=72 ? 1: 0)*22+ (clt_composite<72 && clt_composite>=68 ? 1: 0)*21+
                    (clt_composite<68 && clt_composite>=66 ? 1: 0)*20+ (clt_composite<66 && clt_composite>=64 ? 1: 0)*19+
                    (clt_composite<64 && clt_composite>=61 ? 1: 0)*18+ (clt_composite<61 && clt_composite>=57 ? 1: 0)*17+
                    (clt_composite<57 && clt_composite>=55 ? 1: 0)*16+ (clt_composite<55 && clt_composite>=52 ? 1: 0)*15+
                    (clt_composite<52 && clt_composite>=49 ? 1: 0)*14+ (clt_composite<49 && clt_composite>=46 ? 1: 0)*13+
                    (clt_composite<46 && clt_composite>=44 ? 1: 0)*12+ (clt_composite<44 && clt_composite>=40 ? 1: 0)*11+
                    (clt_composite<40 ? 1: 0)*10;
        }else{
            converted_act_composite=0;
        }

        if(act_composite>0){
            act_composite=Math.max(converted_act_composite, act_composite);
        }else{
            act_composite=converted_act_composite;
        }
        return act_composite;
    }
    
    public String getState(String key) {
    	return states.get(key);    	
    }
    
    public String getCollege(String key) {
    	return colleges.get(key);
    }
    
    public int getAge(int month,int year) {
    	LocalDate today = LocalDate.now(); 
    	LocalDate birthday = LocalDate.of(year, Month.of(month), 1);
    	Period p = Period.between(birthday, today);
    	return p.getYears();
    }
    
    public boolean ansToBoolean(String ans) {
    	if(ans.equalsIgnoreCase("yes"))return true;
    	return false;
    }
}
