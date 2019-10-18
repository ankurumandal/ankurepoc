package com.ascent.autobcm.util;

public class Constants {

	public static final String ACTIVE = "Y";
	public static final String INACTIVE = "N";

	public static final String BLANK = "";
	public static final String SPACE = " ";

	public static final String CUSTOM_IDENTIFIER_QUERY = "select count(*) as totalCount from users";

	public static final String TARGETED_COLUMN = "totalCount";

	public static final String EMPLOYEE_ID_PRESENT = "User Already Present with Employee Id. There Cannot Be Duplicate Employee Id :";
	public static final String ROLE_ALREADY_PRESENT = "Role Already Present. There Cannot Be Duplicate Role :";
	public static final String OPERATION_ALREADY_PRESENT = "Operation Already Present. There Cannot Be Duplicate Operation :";
	public static final String GROUP_ALREADY_PRESENT = "Group Already Present. There Cannot Be Duplicate Group :";
	public static final String USER_NAME_ALREADY_PRESENT = "User Already Present with UserName. There Cannot Be Duplicate UserName :";
	public static final String ENTITY_TYPE_ALREADY_PRESENT = "Entity Type Already Present. There Should Not Be Duplicate Value.";
	public static final String ENTITY_TYPE_ATTRIBUTE_DEFINITION_PRESENT = "Entity Type Attribute Definition Already Present. There Should Not Be Duplicate Value.";
	public static final String ENTITY_ALREADY_PRESENT = "Entity Already Present. There Should Not Be Duplicate Value.";
	public static final String ENTITY_ATTRIBUTE_VALUE_ALREADY_PRESENT = "Entity Attribute Value Already Present. There Should Not Be Duplicate Value.";

	public static final String NO_USER_FOUND = "No Such User Found.";
	public static final String NO_ROLE_FOUND = "No Such Role Found.";
	public static final String NO_OPERATION_FOUND = "No Such Operation Found.";
	public static final String NO_GROUP_FOUND = "No Such Group Found.";
	public static final String NO_ENTITY_TYPE_FOUND = "No Such Entity Type Found.";
	public static final String NO_ENTITY_TYPE_ATTRIBUTE_DEFINITION_FOUND = "No Such Entity Type Attribute Definition Found.";
	public static final String NO_ENTITY_FOUND = "No Such Entity Type Found.";
	public static final String NO_ENTITY_ATTRIBUTE_VALUE_FOUND = "No Such Entity Attribute Value Found.";

	public static final String IMPROPER_PARAMETERS_PASSED = "Improper Parameters Passed. Please recheck the request parameters.";
	public static final String SYSTEM_ADMIN_INTEGRITY_CHECK = "There has to be atleast one System Admin that needs to be active.";
	public static final String NO_SUCH_USER_ROLE_FOUND = "No Such User Role Found";
	public static final String USER_NAME_PASSWORD_MATCH = "Password cannot be as same as the Username.";

//	public static final String ALPHANUMERIC_PASSWORD_CHECK = "^(?!.*([A-Za-z0-9!@#$%^&*()_-])\1{2})(?=.*[A-Za-z])(?=.*[!@#$%^&*()_-])(?=.*\d)[A-Za-z0-9!@#$%^&*()_-]+$";

	public static final String ALPHANUMERIC_PASSWORD_CHECK = "^(?!.*([A-Za-z0-9!@#$%^&*()_-])\1{2})(?=.*[A-Za-z])(?=.*[!@#$%^&*()_-])(?=.*\\d)[A-Za-z0-9!@#$%^&*()_-]+$";

	public static final String ALPHANUMERIC_SPECIAL_CHARACTERS_PASSWORD_REQUIRED = "Alphanumeric Password Required Along with Atleast One Special Character And Not More Than Two Repeated Characters";
	public static final String PASSWORD_LENGTH = "8";
	public static final String INCORRECT_PASSWORD_LENGTH = "Password length should minimum be " + PASSWORD_LENGTH;

	public static final String LOGIN_SUCCESSFUL = "SuccessFully Logged In";
	public static final String INVALID_CREDENTIALS_PROVIDED = "The Password Is Incorrect. Please Log-In Again With Proper Credentials";

	public static final String APPLICATION_URL = "http://192.168.1.72";
	public static final String APPLICATION_CONTEXT = "/";

	public static final String PASSWORD_RESET = "resetPassword/";
	public static final String ACTIVATE_USER = "activateUser/";
	public static final String CHANGE_THE_PASSWORD = "Please Change The Password And Then Log In";

	public static final String YES = "Y";
	public static final String NO = "N";

	public static final String NO_USERS_FOUND_FOR_PASSWORD_CHANGE = "No Such Users Under Grace Period For Password Expiry";
	public static final String NO_USERS_FOUND_FOR_INACTIVITY = "No Such Users Found Who Were Inactive From Long Time";
	
	public static final String PERMISSION_ALREADY_PRESENT = "Permission Already Present";
	public static final String NO_PERMISSION_FOUND = "No Such Permission Found";

}
