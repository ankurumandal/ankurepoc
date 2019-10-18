package com.ascent.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.ascent.autobcm.exception.ImproperEmailFormatException;
import com.ascent.autobcm.exception.NoUsersFoundForPasswordChange;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.service.UserService;
import com.ascent.autobcm.util.Constants;
import com.ascent.autobcm.util.NotificationConstants;

@Component
public class ForceChangePasswordScheduler {

	private static final Logger LOGGER = LogManager.getLogger(ForceChangePasswordScheduler.class);

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

	// MIN HOUR DOM MON DOW CMD
	// Minute Field Hour Field Day Of Month Month Field Day Of Week Command
	@Scheduled(cron = "0 22 16 * * ?")
//	@Scheduled(fixedRate = 10000000)
	public void cronJobSch() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);
		List<User> usersForPasswordChange = null;

		try {
			usersForPasswordChange = userService.findUsersForPasswordChange(10);

			if (null != usersForPasswordChange && usersForPasswordChange.size() > 0) {

				for (User currentUser : usersForPasswordChange) {
					Map<String, Object> model = new HashMap<String, Object>();
					model.put("personName", currentUser.getFirstName() + Constants.SPACE + currentUser.getLastName());
					model.put("resetLink", Constants.APPLICATION_URL + Constants.APPLICATION_CONTEXT
							+ Constants.PASSWORD_RESET + "/" + currentUser.getUserName());
					model.put("gracePeriod", NotificationConstants.GRACE_PERIOD_FOR_PASSWORD_CHANGE);
					sendPasswordChangeNotification(model, currentUser.getEmailId(),
							NotificationConstants.PASSWORD_RESET_GRACE_PERIOD_SUBJECT);
				}

			}
		} catch (NoUsersFoundForPasswordChange e) {
			LOGGER.error(Constants.BLANK, e);
		} catch (ImproperEmailFormatException e) {
			LOGGER.error(Constants.BLANK, e);
		}

		LOGGER.info("Called");
		System.out.println("Java cron job expression:: " + strDate);
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

			mimeMessageHelper.setSubject(NotificationConstants.PASSWORD_RESET_GRACE_PERIOD_SUBJECT);
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
					NotificationConstants.PASSWORD_RESET_GRACE_PERIOD_TEMPLATE, model));
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
		}
		return content.toString();
	}
}
