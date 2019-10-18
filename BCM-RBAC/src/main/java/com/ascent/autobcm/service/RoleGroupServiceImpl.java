package com.ascent.autobcm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.RoleGroupRepository;
import com.ascent.autobcm.model.Group;
import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.RoleGroup;

@Service
public class RoleGroupServiceImpl implements RoleGroupService {

	@Autowired
	RoleGroupRepository roleGroupRepository;

	@Override
	public RoleGroup saveRoleGroupEntity(RoleGroup roleGroupEntity) {
		return roleGroupRepository.save(roleGroupEntity);
	}

	@Override
	public List<RoleGroup> findByGroupId(long groupId) {
		return roleGroupRepository.findByGroupId(groupId);
	}

	@Override
	public List<RoleGroup> saveAllRoleGroupEntity(List<RoleGroup> roleGroupEntity) {
		List<RoleGroup> persistedEntities = roleGroupRepository.saveAll(roleGroupEntity);
		return persistedEntities;
	}

	@Override
	public List<Role> findRolesByGroup(long groupId) {
		List<Role> rolesForGroups = roleGroupRepository.findByGroupAssigned(groupId);
		return rolesForGroups;
	}

	@Override
	public List<Group> findGroupsByRole(long roleId) {
		List<Group> groupForRoles = roleGroupRepository.findByRoleAssigned(roleId);
		return groupForRoles;
	}

}
