package com.ascent.autobcm.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.UserGroupRepository;
import com.ascent.autobcm.dao.UserRepository;
import com.ascent.autobcm.dao.UserRoleRepository;
import com.ascent.autobcm.exception.EmployeeIdAlreadyPresentException;
import com.ascent.autobcm.exception.ImproperPasswordException;
import com.ascent.autobcm.exception.NoSuchUserFoundException;
import com.ascent.autobcm.exception.NoUsersFoundForInactivity;
import com.ascent.autobcm.exception.NoUsersFoundForPasswordChange;
import com.ascent.autobcm.exception.SystemAdminIntegrityCheckException;
import com.ascent.autobcm.exception.UserNameAlreadyPresent;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.model.UserGroup;
import com.ascent.autobcm.model.UserRole;
import com.ascent.autobcm.util.Constants;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Override
	public User saveUser(User userObjToPersist)
			throws UserNameAlreadyPresent, EmployeeIdAlreadyPresentException, ImproperPasswordException {
		User user = null;
		boolean isProperPassword;

		user = userRepository.findByEmployeeId(userObjToPersist.getEmployeeId());

		if (null != user)
			throw new EmployeeIdAlreadyPresentException(
					Constants.EMPLOYEE_ID_PRESENT + userObjToPersist.getEmployeeId());

		user = userRepository.findByUserName(userObjToPersist.getUserName());

		if (null != user)
			throw new UserNameAlreadyPresent(Constants.USER_NAME_ALREADY_PRESENT);

		if (userObjToPersist.getUserName().equals(userObjToPersist.getPassword()))
			throw new ImproperPasswordException(Constants.USER_NAME_PASSWORD_MATCH);

		Pattern alphaNumPattern = Pattern.compile(Constants.ALPHANUMERIC_PASSWORD_CHECK);
		Matcher matcher = alphaNumPattern.matcher(userObjToPersist.getPassword());
		isProperPassword = matcher.matches();

		if (!isProperPassword)
			throw new ImproperPasswordException(Constants.ALPHANUMERIC_SPECIAL_CHARACTERS_PASSWORD_REQUIRED);

		userRepository.save(userObjToPersist);
		return user;
	}

	@Override
	public User findByEmployeeId(String employeeId) {
		User userFromDb = userRepository.findByEmployeeId(employeeId);
		return userFromDb;
	}

	@Override
	public boolean deleteByEmployeeId(long employeeId) {
		userRepository.deleteByEmployeeId(employeeId);
		return true;
	}

	@Override
	public boolean deleteUserEntity(User userToDelete) {
		userRepository.delete(userToDelete);
		return true;
	}

	@Override
	public List<User> findAllByActive(String activeStatus) {
		List<User> userList = userRepository.findByActive(activeStatus);
		return userList;
	}

	@Override
	public User findByUserName(String userName) throws NoSuchUserFoundException {
		User userFromDb = null;

		userFromDb = userRepository.findByUserName(userName);
		if (null == userFromDb)
			throw new NoSuchUserFoundException(Constants.NO_USER_FOUND);
		return userFromDb;
	}

	@Override
	public User findById(long id) throws NoSuchUserFoundException {
		User userFromDb = null;

		userFromDb = userRepository.findById(id);
		if (null == userFromDb)
			throw new NoSuchUserFoundException(Constants.NO_USER_FOUND);
		return userFromDb;
	}

	@Override
	public User updateUser(User userObjToPersist) throws UserNameAlreadyPresent, EmployeeIdAlreadyPresentException,
			NoSuchUserFoundException, ImproperPasswordException {
		User user = null;
		boolean isProperPassword;

		user = userRepository.findById(userObjToPersist.getId());

		if (null == user)
			throw new NoSuchUserFoundException(Constants.NO_USER_FOUND);

		user = userRepository.findByEmployeeId(userObjToPersist.getEmployeeId());

		if (null != user && user.getId() != userObjToPersist.getId())
			throw new EmployeeIdAlreadyPresentException(
					Constants.EMPLOYEE_ID_PRESENT + userObjToPersist.getEmployeeId());

		user = userRepository.findByUserName(userObjToPersist.getUserName());

		if (null != user && user.getId() != userObjToPersist.getId())
			throw new UserNameAlreadyPresent(Constants.USER_NAME_ALREADY_PRESENT);

		if (userObjToPersist.getUserName().equals(userObjToPersist.getPassword()))
			throw new ImproperPasswordException(Constants.USER_NAME_PASSWORD_MATCH);

		Pattern alphaNumPattern = Pattern.compile(Constants.ALPHANUMERIC_PASSWORD_CHECK);
		Matcher matcher = alphaNumPattern.matcher(userObjToPersist.getPassword());
		isProperPassword = matcher.matches();

		if (!isProperPassword)
			throw new ImproperPasswordException(Constants.ALPHANUMERIC_SPECIAL_CHARACTERS_PASSWORD_REQUIRED);

		userRepository.save(userObjToPersist);

		return userObjToPersist;
	}

	@Override
	public User activateOrDeactivateUser(User userObjToPersist) {
		User persistedUser = userRepository.save(userObjToPersist);
		return persistedUser;
	}

	@Override
	public User deleteUser(long userId) throws SystemAdminIntegrityCheckException, NoSuchUserFoundException {
		User userFromDb = null;
		boolean toCheckSystemAdminCheck = false;

		userFromDb = userRepository.findById(userId);
		if (null == userFromDb)
			throw new NoSuchUserFoundException(Constants.NO_USER_FOUND);

		List<UserRole> superAdminCheck = userRoleRepository.findByRoleRoleName("SystemAdmin");
		if (null != superAdminCheck && superAdminCheck.size() > 0) {
			for (int i = 0; i < superAdminCheck.size(); i++) {
				if (userFromDb.getId() == superAdminCheck.get(i).getUser().getId())
					toCheckSystemAdminCheck = true;
			}
		}

		if (toCheckSystemAdminCheck) {
			if (superAdminCheck.size() > 1) {

				List<UserRole> correspondingUserRoleMappings = userRoleRepository.findByUserId(userId);
				if (correspondingUserRoleMappings.size() > 0) {
					correspondingUserRoleMappings.forEach(item -> item.setActive(Constants.INACTIVE));
					userRoleRepository.saveAll(correspondingUserRoleMappings);
				}

				userFromDb.setActive(Constants.INACTIVE);
				userFromDb = userRepository.save(userFromDb);
				return userFromDb;
			} else {
				throw new SystemAdminIntegrityCheckException(Constants.SYSTEM_ADMIN_INTEGRITY_CHECK);
			}
		}

		// Disabling User Role Mapping
		List<UserRole> existingUserRoleMappings = userRoleRepository.findByUserId(userId);
		if (existingUserRoleMappings.size() > 0) {
			existingUserRoleMappings.stream().forEach(item -> item.setActive(Constants.INACTIVE));
		}
		userRoleRepository.saveAll(existingUserRoleMappings);

		// Disabling User Group Mapping
		List<UserGroup> existingUserGroupMappings = userGroupRepository.findByUserId(userId);
		if (existingUserGroupMappings.size() > 0) {
			existingUserGroupMappings.stream().forEach(item -> item.setActive(Constants.INACTIVE));
		}
		userGroupRepository.saveAll(existingUserGroupMappings);

		userFromDb.setActive(Constants.INACTIVE);
		userFromDb = userRepository.save(userFromDb);
		return userFromDb;

	}

	@Override
	public User reactivateUser(long userId) throws NoSuchUserFoundException {
		User userFromDb = null;

		userFromDb = userRepository.findById(userId);
		if (null == userFromDb)
			throw new NoSuchUserFoundException(Constants.NO_USER_FOUND);

		List<UserRole> correspondingUserRoleMappings = userRoleRepository.findByUserIdAndActive(userId,
				Constants.INACTIVE);
		if (correspondingUserRoleMappings.size() > 0) {
			correspondingUserRoleMappings.forEach(item -> item.setActive(Constants.ACTIVE));
			userRoleRepository.saveAll(correspondingUserRoleMappings);
		}

		// Disabling User Group Mapping
		List<UserGroup> existingUserGroupMappings = userGroupRepository.findByUserIdAndActive(userId,
				Constants.INACTIVE);
		if (existingUserGroupMappings.size() > 0) {
			existingUserGroupMappings.stream().forEach(item -> item.setActive(Constants.ACTIVE));
		}
		userGroupRepository.saveAll(existingUserGroupMappings);

		userFromDb.setActive(Constants.ACTIVE);
		userFromDb = userRepository.save(userFromDb);
		return userFromDb;

	}

	@Override
	public List<User> findUsersForPasswordChange(long days) throws NoUsersFoundForPasswordChange {
		List<User> usersForPasswordChange = null;

		usersForPasswordChange = userRepository.findUsersForPasswordChange(10);
		if (null == usersForPasswordChange)
			throw new NoUsersFoundForPasswordChange(Constants.NO_USERS_FOUND_FOR_PASSWORD_CHANGE);
		return usersForPasswordChange;
	}

	@Override
	public List<User> findUsersByInactivity(int days) throws NoUsersFoundForInactivity {
		List<User> userDueToInactivity = null;

		userDueToInactivity = userRepository.findUsersByInactivity(days);

		if (null == userDueToInactivity)
			throw new NoUsersFoundForInactivity(Constants.NO_USERS_FOUND_FOR_INACTIVITY);
		return userDueToInactivity;
	}

}
