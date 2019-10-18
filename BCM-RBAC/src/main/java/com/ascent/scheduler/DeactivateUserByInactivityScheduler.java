package com.ascent.scheduler;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.ascent.autobcm.exception.EmployeeIdAlreadyPresentException;
import com.ascent.autobcm.exception.ImproperEmailFormatException;
import com.ascent.autobcm.exception.ImproperPasswordException;
import com.ascent.autobcm.exception.NoSuchUserFoundException;
import com.ascent.autobcm.exception.NoUsersFoundForInactivity;
import com.ascent.autobcm.exception.UserNameAlreadyPresent;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.service.UserService;
import com.ascent.autobcm.util.Constants;
import com.ascent.autobcm.util.NotificationConstants;

@Component
public class DeactivateUserByInactivityScheduler {

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

	private static final Logger LOGGER = LogManager.getLogger(DeactivateUserByInactivityScheduler.class);

	@Scheduled(cron = "0 53 17 * * ?")
	public void cronJobSch() {
		List<User> inactiveUsers = null;

		try {
			inactiveUsers = userService.findUsersByInactivity(NotificationConstants.MINIMUM_INACTIVITY_PERIOD);

			if (null != inactiveUsers && inactiveUsers.size() > 0) {

				for (User currentUser : inactiveUsers) {
					Map<String, Object> model = new HashMap<String, Object>();
					model.put("personName", currentUser.getFirstName() + Constants.SPACE + currentUser.getLastName());
					model.put("resetLink", Constants.APPLICATION_URL + Constants.APPLICATION_CONTEXT
							+ Constants.ACTIVATE_USER + "/" + currentUser.getId());
					model.put("gracePeriod", NotificationConstants.GRACE_PERIOD_FOR_PASSWORD_CHANGE);
					sendPasswordChangeNotification(model, currentUser.getEmailId(),
							NotificationConstants.PASSWORD_RESET_GRACE_PERIOD_SUBJECT);
					currentUser.setActive(Constants.INACTIVE);
					userService.updateUser(currentUser);
				}

			}
		} catch (ImproperEmailFormatException e) {
			LOGGER.error(Constants.BLANK, e);
		} catch (UserNameAlreadyPresent e) {
			LOGGER.error(Constants.BLANK, e);
		} catch (EmployeeIdAlreadyPresentException e) {
			LOGGER.error(Constants.BLANK, e);
		} catch (NoSuchUserFoundException e) {
			LOGGER.error(Constants.BLANK, e);
		} catch (ImproperPasswordException e) {
			LOGGER.error(Constants.BLANK, e);
		} catch (NoUsersFoundForInactivity e) {
			LOGGER.error(Constants.BLANK, e);
		}

		LOGGER.info("Called");
	}

	private void sendPasswordChangeNotification(Map<String, Object> model, String toContacts, String purpose)
			throws ImproperEmailFormatException {
		MimeMessage mimeMessage = officialEmailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = null;

		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			if (null != toContacts && !toContacts.isEmpty()) {
				boolean isVerified = emailValidation(toContacts);

				if (isVerified)
					mimeMessageHelper.setTo(toContacts);
				else
					throw new ImproperEmailFormatException(NotificationConstants.EMAIL_REGEX_VALIDATION);
			}

			mimeMessageHelper.setSubject(NotificationConstants.USER_INACTIVITY_DEACTIVE_SUBJECT);
			mimeMessageHelper.setFrom(NotificationConstants.MY_OFFICIAL_EMAIL);
			mimeMessageHelper.setText(geContentFromTemplate(model, purpose), true);
		} catch (MessagingException e) {
			LOGGER.error(Constants.BLANK, e);
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

		StringBuffer content = new StringBuffer();
		try {
			content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
					NotificationConstants.USER_INACTIVITY_DEACTIVE_TEMPLATE, model));
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
		}
		return content.toString();
	}

}
