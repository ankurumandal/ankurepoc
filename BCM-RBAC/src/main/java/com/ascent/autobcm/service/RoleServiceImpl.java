package com.ascent.autobcm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ascent.autobcm.dao.RoleGroupRepository;
import com.ascent.autobcm.dao.RoleOperationsRepository;
import com.ascent.autobcm.dao.RoleRepository;
import com.ascent.autobcm.dao.UserRoleRepository;
import com.ascent.autobcm.exception.NoSuchRoleFoundException;
import com.ascent.autobcm.exception.RoleAlreadyPresentException;
import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.RoleGroup;
import com.ascent.autobcm.model.RoleOperations;
import com.ascent.autobcm.model.UserRole;
import com.ascent.autobcm.util.Constants;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	RoleOperationsRepository roleOperationsRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	RoleGroupRepository roleGroupRepository;

	@Override
	public Role saveRole(Role roleObjToPersist) throws RoleAlreadyPresentException {
		Optional<Role> role = null;

		role = roleRepository.findByRoleName(roleObjToPersist.getRoleName());

		if (role.isPresent() && roleObjToPersist.getId() != role.get().getId())
			throw new RoleAlreadyPresentException(Constants.ROLE_ALREADY_PRESENT + roleObjToPersist.getRoleName());

		Role persistedRole = roleRepository.save(roleObjToPersist);
		return persistedRole;
	}

	@Override
	public Role findByRoleName(String roleToBeFound) {
		Optional<Role> role = roleRepository.findByRoleName(roleToBeFound);
		if (role.isPresent()) {
			return role.get();
		}
		return null;
	}

	@Override
	public Role findById(long roleId) throws NoSuchRoleFoundException {
		Role role = roleRepository.findById(roleId);

		if (null == role)
			throw new NoSuchRoleFoundException(Constants.NO_ROLE_FOUND);
		return role;
	}

	@Override
	public boolean deleteById(long roleId) {
		roleRepository.deleteById(roleId);
		return true;

	}

	@Override
	public List<Role> findAllByActive(String activeStatus) {
		List<Role> roleList = roleRepository.findByActive(activeStatus);
		return roleList;
	}

	@Override
	public Role findByIdAndRoleName(long roleId, String roleName) {
		Role role = roleRepository.findByIdAndRoleName(roleId, roleName);
		return role;
	}

	@Override
	public Role updateRole(Role roleObjToPersist) throws RoleAlreadyPresentException, NoSuchRoleFoundException {
		Optional<Role> role = null;

		role = roleRepository.findByRoleName(roleObjToPersist.getRoleName());
		if (role.isPresent() && roleObjToPersist.getId() != role.get().getId())
			throw new RoleAlreadyPresentException(Constants.ROLE_ALREADY_PRESENT + roleObjToPersist.getRoleName());

		Role persistedRole = roleRepository.save(roleObjToPersist);
		return persistedRole;
	}

	@Override
	@Transactional
	public Role deleteRole(long roleId) throws NoSuchRoleFoundException {
		Role roleFromDb = null;

		roleFromDb = roleRepository.findById(roleId);
		if (null == roleFromDb)
			throw new NoSuchRoleFoundException(Constants.NO_ROLE_FOUND);

		// Disabling Role - Operations
		List<RoleOperations> correspondingRoleOperations = roleOperationsRepository.findByRoleIdAndStatus(roleId,
				Constants.ACTIVE);
		if (correspondingRoleOperations.size() > 0) {
			correspondingRoleOperations.forEach(item -> item.setActive(Constants.INACTIVE));
			roleOperationsRepository.saveAll(correspondingRoleOperations);
		}

		// Disabling User - Roles
		List<UserRole> correspondingUserRole = userRoleRepository.findByRoleIdAndActive(roleId, Constants.ACTIVE);
		if (correspondingUserRole.size() > 0) {
			correspondingUserRole.forEach(item -> item.setActive(Constants.INACTIVE));
			userRoleRepository.saveAll(correspondingUserRole);
		}

		// Disabling Group - Roles
		List<RoleGroup> correspondingRoleGroup = roleGroupRepository.findByRoleIdAndActive(roleId, Constants.ACTIVE);
		if (correspondingRoleGroup.size() > 0) {
			correspondingRoleGroup.forEach(item -> item.setActive(Constants.INACTIVE));
			roleGroupRepository.saveAll(correspondingRoleGroup);
		}

		roleFromDb.setActive(Constants.INACTIVE);
		roleFromDb = roleRepository.save(roleFromDb);

		return roleFromDb;
	}

	@Override
	public Role reactivateRole(long roleId) throws NoSuchRoleFoundException {
		Role roleFromDb = null;

		roleFromDb = roleRepository.findById(roleId);
		if (null == roleFromDb)
			throw new NoSuchRoleFoundException(Constants.NO_ROLE_FOUND);

		// Enabling Role - Operations
		List<RoleOperations> correspondingRoleOperations = roleOperationsRepository.findByRoleIdAndActive(roleId,
				Constants.ACTIVE);
		if (correspondingRoleOperations.size() > 0) {
			correspondingRoleOperations.forEach(item -> item.setActive(Constants.ACTIVE));
			roleOperationsRepository.saveAll(correspondingRoleOperations);
		}

		// Enabling User - Roles
		List<UserRole> correspondingUserRole = userRoleRepository.findByRoleId(roleId);
		if (correspondingUserRole.size() > 0) {
			correspondingUserRole.forEach(item -> item.setActive(Constants.ACTIVE));
			userRoleRepository.saveAll(correspondingUserRole);
		}

		// Enabling Group - Roles
		List<RoleGroup> correspondingRoleGroup = roleGroupRepository.findByRoleId(roleId);
		if (correspondingRoleGroup.size() > 0) {
			correspondingRoleGroup.forEach(item -> item.setActive(Constants.ACTIVE));
			roleGroupRepository.saveAll(correspondingRoleGroup);
		}

		roleFromDb.setActive(Constants.ACTIVE);
		roleFromDb = roleRepository.save(roleFromDb);

		return roleFromDb;
	}

}
