package com.ascent.autobcm.model;

public enum RoleType {

	USER("USER"), DBA("DBA"), Admin("Admin"),  SuperAdmin("SuperAdmin");

	String roleType;

	private RoleType(String userProfileType) {
		this.roleType = userProfileType;
	}

	public String getRoleType() {
		return roleType;
	}
}
