/**
 * 
 */
package com.virtue.csr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 *
 */
@Configuration
@EnableMongoRepositories(basePackages = {"com.virtue.csr.customer"}, mongoTemplateRef="mongoTemplateCSR")
public class CsrConfig {
	
	@Value("${spring.csr.mongodb.uri}")
	String csrDb_url;
	
	@Autowired
	MongoCustomConversions customConversions;
	
	@Bean("mongoTemplateCSR")
	public MongoTemplate mongoTemplateCSR(MongoMappingContext context) throws Exception {
		MongoDbFactory mongoDbFactory = new SimpleMongoClientDbFactory(csrDb_url);
		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		converter.setCustomConversions(customConversions);
	    converter.afterPropertiesSet();
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, converter);
		return mongoTemplate;
	}

}
