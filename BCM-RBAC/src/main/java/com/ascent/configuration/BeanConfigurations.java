package com.ascent.configuration;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineFactory;

import com.ascent.autobcm.util.NotificationConstants;

@Component
public class BeanConfigurations {

	@Bean(name = "unOfficialAccount")
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername(NotificationConstants.MY_EMAIL);
		mailSender.setPassword(NotificationConstants.MY_PASSWORD);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

	@Bean(name = "officialAccount")
	public JavaMailSender getJavaMailSenderSMTP() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("email.ascentbusiness.com");
		mailSender.setPort(587);

		mailSender.setUsername(NotificationConstants.MY_OFFICIAL_EMAIL);
		mailSender.setPassword(NotificationConstants.MY_OFFICIAL_PASSWORD);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

	@Bean
	public VelocityEngine getVelocityEngine() throws VelocityException, IOException {
		VelocityEngineFactory velocityEngineFactory = new VelocityEngineFactory();
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		velocityEngineFactory.setVelocityProperties(props);
		return velocityEngineFactory.createVelocityEngine();
	}
	
	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(20);
		return taskScheduler;
	}

}
