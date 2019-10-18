package com.ascent.autobcm.service;

import com.ascent.autobcm.exception.GroupAlreadyPresentException;
import com.ascent.autobcm.exception.NoSuchGroupFound;
import com.ascent.autobcm.model.Group;

public interface GroupService {

	public Group saveGroup(Group group) throws GroupAlreadyPresentException;

	public Group findByGroupName(String groupName);

	public Group findById(long groupName) throws NoSuchGroupFound;

	public Group updateGroup(Group group) throws NoSuchGroupFound, GroupAlreadyPresentException;

	public Group deleteExistingGroup(long groupName) throws NoSuchGroupFound;

	public Group reactivateExistingGroup(long groupName) throws NoSuchGroupFound;

}
