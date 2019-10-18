package com.ascent.autobcm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ascent.autobcm.exception.EmployeeIdAlreadyPresentException;
import com.ascent.autobcm.exception.ImproperPasswordException;
import com.ascent.autobcm.exception.NoSuchUserFoundException;
import com.ascent.autobcm.exception.NoUsersFoundForInactivity;
import com.ascent.autobcm.exception.NoUsersFoundForPasswordChange;
import com.ascent.autobcm.exception.SystemAdminIntegrityCheckException;
import com.ascent.autobcm.exception.UserNameAlreadyPresent;
import com.ascent.autobcm.model.User;

@Service
public interface UserService {

	public User saveUser(User userObjToPersist)
			throws UserNameAlreadyPresent, EmployeeIdAlreadyPresentException, ImproperPasswordException;

	public User updateUser(User userObjToPersist) throws UserNameAlreadyPresent, EmployeeIdAlreadyPresentException,
			NoSuchUserFoundException, ImproperPasswordException;

	public User activateOrDeactivateUser(User userObjToPersist);

	public User findByUserName(String userName) throws NoSuchUserFoundException;

	public User findByEmployeeId(String employeeId);

	public boolean deleteByEmployeeId(long employeeId);

	public boolean deleteUserEntity(User userToDelete);

	public List<User> findAllByActive(String activeStatus);

	public User findById(long id) throws NoSuchUserFoundException;

	public User deleteUser(long userId) throws NoSuchUserFoundException, SystemAdminIntegrityCheckException;

	public User reactivateUser(long userId) throws NoSuchUserFoundException;

	public List<User> findUsersForPasswordChange(long days) throws NoUsersFoundForPasswordChange;
	
	public List<User> findUsersByInactivity(int days) throws NoUsersFoundForInactivity ;
}
