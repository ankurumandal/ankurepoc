package com.ascent.autobcm.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.ascent.autobcm.dao.AuditLogRepository;
import com.ascent.autobcm.model.AuditLog;

public class AuditLogServiceImpl implements AuditLogService {

	@Autowired
	AuditLogRepository auditLogRepository;

	@Override
	public AuditLog saveAuditLog(AuditLog logToPersist) {
		// TODO Auto-generated method stub
		return null;
	}

}
