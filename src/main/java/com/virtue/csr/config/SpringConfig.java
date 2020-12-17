package com.virtue.csr.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.MongoClientURI;
import com.virtue.csr.service.SequenceGenerator;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class SpringConfig {

	@Value("${db1.mongodb.uri}")
	private String db1mongoURI;
	
	@Bean
	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoMappingContext context) throws Exception {
		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
	    converter.setCustomConversions(customConversions());
	    converter.afterPropertiesSet();
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, converter);
		return mongoTemplate;
	}
	
	@Bean
    public MongoCustomConversions customConversions() {
        final List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(new MongoLocalDateTimeFromDateConverter());
        converterList.add(new MongoLocalDateTimeToDateConverter());
        return new MongoCustomConversions(converterList);
    }


	@Bean
	public SequenceGenerator sequenceGenerator() {
		return new SequenceGenerator();
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}
	
	@Bean
	public HashMap<String, Object> getConfigProperties(){
		HashMap<String, Object> configProperties = new LinkedHashMap<String, Object>();
		return configProperties;
	}
	
	@Bean
	public ObjectWriter jsonWriter() {
		ObjectWriter jsow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return jsow;
	}
	
	@Bean("db1MongoTemplate")
	//@ConditionalOnExpression("#{ 'db1mongoURI' != '' }")
	@ConditionalOnExpression("!T(org.springframework.util.StringUtils).isEmpty('${db1.mongodb.uri:}')")
    public MongoTemplate db1MongoTemplate() throws Exception {
		@SuppressWarnings("deprecation")
		SimpleMongoDbFactory dbFactory = new SimpleMongoDbFactory(new MongoClientURI(db1mongoURI));
		return new MongoTemplate(dbFactory);
    }

	
}
