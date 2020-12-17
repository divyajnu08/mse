package com.virtue.csr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CSRServerApplication extends SpringBootServletInitializer {
	   @Override
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	      return application.sources(CSRServerApplication.class);
	   }
	   public static void main(String[] args) {
	      SpringApplication.run(CSRServerApplication.class, args);
	   }
	}