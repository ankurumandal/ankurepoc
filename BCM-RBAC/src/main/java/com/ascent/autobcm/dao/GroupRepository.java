package com.ascent.autobcm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ascent.autobcm.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
	
	public Group findByGroupName(String groupName);

	public Group findById(long groupId);
}
