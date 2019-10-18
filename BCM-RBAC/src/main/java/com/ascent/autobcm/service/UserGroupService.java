package com.ascent.autobcm.service;

import java.util.List;

import com.ascent.autobcm.model.Group;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.model.UserGroup;

public interface UserGroupService {

	public UserGroup saveUserGroup(UserGroup userGroup);

	public List<UserGroup> saveAllUserGroup(List<UserGroup> userGroup);

	public List<UserGroup> findByGroupId(long groupId);

	public List<UserGroup> findByUserId(long userId);

	public List<User> findUsersForGroup(long groupId);

	public List<Group> findGroupsForUser(long userId);

}
