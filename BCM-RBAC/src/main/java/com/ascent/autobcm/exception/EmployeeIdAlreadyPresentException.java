package com.ascent.autobcm.exception;

@SuppressWarnings("serial")
public class EmployeeIdAlreadyPresentException extends Exception {

	public EmployeeIdAlreadyPresentException(String exceptionMessage) {
		super(exceptionMessage);
	}

}
