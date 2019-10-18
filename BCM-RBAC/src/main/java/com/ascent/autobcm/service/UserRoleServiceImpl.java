package com.ascent.autobcm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.UserRoleRepository;
import com.ascent.autobcm.exception.NoSuchUserRoleFoundException;
import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.model.UserRole;
import com.ascent.autobcm.util.Constants;

@Service
public class UserRoleServiceImpl implements UserRoleService {

	@Autowired
	UserRoleRepository userRoleRepository;

	@Override
	public UserRole saveUserRole(UserRole userRoleMappingToPersist) {
		UserRole persistedInDb = userRoleRepository.save(userRoleMappingToPersist);
		return persistedInDb;
	}

	@Override
	public List<UserRole> findByUserId(long employeeId) {
		List<UserRole> roleMappings = userRoleRepository.findByUserId(employeeId);
		return roleMappings;
	}

	@Override
	public boolean deleteExistingMappings(List<UserRole> existingMapping) {
		userRoleRepository.deleteAll(existingMapping);
		return true;
	}

	@Override
	public boolean findAndDeleteExistingMappings(long employeeId) {
		List<UserRole> existingMappingForRole = userRoleRepository.findByUserId(employeeId);
		if (existingMappingForRole.size() > 0) {
			userRoleRepository.deleteAll(existingMappingForRole);
			return true;
		}
		return false;
	}

	@Override
	public List<UserRole> saveUserRoleMappings(List<UserRole> existingRoleMappings) {
		return userRoleRepository.saveAll(existingRoleMappings);
	}

	@Override
	public List<UserRole> findAllByActive(String activeStatus) {
		List<UserRole> userRoleList = userRoleRepository.findByActive(activeStatus);
		return userRoleList;
	}

	@Override
	public List<User> findByRoleAssigned(long roleId) {
		List<User> usersAssignedForRole = userRoleRepository.findByRoleAssigned(roleId);
		return usersAssignedForRole;
	}

	@Override
	public List<Role> findByUserAssigned(long userId) {
		List<Role> rolesAssignedToUser = userRoleRepository.findByUserAssigned(userId);
		return rolesAssignedToUser;
	}

	@Override
	public List<UserRole> findByRoleRoleName(String roleName) {
		List<UserRole> rolesAssignedToUser = userRoleRepository.findByRoleRoleName(roleName);
		return rolesAssignedToUser;
	}

	@Override
	public List<UserRole> findByRoleIdInAndUserIdIn(List<Long> roleIds, List<Long> userIds)
			throws NoSuchUserRoleFoundException {
		List<UserRole> existingUserRoleMappings = null;
		existingUserRoleMappings = userRoleRepository.findByRoleIdInAndUserIdIn(roleIds, userIds);
		if (null == existingUserRoleMappings || existingUserRoleMappings.size() == 0)
			throw new NoSuchUserRoleFoundException(Constants.NO_SUCH_USER_ROLE_FOUND);

		return existingUserRoleMappings;
	}

	@Override
	public List<UserRole> findByRoleIdInAndUserIdInAndActive(List<Long> roleIds, List<Long> userIds, String active)
			throws NoSuchUserRoleFoundException {
		List<UserRole> existingUserRoleMappings = null;
		existingUserRoleMappings = userRoleRepository.findByRoleIdInAndUserIdInAndActive(roleIds, userIds, active);
		if (null == existingUserRoleMappings || existingUserRoleMappings.size() == 0)
			throw new NoSuchUserRoleFoundException(Constants.NO_SUCH_USER_ROLE_FOUND);
		return existingUserRoleMappings;
	}

}
