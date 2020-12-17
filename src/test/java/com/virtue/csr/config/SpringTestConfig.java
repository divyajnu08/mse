package com.virtue.csr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.MongoClientURI;

//@PropertySource("classpath:application-${spring.profiles.active}.properties")
@EnableAutoConfiguration
public class SpringTestConfig extends SpringConfig{
	
	//@Value("${spring.data.mongodb.uri}")
	//private String mongoURI="mongodb+srv://csr_dev:9re4WvUweq8lhfjE@devc1.zqsiv.mongodb.net/WEP_CSR_DEVL";
	
	//public MongoDbFactory mongoDbFactory() {
    //    return new SimpleMongoDbFactory(new MongoClientURI(mongoURI));
    //}
	
	//@Bean
    //public MongoTemplate mongoTemplate() throws Exception {
    //    return new MongoTemplate(mongoDbFactory());
    //}
}
