package com.ascent.autobcm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascent.autobcm.dao.UserGroupRepository;
import com.ascent.autobcm.model.Group;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.model.UserGroup;

@Service
public class UserGroupServiceImpl implements UserGroupService {

	@Autowired
	UserGroupRepository userGroupRepository;

	@Override
	public UserGroup saveUserGroup(UserGroup userGroup) {
		return userGroupRepository.save(userGroup);
	}

	@Override
	public List<UserGroup> saveAllUserGroup(List<UserGroup> userGroup) {
		return userGroupRepository.saveAll(userGroup);
	}

	@Override
	public List<UserGroup> findByGroupId(long groupId) {
		// TODO Auto-generated method stub
		return userGroupRepository.findByGroupId(groupId);
	}

	@Override
	public List<UserGroup> findByUserId(long userId) {
		List<UserGroup> groupsForUser = userGroupRepository.findByUserId(userId);
		return groupsForUser;
	}

	@Override
	public List<User> findUsersForGroup(long groupId) {
		List<User> userForGroup = userGroupRepository.findUsersAssignedForGroup(groupId);
		return userForGroup;
	}

	@Override
	public List<Group> findGroupsForUser(long userId) {
		List<Group> groupsForuser = userGroupRepository.findGroupsAssignedForUser(userId);
		return groupsForuser;
	}

}
