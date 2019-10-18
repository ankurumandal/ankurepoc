package com.ascent.autobcm.util;

public class NotificationConstants {

	public static final String MY_EMAIL = "ankurr.mandal@gmail.com";

	public static final String MY_PASSWORD = "3988318295Aa!";

	public static final String FRIEND_EMAIL = "dolly.b@ascentbusiness.com";

	public static final String MY_OFFICIAL_EMAIL = "dolly.b@ascentbusiness.com";

	public static final String MY_OFFICIAL_PASSWORD = "dolly.b321?";

	public static final String EMAIL_REGEX_VALIDATION = "^(.+)@(.+)$";

	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String IMPROPER_EMAIL_FORMAT = "Incorrect Email Id Provided";

	public static final String USER_NAME = "AscentDemo";
	public static final String PASSWORD = "P@ssw0rd";

	public static final String FORCE_CHANGE_PASSWORD = "ForceChangePassword";
	public static final String FORCE_CHANGE_PASSWORD_SUBJECT = "Reminder For Password Change";
	public static final String FORCE_CHANGE_PASSWORD_TEMPLATE = "/templates/forcePasswordChange.vm";

	public static final String ADMIN_RESET_PASSWORD = "AdminResetPassword";
	public static final String ADMIN_RESET_PASSWORD_SUBJECT = "Post Process After Password Reset";
	public static final String ADMIN_RESET_PASSWORD_TEMPLATE = "/templates/adminPasswordReset.vm";

	public static final String PASSWORD_RESET_GRACE_PERIOD = "GracePeriodResetPassword";
	public static final String PASSWORD_RESET_GRACE_PERIOD_SUBJECT = "Password Change At The End Of Grace Period";
	public static final String PASSWORD_RESET_GRACE_PERIOD_TEMPLATE = "/templates/gracePeriodPasswordReset.vm";

	public static final String USER_INACTIVITY_DEACTIVE = "GracePeriodResetPassword";
	public static final String USER_INACTIVITY_DEACTIVE_SUBJECT = "User Inactivity";
	public static final String USER_INACTIVITY_DEACTIVE_TEMPLATE = "/templates/deactivateUserInactivity.vm";

	public static int GRACE_PERIOD_FOR_PASSWORD_CHANGE = 10;
	public static int MINIMUM_INACTIVITY_PERIOD = 10;
}
