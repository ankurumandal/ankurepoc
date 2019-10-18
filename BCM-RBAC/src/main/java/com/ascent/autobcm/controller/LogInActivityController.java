package com.ascent.autobcm.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ascent.autobcm.exception.EmployeeIdAlreadyPresentException;
import com.ascent.autobcm.exception.ImproperEmailFormatException;
import com.ascent.autobcm.exception.ImproperPasswordException;
import com.ascent.autobcm.exception.IncorrectCredentialsProvidedException;
import com.ascent.autobcm.exception.NoSuchUserFoundException;
import com.ascent.autobcm.exception.UserNameAlreadyPresent;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.service.UserService;
import com.ascent.autobcm.util.Constants;
import com.ascent.autobcm.util.NotificationConstants;

@RestController
@RequestMapping("rest/login/")
public class LogInActivityController {

	private static final Logger LOGGER = LogManager.getLogger(LogInActivityController.class);

	@Autowired
	UserService userService;

	@Autowired
	@Qualifier("unOfficialAccount")
	private JavaMailSender emailSender;

	@Autowired
	@Qualifier("officialAccount")
	private JavaMailSender officialEmailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	@PostMapping("/authenticateuser/{userName}/{password}")
	public ResponseEntity<String> loginUser(@PathVariable("userName") String userName,
			@PathVariable("password") String password) throws Exception {
		User userFromDb = null;
		try {
			userFromDb = userService.findByUserName(userName);
			if (Constants.ACTIVE.equalsIgnoreCase(userFromDb.getUserToPasswordReset())) {

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("personName", userFromDb.getFirstName() + Constants.SPACE + userFromDb.getLastName());
				model.put("resetLink", Constants.APPLICATION_URL + Constants.APPLICATION_CONTEXT
						+ Constants.PASSWORD_RESET + "/" + userFromDb.getUserName());

				sendPasswordChangeNotification(model, userFromDb.getEmailId(),
						NotificationConstants.FORCE_CHANGE_PASSWORD);

				userFromDb.setUserToPasswordReset(Constants.INACTIVE);
				userService.updateUser(userFromDb);
				return ResponseEntity.ok(Constants.CHANGE_THE_PASSWORD);

			} else if (userFromDb.getAdminPasswordReset().equals(Constants.YES)) {
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("personName", userFromDb.getFirstName() + Constants.SPACE + userFromDb.getLastName());
				model.put("resetLink", Constants.APPLICATION_URL + Constants.APPLICATION_CONTEXT
						+ Constants.PASSWORD_RESET + "/" + userFromDb.getUserName());

				sendPasswordChangeNotification(model, userFromDb.getEmailId(),
						NotificationConstants.ADMIN_RESET_PASSWORD);

				userFromDb.setAdminPasswordReset(Constants.NO);
				userService.updateUser(userFromDb);
				return ResponseEntity.ok(Constants.CHANGE_THE_PASSWORD);

			} else if (userFromDb.getPassword().equals(password)) {

				Date currentInstance = new Date();
				LocalDateTime loginDateTime = LocalDateTime.ofInstant(currentInstance.toInstant(),
						ZoneId.systemDefault());

				userFromDb.setLastLoginDate(loginDateTime);
				userService.updateUser(userFromDb);

				return ResponseEntity.ok(Constants.LOGIN_SUCCESSFUL);
			} else
				throw new IncorrectCredentialsProvidedException(Constants.INVALID_CREDENTIALS_PROVIDED);

		} catch (NoSuchUserFoundException | IncorrectCredentialsProvidedException e) {

			if (e instanceof IncorrectCredentialsProvidedException) {
				long countOfIncorrectLogin = userFromDb.getWrongCredentialsLogin();
				userFromDb.setWrongCredentialsLogin(countOfIncorrectLogin + 1);
				try {
					userService.updateUser(userFromDb);
				} catch (UserNameAlreadyPresent | EmployeeIdAlreadyPresentException | ImproperPasswordException e1) {
					LOGGER.error(Constants.BLANK, e);
					throw e;
				}

			}
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		return ResponseEntity.ok(Constants.LOGIN_SUCCESSFUL);

	}

	private void sendPasswordChangeNotification(Map<String, Object> model, String toContacts, String purpose)
			throws ImproperEmailFormatException {
		MimeMessage mimeMessage = officialEmailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = null;

		Map<String, String> subjectMap = new HashMap<String, String>();
		subjectMap.put(NotificationConstants.FORCE_CHANGE_PASSWORD,
				NotificationConstants.FORCE_CHANGE_PASSWORD_SUBJECT);
		subjectMap.put(NotificationConstants.ADMIN_RESET_PASSWORD, NotificationConstants.ADMIN_RESET_PASSWORD_SUBJECT);

		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			if (null != toContacts && !toContacts.isEmpty()) {
				boolean isVerified = emailValidation(toContacts);

				if (isVerified)
					mimeMessageHelper.setTo(toContacts);
				else
					throw new ImproperEmailFormatException(NotificationConstants.EMAIL_REGEX_VALIDATION);
			}

			mimeMessageHelper.setSubject(subjectMap.get(purpose));
			mimeMessageHelper.setFrom(NotificationConstants.MY_OFFICIAL_EMAIL);
			mimeMessageHelper.setText(geContentFromTemplate(model, purpose), true);
		} catch (MessagingException e) {

		}

		this.officialEmailSender.send(mimeMessageHelper.getMimeMessage());
	}

	private boolean emailValidation(String commaSeparatedEmails) {

		Pattern pattern = Pattern.compile(NotificationConstants.EMAIL_PATTERN);
		String[] emailsArray = commaSeparatedEmails.split(",");

		for (int i = 0; i < emailsArray.length; i++) {
			Matcher matcher = pattern.matcher(emailsArray[i]);
			if (!matcher.matches())
				return false;
		}

		return true;

	}

	private String geContentFromTemplate(Map<String, Object> model, String purpose) {
		Map<String, String> templateMap = new HashMap<String, String>();
		templateMap.put(NotificationConstants.FORCE_CHANGE_PASSWORD,
				NotificationConstants.FORCE_CHANGE_PASSWORD_TEMPLATE);
		templateMap.put(NotificationConstants.ADMIN_RESET_PASSWORD,
				NotificationConstants.ADMIN_RESET_PASSWORD_TEMPLATE);

		StringBuffer content = new StringBuffer();
		try {
			content.append(
					VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateMap.get(purpose), model));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}
