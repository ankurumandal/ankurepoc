package com.ascent.autobcm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EntityScan("com.ascent.autobcm.model")
@ComponentScan({ "com.ascent.autobcm.controller", "com.ascent.autobcm.dao", "com.ascent.autobcm.model",
		"com.ascent.autobcm.service", "com.ascent.scheduler", "com.ascent.configuration" })
@EnableJpaRepositories("com.ascent.autobcm.dao")
@EnableSwagger2
@EnableScheduling
public class BcmRbacApplication {

	public static void main(String[] args) {
		SpringApplication.run(BcmRbacApplication.class, args);
		System.out.println("Ankur");
	}

}
