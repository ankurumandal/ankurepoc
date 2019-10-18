package com.ascent.autobcm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ascent.autobcm.model.Operations;

@Repository
public interface OperationsRepository extends JpaRepository<Operations, Long>  {
	
	public List<Operations> findByActive(String activeStatus);
	
	//public Operations findById(long operationsId);
	
	public Operations findByOperationName(String operationName);

}
