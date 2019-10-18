package com.ascent.autobcm.service;

import java.util.List;

import com.ascent.autobcm.exception.NoSuchUserRoleFoundException;
import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.model.UserRole;

public interface UserRoleService {

	public UserRole saveUserRole(UserRole userRoleMapping);

	public List<UserRole> findByUserId(long employeeId);

	public boolean deleteExistingMappings(List<UserRole> existingMapping);

	public boolean findAndDeleteExistingMappings(long employeeId);

	public List<UserRole> saveUserRoleMappings(List<UserRole> existingRoleMappings);

	public List<UserRole> findAllByActive(String activeStatus);

	public List<User> findByRoleAssigned(long roleId);

	public List<Role> findByUserAssigned(long userId);

	public List<UserRole> findByRoleRoleName(String roleName);

	public List<UserRole> findByRoleIdInAndUserIdIn(List<Long> roleIds, List<Long> userIds)
			throws NoSuchUserRoleFoundException;

	public List<UserRole> findByRoleIdInAndUserIdInAndActive(List<Long> roleIds, List<Long> userIds, String active)
			throws NoSuchUserRoleFoundException;

}
