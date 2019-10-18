package com.ascent.autobcm.service;

import com.ascent.autobcm.model.AuditLog;

public interface AuditLogService {
	
	public AuditLog saveAuditLog(AuditLog logToPersist);

}
