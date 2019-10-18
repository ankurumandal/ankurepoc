package com.ascent.autobcm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.GroupRepository;
import com.ascent.autobcm.dao.RoleGroupRepository;
import com.ascent.autobcm.dao.UserGroupRepository;
import com.ascent.autobcm.dao.UserRoleRepository;
import com.ascent.autobcm.exception.GroupAlreadyPresentException;
import com.ascent.autobcm.exception.NoSuchGroupFound;
import com.ascent.autobcm.model.Group;
import com.ascent.autobcm.model.RoleGroup;
import com.ascent.autobcm.model.UserGroup;
import com.ascent.autobcm.model.UserRole;
import com.ascent.autobcm.util.Constants;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Autowired
	RoleGroupRepository roleGroupRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Override
	public Group saveGroup(Group group) throws GroupAlreadyPresentException {
		Group groupFromDb = null;

		groupFromDb = groupRepository.findByGroupName(group.getGroupName());
		if (null != groupFromDb)
			throw new GroupAlreadyPresentException(Constants.GROUP_ALREADY_PRESENT + group.getGroupName());

		groupFromDb = groupRepository.save(group);
		return groupFromDb;
	}

	@Override
	public Group findByGroupName(String groupName) {
		return groupRepository.findByGroupName(groupName);
	}

	@Override
	public Group findById(long groupId) throws NoSuchGroupFound {
		Group groupFromDb = groupRepository.findById(groupId);

		if (null == groupFromDb)
			throw new NoSuchGroupFound(Constants.NO_GROUP_FOUND);

		return groupRepository.findById(groupId);
	}

	@Override
	public Group updateGroup(Group group) throws NoSuchGroupFound, GroupAlreadyPresentException {
		Group groupFromDb = null;

		groupFromDb = groupRepository.findById(group.getId());
		if (null != groupFromDb)
			throw new NoSuchGroupFound(Constants.NO_GROUP_FOUND + group.getGroupName());

		groupFromDb = groupRepository.findByGroupName(group.getGroupName());
		if (null != groupFromDb)
			throw new GroupAlreadyPresentException(Constants.GROUP_ALREADY_PRESENT + group.getGroupName());

		groupFromDb = groupRepository.save(group);
		return groupFromDb;
	}

	@Override
	public Group deleteExistingGroup(long groupId) throws NoSuchGroupFound {
		Group groupToDelete = null;
		List<Long> roleIds = new ArrayList<Long>();
		List<Long> userIds = new ArrayList<Long>();

		groupToDelete = groupRepository.findById(groupId);
		if (null == groupToDelete)
			throw new NoSuchGroupFound(Constants.NO_GROUP_FOUND);

		// Disable User Group
		List<UserGroup> existingUserGroupMappings = userGroupRepository.findByGroupId(groupId);
		if (existingUserGroupMappings.size() > 0) {
			existingUserGroupMappings.stream().peek(item -> userIds.add(item.getUser().getId()))
					.forEach(item -> item.setActive(Constants.INACTIVE));
		}
		userGroupRepository.saveAll(existingUserGroupMappings);

		// Disable Role Group
		List<RoleGroup> existingRoleGroupMappings = roleGroupRepository.findByGroupId(groupId);
		if (existingRoleGroupMappings.size() > 0) {
			existingRoleGroupMappings.stream().peek(item -> roleIds.add(item.getRole().getId()))
					.forEach(item -> item.setActive(Constants.INACTIVE));
		}
		roleGroupRepository.saveAll(existingRoleGroupMappings);

		List<UserRole> userRoleMapping = userRoleRepository.findByRoleIdInAndUserIdInAndActive(roleIds, userIds,
				Constants.ACTIVE);
		userRoleMapping.stream().forEach(item -> item.setActive(Constants.INACTIVE));
		userRoleRepository.saveAll(userRoleMapping);

		groupToDelete.setActive(Constants.INACTIVE);
		groupToDelete = groupRepository.save(groupToDelete);
		return groupToDelete;
	}

	@Override
	public Group reactivateExistingGroup(long groupId) throws NoSuchGroupFound {
		Group groupToDelete = null;
		List<Long> roleIds = new ArrayList<Long>();
		List<Long> userIds = new ArrayList<Long>();

		groupToDelete = groupRepository.findById(groupId);
		if (null == groupToDelete)
			throw new NoSuchGroupFound(Constants.NO_GROUP_FOUND);

		// Enable User Group
		List<UserGroup> existingUserGroupMappings = userGroupRepository.findByGroupId(groupId);
		if (existingUserGroupMappings.size() > 0) {
			existingUserGroupMappings.stream().peek(item -> userIds.add(item.getUser().getId()))
					.forEach(item -> item.setActive(Constants.ACTIVE));
		}
		userGroupRepository.saveAll(existingUserGroupMappings);

		// Enable Role Group
		List<RoleGroup> existingRoleGroupMappings = roleGroupRepository.findByGroupId(groupId);
		if (existingRoleGroupMappings.size() > 0) {
			existingRoleGroupMappings.stream().peek(item -> roleIds.add(item.getRole().getId()))
					.forEach(item -> item.setActive(Constants.ACTIVE));
		}
		roleGroupRepository.saveAll(existingRoleGroupMappings);

		List<UserRole> userRoleMapping = userRoleRepository.findByRoleIdInAndUserIdInAndActive(roleIds, userIds,
				Constants.INACTIVE);
		userRoleMapping.stream().forEach(item -> item.setActive(Constants.ACTIVE));
		userRoleRepository.saveAll(userRoleMapping);

		groupToDelete.setActive(Constants.ACTIVE);
		groupToDelete = groupRepository.save(groupToDelete);
		return groupToDelete;
	}

}
